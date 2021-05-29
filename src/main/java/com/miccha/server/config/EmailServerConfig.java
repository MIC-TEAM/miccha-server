package com.miccha.server.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailServerConfig {
    private String host;
    private int port;
}
