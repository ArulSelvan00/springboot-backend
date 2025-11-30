package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 🚀 CRITICAL FIX: Add your Netlify URL and the localhost URL
        config.setAllowedOriginPatterns(Arrays.asList(
                "https://endearing-heliotrope-12d102.netlify.app", // ✅ Your Deployed Frontend
                "http://localhost:3000",
                "http://localhost:8080" // If you test locally on Spring Boot port
        ));

        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        // This is necessary since you are using allowedOriginPatterns
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Apply this configuration to all paths ("/**")
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}