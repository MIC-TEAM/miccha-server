package com.miccha.server;

import com.miccha.server.config.CorsConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.List;

@AllArgsConstructor
@Configuration
public class CorsConfiguration implements WebFluxConfigurer {
    private CorsConfig corsConfig;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        final List<String> corsOrigins = corsConfig.getOrigins();
        registry.addMapping("/**")
                .allowedOrigins(corsOrigins.toArray(new String[corsOrigins.size()]));
    }
}
