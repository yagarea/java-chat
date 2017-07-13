package com.github.yagarea.chat.server;

import com.github.yagarea.chat.shared.LoginResponse;
import com.github.yagarea.chat.shared.security.RSA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientConnection implements Runnable {
    private static final Pattern PRIVATE_MESSAGE_NICKNAME_PATTERN = Pattern.compile("@(\\w+) (.*)");
    private static final Pattern NICKNAME_RULES = Pattern.compile("\\w+");


    protected final String username;
    private final PrintWriter socketWriter;
    private final Map<String, ClientConnection> clients;
    private final RSA decryptor;
    private final RSA encryptor;
    private final BufferedReader socketReader;
    private final Authenticator authenticator;

    ClientConnection(Socket clientSocket, Map<String, ClientConnection> clients, RSA decryptor, Authenticator authenticator) throws IOException {
        this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
        this.clients = clients;
        this.decryptor = decryptor;

        sendEncryptionKeys();
        this.authenticator = authenticator;
        encryptor = makeEncryptor();
        this.username = authenticateUser();
    }

    private String authenticateUser() throws IOException {
        while (true) {
            String encryptedUsername = socketReader.readLine();
            String decryptedUsername = decryptor.decryptString(encryptedUsername);
            String encryptedPassword = socketReader.readLine();
            String decryptedPassword = decryptor.decryptString(encryptedPassword);

            if (NICKNAME_RULES.matcher(decryptedUsername).matches()) {
                if (!clients.containsKey(decryptedUsername)) {
                    if (authenticator.userIsRegistered(decryptedUsername)) {
                        if (authenticator.authenticate(decryptedUsername, decryptedPassword)) {
                            sendEncrypeted(LoginResponse.LOGIN_ACCPETED.name());
                            return decryptedUsername;
                        } else {
                            sendEncrypeted(LoginResponse.PASSWORD_INVALID.name());
                        }
                    } else {
                        authenticator.registerUser(decryptedUsername, decryptedPassword);
                        sendEncrypeted(LoginResponse.REGISTERED.name());
                        return decryptedUsername;
                    }
                } else {
                    sendEncrypeted(LoginResponse.ALREADY_LOGGED_IN.name());
                }
            } else {
                sendEncrypeted(LoginResponse.INVALID_USERNAME.name());
            }
        }

    }

    @Override
    public void run() {
        broadcast(username + " has joined this chatting room");
        try {
            while (true) {
                String clientData = socketReader.readLine();
                if (clientData == null) {
                    disconnect();
                    break;
                }

                String message = decryptor.decryptString(clientData);
                Matcher privateMessageMatcher = PRIVATE_MESSAGE_NICKNAME_PATTERN.matcher(message);
                if (message.equals(":clients")) {
                    sendClientList();
                } else if (message.startsWith(":changePassword")) {
                    authenticator.changePassword(username, message.substring(":changePassword ".length()));
                    sendEncrypeted("PASSWORD CHANGED");
                } else if (privateMessageMatcher.matches()) {
                    sendPrivateMessage(privateMessageMatcher);
                } else {
                    broadcast(username + ": " + message);
                }
            }
        } catch (IOException e) {
            disconnect();
        }
    }

    private void disconnect() {
        clients.remove(username);
        broadcast(username + " has disconnected this chatting room");
    }

    private void broadcast(String message) {
        for (ClientConnection cC : clients.values()) {
            if (cC != this) {
                cC.sendEncrypeted(message);
            }
        }
    }

    private void send(String data) {
        try {
            socketWriter.println(data);
            socketWriter.flush();
        } catch (Exception e) {
            disconnect();
        }
    }

    private void sendEncrypeted(String message) {
        send(encryptor.encryptString(message));
    }

    private void sendClientList() {
        for (String nickname : clients.keySet()) {
            sendEncrypeted("\t" + nickname);
        }
    }

    private void sendPrivateMessage(Matcher privateMessageMatcher) {
        String to = privateMessageMatcher.group(1);
        String messageText = privateMessageMatcher.group(2);
        if (clients.keySet().contains(to)) {
            clients.get(to).sendEncrypeted("PRIVATE " + username + ": " + messageText);
        } else {
            sendEncrypeted("SERVER: WRONG NICKNAME");
        }
    }

    private void sendEncryptionKeys() {
        send(decryptor.getE().toString());
        send(decryptor.getN().toString());
    }

    private RSA makeEncryptor() throws IOException {
        BigInteger e = new BigInteger(socketReader.readLine());
        BigInteger n = new BigInteger(socketReader.readLine());
        return new RSA(e, n);
    }

}