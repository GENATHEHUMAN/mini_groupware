package com.gunwoo.nnsoftgroupware.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// CORS 관련 import
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
			.csrf(csrf -> csrf.disable())
			
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    // 인증되지 않은 사용자가 보호된 리소스에 접근 시 401 Unauthorized 반환
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write("인증이 필요합니다.");
                })
            )
			
			.authorizeHttpRequests(authz -> authz
					.requestMatchers("/api/users/register", "/api/users/login").permitAll()
					.anyRequest().authenticated()
			)
			.formLogin(form -> form
					.loginProcessingUrl("/api/users/login")
					.usernameParameter("username")
					.passwordParameter("password")
					.successHandler((request, response, authentication) -> {
						response.setStatus(HttpStatus.OK.value());
						
						response.setCharacterEncoding("UTF-8");
		                response.setContentType("text/plain;charset=UTF-8");
						
						response.getWriter().write("로그인 성공");
					})
					.failureHandler((request, response, exception) -> {
						response.setStatus(HttpStatus.UNAUTHORIZED.value());
						
						response.setCharacterEncoding("UTF-8");
		                response.setContentType("text/plain;charset=UTF-8");
		                
						response.getWriter().write("로그인 실패: " + exception.getMessage());
					})
			);
		
		return http.build();
	}
	
	// ▼▼▼ CORS 설정을 위한 Bean 추가 ▼▼▼
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        configuration.addAllowedOrigin("http://localhost:3000"); // 3000번 포트의 요청을 허용
        configuration.addAllowedMethod("*"); // 모든 HTTP 메소드(GET, POST, 등)를 허용
        configuration.addAllowedHeader("*"); // 모든 헤더를 허용
        configuration.setAllowCredentials(true); // 쿠키와 같은 인증 정보를 함께 보낼 수 있도록 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 URL 경로에 대해 위 설정을 적용
        return source;
    }
}







