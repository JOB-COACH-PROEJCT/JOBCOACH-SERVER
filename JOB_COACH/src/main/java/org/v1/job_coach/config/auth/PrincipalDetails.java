package org.v1.job_coach.config.auth;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.v1.job_coach.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//권한 체크
@Getter
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final User user;

    private Map<String, Object> attributes; //권한<providerId, 객체>

    //일반 로그인할 때 사용
    public PrincipalDetails(User user) {
        this.user = user;
    }

    //OAuth2 로그인할 때 사용
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {    /* User 권한 return */
        /* user의 권한(Role)은 Enum타입이라 Collection으로 반환해줄 수 없음 */

        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(user.getRole());
            }
        });
        return collect;
    }


    @Override
    public String getName() {
        return user.getName();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }


}
