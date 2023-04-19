package com.buchu.greenfarm.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final AuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers().frameOptions().disable()
                .and().authorizeHttpRequests()
                    .requestMatchers(HttpMethod.POST,"/**").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/**").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/**").authenticated()
                    .requestMatchers("/notifications").authenticated()
                    .requestMatchers("/u/register").hasRole("GUEST")
                    .requestMatchers("/*/follow").hasRole("USER")
                    .anyRequest().permitAll()
                .and().logout()
                    .logoutUrl("/u/logout")
                    .logoutSuccessUrl("/u/login")
                    .invalidateHttpSession(true)
                    .permitAll()
                .and().exceptionHandling()
                    .accessDeniedHandler(customAccessDeniedHandler)
                .and().oauth2Login()
                    .loginPage("/u/login").successHandler(customAuthenticationSuccessHandler)
                    .userInfoEndpoint().userService(customOAuth2UserService);
        return http.build();
    }
}
