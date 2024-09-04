package org.v1.job_coach.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String provider;

    @Column
    private String providerId;

    @Column
    private Role role;

    @Column
    private Timestamp created_at;

    @Column
    private Timestamp updated_at;

    /* 일반 사용자 회원가입 */
    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /* OAuth2 사용자 회원가입 */
    public User(String name, String email, String password, String provider, String providerId, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.role = role;
    }

    void update(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}


/*

필드:
id: 사용자 고유 식별자
email: 이메일 주소
password: 비밀번호 (일반 회원가입 시)
oauthProvider: 소셜 로그인 제공자 (구글, 네이버 등)
name: 사용자 이름
role: 사용자 역할 (예: USER, ADMIN)
created_at: 계정 생성일
updated_at: 계정 수정일*/
