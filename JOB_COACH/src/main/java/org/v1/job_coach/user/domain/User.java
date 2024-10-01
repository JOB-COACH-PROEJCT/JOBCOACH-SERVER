package org.v1.job_coach.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.v1.job_coach.user.dto.request.SignUpRequestDto;
import org.v1.job_coach.domain.mypage.dto.request.UserUpdateRequestDto;
import org.v1.job_coach.domain.chatroom.domain.ChatRoom;
import org.v1.job_coach.domain.consulting.domain.Consulting;
import org.v1.job_coach.domain.community.domain.Board;
import org.v1.job_coach.domain.community.domain.Comment;
import org.v1.job_coach.domain.review.domain.Review;

import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"email", "isActive"})})
@Builder
@AllArgsConstructor
public class User implements UserDetails {
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private boolean isActive = true;

    @Column
    private LocalDateTime deactivatedAt; // 비활성화된 날짜

    @Column(nullable = false)
    @NotBlank(message = "사용자 이름은 필수 항목입니다.")
    @Size(min = 2, max = 10, message = "사용자 이름은 2자 이상, 10자 이하여야 합니다.")
    private String name;

    @Email(message = "유효한 이메일 주소를 입력해 주세요.")
    @NotBlank(message = "이메일은 필수 항목입니다.")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10자리 또는 11자리 숫자로 입력해 주세요.")
    @Column(nullable = false)
    private String number;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    @Size(min = 6, max = 20, message = "비밀번호는 6자 이상, 20자 이하여야 합니다.")
    @Column(nullable = false)
    private String password;

    @Column
    @JsonIgnore
    private String profile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Consulting> ConsultingList = new ArrayList<>(); // 유저가 생성한 채팅룸

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ChatRoom> chatRooms = new ArrayList<>(); // 유저가 생성한 채팅룸

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 이 User가 작성한 Board들을 관리
    private List<Board> board;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;  // 유저가 작성한 후기 리스트

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // 이 User가 작성한 Comment들을 관리
    private List<Comment> comments;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    //* @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // 해당 필드를 JSON 직렬화와 역직렬화에서 "쓰기 전용"으로 설정
    // 응답으로 User 객체를 JSON으로 변환할 때), 이 필드는 JSON에 포함되지 않음
    // 즉, 클라이언트는 이 필드의 값을 읽을 수 없음.*//
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.email;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "pid=" + pid +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(pid, user.pid); // ID를 기준으로 동등성 비교
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid);
    }

    public boolean updateUser(UserUpdateRequestDto updateRequest, String password) {
        this.password = password;
        this.number = updateRequest.phoneNumber();
        this.profile = updateRequest.profileUrl();
        return true;
    }

    public void deactivate(){
        this.isActive = false;
        this.deactivatedAt = LocalDateTime.now();
    }

    public void activate(SignUpRequestDto signUpRequestDto, String password){
        this.isActive = true;
        this.number = signUpRequestDto.number();
        this.password = password;
        this.name = signUpRequestDto.name();
        this.profile = signUpRequestDto.profile();
        this.deactivatedAt = null;
    }
}

