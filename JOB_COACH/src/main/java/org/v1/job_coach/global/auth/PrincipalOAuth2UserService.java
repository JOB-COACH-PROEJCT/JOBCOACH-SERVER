/*
package org.v1.job_coach.config.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.v1.job_coach.config.auth.PrincipalDetails;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.repository.UserRepository;

@Slf4j
@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public PrincipalOAuth2UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    */
/* 구글로부터 받은 userRequest 데이터에 대한 후처리 -> loadUser *//*

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthorizationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("userRequest getAttributes: {}", super.loadUser(userRequest).getAttributes());

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.info("Google 로그인 요청");
        }else if (userRequest.getClientRegistration().getRegistrationId().equals("Naver")){
            log.info("Naver 로그인 요청");
        }

        */
/* 사용자 정보 가져오기 *//*

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerId;
        String password = bCryptPasswordEncoder.encode("잡코치 비밀번호 겟인데어");
        String email = oAuth2User.getAttribute("email");
        Role role = Role.ROLE_USER;

        User findUser = userRepository.findByName(username);

        if (findUser == null) {
            findUser = new User(username, password, email, provider, providerId, role);
            userRepository.save(findUser);
        }

        return new PrincipalDetails(findUser, oAuth2User.getAttributes());

    }
}
*/
