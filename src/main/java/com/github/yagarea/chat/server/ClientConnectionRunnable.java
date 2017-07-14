package com.github.yagarea.chat.server;

import com.github.yagarea.chat.shared.security.RSA;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class ClientConnectionRunnable implements Runnable {


    private final Socket clientSocket;
    private final Map<String, ClientConnection> clients;
    private final RSA decryptor;
    private final Authenticator auth;

    public ClientConnectionRunnable(Socket clientSocket, Map<String, ClientConnection> clients, RSA decryptor, Authenticator auth) {

        this.clientSocket = clientSocket;
        this.clients = clients;
        this.decryptor = decryptor;
        this.auth = auth;
    }

    @Override
    public void run() {
        try {
            ClientConnection newClient = new ClientConnection(clientSocket, clients, decryptor, auth);
            clients.put(newClient.username, newClient);
            newClient.startListenig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
