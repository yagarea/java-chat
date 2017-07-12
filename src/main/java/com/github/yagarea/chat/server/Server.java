package com.github.yagarea.chat.server;

import com.github.yagarea.chat.shared.security.RSA;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    public static void main(String[] args) {
        RSA encryptor = new RSA();
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
                ClientConnection newClient = new ClientConnection(clientSocket, clients, encryptor, auth);

                new Thread(newClient).start();
                clients.put(newClient.username, newClient);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}