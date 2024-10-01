package org.v1.job_coach.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.v1.job_coach.global.auth.handler.CustomAccessDeniedHandler;
import org.v1.job_coach.global.auth.handler.CustomAuthenticationEntryPoint;
import org.v1.job_coach.global.auth.jwt.JwtAuthenticationFilter;
import org.v1.job_coach.global.auth.jwt.JwtTokenProvider;

@Configuration
@EnableWebSecurity /* ->스프링 시큐리티 필터가 스프링 필터체인에 등록된다. */
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(CustomAuthenticationEntryPoint customAuthenticationEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler, JwtTokenProvider jwtTokenProvider) {
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSessionRequestCache cache = new HttpSessionRequestCache();
        //로그인하면 뒤에 자꾸 ?continue 이런거 뜨는데 잠시 비활성화 해주려고 일단 추가함
        cache.setMatchingRequestParameterName(null);

        http.csrf(CsrfConfigurer<HttpSecurity>::disable)
                .headers(header -> header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .requestCache(requestCache -> requestCache
                        .requestCache(cache))
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("/user/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/sign/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() //-> /v3/api-docs/** 추가해주니까 됐음...
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 아래 라인에서 exceptionHandling()을 한 번만 호출하도록 변경
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
