package com.miccha.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//EMAIL 설정 객체
@Getter
@Setter
@Component
@ConfigurationProperties("mail")
public class MailConfig {
    private EmailServerConfig server;
    private String sender;
    private int threadCount;
}
