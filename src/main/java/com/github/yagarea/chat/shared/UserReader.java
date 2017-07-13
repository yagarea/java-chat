package com.github.yagarea.chat.shared;

import java.io.IOException;

public interface UserReader {
    String readLine() throws IOException;
    String readPassword() throws IOException;
}
