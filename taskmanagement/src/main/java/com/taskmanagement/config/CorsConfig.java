package com.taskmanagement.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // ✅ Chỉ định rõ origin (không dùng "*")
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "https://quizapp-fe.vercel.app"
        ));

        // ✅ Thêm OPTIONS (preflight)
        config.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));

        // ✅ Cho phép tất cả headers (tránh lỗi cookie / auth)
        config.setAllowedHeaders(List.of("*"));

        // ✅ Quan trọng khi dùng cookie
        config.setAllowCredentials(true);

        // ✅ (Optional nhưng nên có)
        config.setExposedHeaders(List.of("Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
