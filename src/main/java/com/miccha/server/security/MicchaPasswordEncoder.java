package com.miccha.server.security;

import com.miccha.server.utils.PasswordHasher;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class MicchaPasswordEncoder implements PasswordEncoder {
    private PasswordHasher hasher;

    @Override
    public String encode(CharSequence rawPassword) {
        return hasher.getHahsedPassword(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
