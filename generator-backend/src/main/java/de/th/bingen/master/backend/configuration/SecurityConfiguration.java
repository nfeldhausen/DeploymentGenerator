package de.th.bingen.master.backend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        List<String> origins = Arrays.asList("*");
        List<String> methods = Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH");
        List<String> headers = Arrays.asList("Authorization", "Cache-Control", "Content-Type");

        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.setAllowedOrigins(origins);
        corsConfig.setAllowedMethods(methods);
        corsConfig.setAllowedHeaders(headers);
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
        corsConfigSource.registerCorsConfiguration("/**", corsConfig);

        return corsConfigSource;
    }
}
