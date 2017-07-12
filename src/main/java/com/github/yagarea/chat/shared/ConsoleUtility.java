package com.github.yagarea.chat.shared;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUtility {
    private Console console;
    private BufferedReader reader;

    public ConsoleUtility() {
        console = System.console();
        if (console == null) {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
    }

    public String readLine() throws IOException {
        if (console != null) {
            return console.readLine();
        }
        return reader.readLine();
    }

    public String readPassword() throws IOException {
        if (console != null) {
            return new String(console.readPassword());
        }
        return reader.readLine();
    }
}
