package com.arina.image_module.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfig()))
                .authorizeHttpRequests(auth -> auth
                        // ✅ Allow preflight requests
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ Allow image APIs
                        .requestMatchers("/image/**").permitAll()

                        // ✅ Allow everything else
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfig() {

        CorsConfiguration config = new CorsConfiguration();

        // ✅ Allowed origins (NO wildcard when credentials=false)
        config.setAllowedOrigins(List.of(
                "https://usersmodule.netlify.app",
                "http://localhost:5173"
        ));

        // ✅ Allowed methods
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        // ✅ Allowed headers
        config.setAllowedHeaders(List.of("*"));

        // ✅ Credentials OFF (required for Spring Boot 4)
        config.setAllowCredentials(false);

        // ✅ Expose headers
        config.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
