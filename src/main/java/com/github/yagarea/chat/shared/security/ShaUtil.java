package com.github.yagarea.chat.shared.security;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class ShaUtil {
    public static void main(String[] args) {
        System.out.println(hash("newPassword"));

    }


    public static String hash(String input) {
        return Hashing.sha256().hashString(input, Charsets.UTF_8).toString();
    }
}
