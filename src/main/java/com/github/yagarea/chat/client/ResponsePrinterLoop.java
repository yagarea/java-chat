package com.github.yagarea.chat.client;

import com.github.yagarea.chat.shared.security.RSA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ResponsePrinterLoop implements Runnable {
    private BufferedReader socketReader;
    private RSA decryptor;


    public ResponsePrinterLoop(Socket input, RSA decryptor) throws IOException {
        this.decryptor = decryptor;
        this.socketReader = new BufferedReader(new InputStreamReader(input.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String line = socketReader.readLine();
                if (line != null) {
                    System.out.println(decryptor.decryptString(line));
                } else {
                    System.err.println("SERVER DISCONNECTED");
                    // Must by sys.exit - when only break; -ing, main Thread will continue to run
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine() throws IOException {
        return socketReader.readLine();
    }
}
