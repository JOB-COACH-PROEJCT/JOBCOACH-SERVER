package org.v1.job_coach.user.application;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserDetailService {
    UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException;
}
