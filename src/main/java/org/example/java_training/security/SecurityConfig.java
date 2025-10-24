package org.example.java_training.security;

import org.example.java_training.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    private CustomAuthHandler customAuthHandler;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtUtil jwtUtil
    ) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        http

        .securityMatcher("/api/**")
        .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF vì dùng JWT
        .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(customAuthHandler) // Xử lý lỗi 401
                    .accessDeniedHandler(customAuthHandler)      // Xử lý lỗi 403
        )


        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()         // Đăng nhập, đăng ký → cho phép
//                .requestMatchers("/api/products/**").authenticated()    // CRUD post → cần xác thực
                .requestMatchers("/api/products/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN") // chỉ ADMIN được vào
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // USER và ADMIN đều được
//                .anyRequest().permitAll()                            // Các route còn lại → cho phép
                        .anyRequest().authenticated()
        )
        .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Không dùng session
        )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Thêm filter JWT

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
}