package org.v1.job_coach.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwt = "JWT"; // JWT (JSON Web Token) 인증 방식을 나타내는 문자열을 정의 .이 이름은 보안 스키마를 참조하는 데 사용됩니다.
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwt);
        // API 엔드포인트가 이 보안 요구 사항을 충족해야 함을 나타내는 객체. addList(jwt) 메서드를 사용하여 JWT 인증이 필요한 엔드포인트를 추가합니다.

        //OpenAPI 문서에서 사용할 보안 스키마를 정의하는 객체생성
        //SecurityScheme: HTTP Bearer 인증 방식을 사용하여 JWT를 처리하도록 설정
        Components components = new Components().addSecuritySchemes(jwt, new SecurityScheme() //
                .name(jwt)  //보안 스키마의 이름을 정의
                .type(SecurityScheme.Type.HTTP) //인증 유형을 HTTP로 설정
                .scheme("bearer")   //인증 방식으로 Bearer를 설정
                .bearerFormat("JWT")    // JWT 형식을 명시
        );

        return new OpenAPI()    //OpenAPI 사양을 정의하는 객체
                .components(new Components())   //OpenAPI 객체에 Components를 설정. 이 Components는 기본적으로 빈 객체로 초기화되며, 나중에 보안 스키마를 추가
                .info(apiInfo())    //API의 기본 정보를 설정하는 메서드
                .addSecurityItem(securityRequirement) //정의한 보안 요구 사항(securityRequirement)을 OpenAPI 객체에 추가. 이 설정으로 인해 Swagger UI에서 JWT 인증을 요구하는 엔드포인트를 명시합
                .components(components);    //앞서 정의한 보안 스키마(components)를 OpenAPI 객체에 추가. 설정은 Swagger UI가 API의 보안 요구 사항을 인식하고 적절한 인증 정보를 요청할 수 있게 함
    }

    private Info apiInfo() {    //OpenAPI 문서에서 API의 메타 정보를 설정하는 객체입니다.
        return new Info()
                .title("Swagger Test")  //API 제목
                .description("Swagger UI")  //API 설명
                .version("1.0.0."); //API 버전

    }
}