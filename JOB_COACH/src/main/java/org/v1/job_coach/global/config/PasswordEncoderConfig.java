/*
package org.v1.job_coach.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Configuration
public class PasswordEncoderConfig {
    */
/* BCryptPsswordEncoder 객체를 사용할 때마다 생성하지 않고 스프링 빈으로 등록하여 하나의 인스턴스로 여러번 사용하기 위함 !*//*

    @Bean
    public BCryptPasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }
}
*/
