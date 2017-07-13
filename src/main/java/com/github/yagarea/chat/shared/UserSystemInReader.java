package com.github.yagarea.chat.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by John on 13.07.17.
 */
public class UserSystemInReader implements UserReader {
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public String readLine() throws IOException {
        return reader.readLine();
    }

    @Override
    public String readPassword() throws IOException {
        return reader.readLine();
    }
}
