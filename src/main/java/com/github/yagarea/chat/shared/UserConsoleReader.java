package com.github.yagarea.chat.shared;

import java.io.Console;

public class UserConsoleReader implements UserReader {
    private final Console console = System.console();

    @Override
    public String readLine() {
        return console.readLine();
    }

    @Override
    public String readPassword() {
        return new String(console.readPassword());
    }
}
