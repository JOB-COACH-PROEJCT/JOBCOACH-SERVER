package org.v1.job_coach.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

@Configuration
@EnableWebSecurity /* -> 스프링 시큐리티 필터가 스프링 필터체인에 등록된다. */

public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        HttpSessionRequestCache cache = new HttpSessionRequestCache();
        cache.setMatchingRequestParameterName(null);//로그인하면 뒤에 자꾸 ?continue 이런거 뜨는데 잠시 비활성화 해주려고 일단 추가함

        http.csrf(AbstractHttpConfigurer::disable)
                .requestCache(requestCache -> requestCache
                        .requestCache(cache))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .anyRequest().permitAll())

                .formLogin(f -> f
                        .loginPage("/loginForm")
                        .permitAll()
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/loginForm")
                )

                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .logout((logout -> logout.logoutSuccessUrl("/loginForm")));

/*
                .oauth2Login(oauth -> oauth
                        .loginPage("/loginForm")
                        .userInfoEndpoint(info -> info.userService(null))
                );*/

        return http.build();

    }

}
