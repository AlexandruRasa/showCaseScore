package com.showcaseScore.movieApp.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/movie/get/**") //-/** Add the endpoint you want to allow CORS for
                .allowedOrigins("http://localhost:3000") // Add the origin of your React app
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Add the HTTP methods you want to allow
                .allowCredentials(true); // Allow credentials (e.g., cookies)

        registry.addMapping("/authenticate") // Add the endpoint for login
                .allowedOrigins("http://localhost:3000") // Allow requests from your React app
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Add the HTTP methods you want to allow
                .allowCredentials(true); // Allow credentials (if applicable)

        registry.addMapping("/review") // Add the endpoint for login
                .allowedOrigins("http://localhost:3000") // Allow requests from your React app
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Add the HTTP methods you want to allow
                .allowCredentials(true); // Allow credentials (if applicable)

        registry.addMapping("https://googleads.g.doubleclick.net/**") // Add the endpoint for Google Ads
                .allowedOrigins("*") // Allow requests from any origin (you may restrict this if needed)
                .allowedMethods("GET"); // Allow GET requests
    }
}

