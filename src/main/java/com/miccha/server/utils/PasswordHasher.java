package com.miccha.server.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    private MessageDigest messageDigest;

    public PasswordHasher(String algorithm) {
        try {
            messageDigest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getHahsedPassword(String plainTextPassword) {
        messageDigest.reset();
        messageDigest.update(plainTextPassword.getBytes());
        return String.format("%064x", new BigInteger(1, messageDigest.digest()));
    }
}
