package com.github.yagarea.chat.shared.security;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class ShaUtil {
    public static void main(String[] args) {
        System.out.println(hash("honza"));
        System.out.println(hash("lsm"));
        System.out.println(hash("java"));
        System.out.println(hash("petra"));
    }


    public static String hash(String input) {
        return Hashing.sha256().hashString(input, Charsets.UTF_8).toString();
    }
}
