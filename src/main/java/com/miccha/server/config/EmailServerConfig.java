package com.miccha.server.config;

import lombok.Getter;
import lombok.Setter;

//EMAIL 서버 설정 불러오는 객체
@Getter
@Setter
public class EmailServerConfig {
    private String host;
    private int port;
}
