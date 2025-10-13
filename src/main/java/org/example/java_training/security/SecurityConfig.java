package org.example.java_training.security;

import org.example.java_training.service.impl.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
//import com.dev.mvc_spring.security.JwtAuthEntryPoint;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtUtil jwtUtil,
                          JwtAuthEntryPoint jwtAuthEntryPoint,
                          CustomAccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        http
        .csrf(AbstractHttpConfigurer::disable) // Tắt CSRF vì dùng JWT
        .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthEntryPoint) //custom log lỗi 401 không có token
                .accessDeniedHandler(accessDeniedHandler)     // ✅ lỗi 403
        )


        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()         // Đăng nhập, đăng ký → cho phép
//                .requestMatchers("/api/products/**").authenticated()    // CRUD post → cần xác thực
                .requestMatchers("/api/products/**").hasRole("ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN") // chỉ ADMIN được vào
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // USER và ADMIN đều được
                .anyRequest().permitAll()                            // Các route còn lại → cho phép
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