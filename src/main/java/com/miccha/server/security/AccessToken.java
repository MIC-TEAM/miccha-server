package com.miccha.server.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccessToken {
    private final String accessToken;
    private final long expiration;
}
