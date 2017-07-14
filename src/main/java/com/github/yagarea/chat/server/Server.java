package com.github.yagarea.chat.server;

import com.github.yagarea.chat.shared.security.RSA;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    public static void main(String[] args) {
        RSA decryptor = new RSA();
        final int PORT = Integer.parseInt(args[1]);

        ServerSocket chatServer = null;
        try {
            chatServer = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, ClientConnection> clients = new ConcurrentHashMap<>();
        Authenticator auth = new Authenticator(args[0]);


        while (chatServer != null) {
            try {
                Socket clientSocket = chatServer.accept();

                ClientConnectionRunnable clientConnectionInit = new ClientConnectionRunnable(clientSocket, clients, decryptor, auth);

                new Thread(clientConnectionInit).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}