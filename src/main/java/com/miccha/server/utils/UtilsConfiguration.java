package com.miccha.server.utils;

import com.miccha.server.config.Config;
import com.miccha.server.config.MailConfig;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AllArgsConstructor
@Configuration
public class UtilsConfiguration {
    private Config config;
    private MailConfig mailConfig;

    @Bean
    public PasswordHasher getPasswordHasher() {
        return new PasswordHasher(config.getHashAlgorithm());
    }

    @Bean
    public EmailSender getEmailSender() {
        ExecutorService executorService = Executors.newFixedThreadPool(mailConfig.getThreadCount());
        return new EmailSender(executorService,
                               mailConfig.getSender(),
                               mailConfig.getServer().getHost(),
                               mailConfig.getServer().getPort());
    }
}
