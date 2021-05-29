package com.miccha.server.utils;

import com.miccha.server.config.Config;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class UtilsConfiguration {
    private Config config;

    @Bean
    public PasswordHasher getPasswordHasher() {
        return new PasswordHasher(config.getHashAlgorithm());
    }
}
