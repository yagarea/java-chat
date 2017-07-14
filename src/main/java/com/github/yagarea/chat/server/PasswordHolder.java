package com.github.yagarea.chat.server;

import com.github.yagarea.chat.shared.security.ShaUtil;

import java.util.Random;

public class PasswordHolder {
    private final String salt;
    private final String hash;

    public PasswordHolder(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }

    public PasswordHolder(String password) {
        Random saltGenerator = new Random();
        this.salt = Long.toString(Math.abs(saltGenerator.nextLong()));
        this.hash = ShaUtil.hash(salt + password);
    }

    public String getSalt() {
        return salt;
    }

    public String getHash() {
        return hash;
    }
}
