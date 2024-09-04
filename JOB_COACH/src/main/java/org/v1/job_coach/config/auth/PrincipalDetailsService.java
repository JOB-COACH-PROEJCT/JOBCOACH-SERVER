package org.v1.job_coach.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.v1.job_coach.entity.User;
import org.v1.job_coach.repository.UserRepository;

@Slf4j
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public PrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override

    //자동으로 실행 됨 -> user가 있는지 확인하는 메서드라고 할 수 있음
    //userRepository에 user의 값이 없을경우 return값이 null이 되면서 UsernameNotFoundException 발생
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepository.findByName(username);
        if (userEntity != null){
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
