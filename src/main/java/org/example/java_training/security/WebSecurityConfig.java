package org.example.java_training.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
@Order(2) // Áp dụng sau API config
public class WebSecurityConfig {

    @Autowired
    private WebAuthHandler webAuthHandler;

    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/**") // Áp dụng cho toàn bộ web route
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(webAuthHandler)
                        .accessDeniedHandler(webAuthHandler)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/sign-in", "/sign-up", "/register", "/css/**", "/js/**", "/img/**", "/static/**"
                        ).permitAll()
                        .requestMatchers("/products/create/**", "/products/edit/**", "/products/update/**", "/products/delete/**")
                        .hasRole("ADMIN")

                        // Both USER and ADMIN can view list
                        .requestMatchers("/products/**").hasAnyRole("USER", "ADMIN")

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/dashboard").authenticated()
                )

                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                // Dùng session cho web login thủ công
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        return http.build();
    }
}
