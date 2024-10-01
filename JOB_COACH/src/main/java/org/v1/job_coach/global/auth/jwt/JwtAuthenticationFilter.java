package org.v1.job_coach.global.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
/*        // /api/v1/gpt 경로에 대한 필터링 건너뛰기
        String requestURI = request.getRequestURI();
        logger.info("[doFilterInternal] 요청 URI: {}", requestURI);
        if (requestURI.startsWith("/api/v1/gpt")) {
            logger.info("[doFilterInternal: TEST ] /api/v1/gpt 경로에 대한 요청이므로 토큰 검증을 건너뜁니다.");
            filterChain.doFilter(request, response);
            return;
        }*/

        String token = jwtTokenProvider.resolveToken(request);
        logger.info("[doFilterInternal] token 값 추출 완료. token : {}", token);

        logger.info("[doFilterInternal] token 값 유효성 체크 시작");
        if(token!=null&& jwtTokenProvider.validationToken(token)){
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("[doFilterInternal] token 값 유효성 체크 완료");
        }
        filterChain.doFilter(request,response);

    }

}