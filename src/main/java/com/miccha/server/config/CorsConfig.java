package com.miccha.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//CORS 설정 불러오는 객체
@Getter
@Setter
@Component
@ConfigurationProperties("cors")
public class CorsConfig {
    private List<String> origins = new ArrayList<>();
}
