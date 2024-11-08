package org.v1.job_coach.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.v1.job_coach.user.domain.User;

@Component
public class SecurityUtil {

    // 현재 인증된 사용자의 사용자명(Username)을 반환
    public static String getCurrentMemberUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("No authentication information available");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }

        throw new IllegalStateException("Cannot extract username from the principal");
    }

    // 현재 인증된 사용자의 UserPId 반환
    public static Long getCurrentMemberUserPid() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalStateException("No authentication information available");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((User) principal).getUserPid(); // User 객체에서 userPid 반환
        }

        throw new IllegalStateException("Cannot extract user PId from the principal");
    }

}
