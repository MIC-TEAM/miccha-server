package com.miccha.server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class Config {
    @Value("${home.pageSize}")
    private int pageSize;
}
