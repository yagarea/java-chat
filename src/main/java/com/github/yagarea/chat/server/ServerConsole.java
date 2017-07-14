package com.github.yagarea.chat.server;

import com.github.yagarea.chat.shared.UserConsoleReader;
import com.github.yagarea.chat.shared.UserReader;
import com.github.yagarea.chat.shared.UserSystemInReader;

import java.io.IOException;
import java.util.Map;

public class ServerConsole implements Runnable {
    private final UserReader consoleReader;
    private final Map<String, ClientConnection> clients;

    public ServerConsole(Map<String, ClientConnection> clients) {
        this.consoleReader = System.console() == null ? new UserSystemInReader() : new UserConsoleReader();
        this.clients = clients;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String line = consoleReader.readLine();
                if (line != null) {
                    executeCommand(consoleReader.readLine());
                } else {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void executeCommand(String command) {
        if (command.startsWith("kick ")) {
            kickUser(command.substring("kick ".length()));
        } else if (command.equals("clients")) {
            printListOfClients();
        } else if (command.startsWith("broadcast ")) {
            broadcast(command.substring("broadcast ".length()));
        } else if (command.equals("help")) {
            System.out.println("Server commands:" +
                    "\n\tkick" +
                    "\n\tbroadcast" +
                    "\n\tclients");
        } else {
            System.err.println("UNKNOWN COMMAND");
        }
    }

    private void kickUser(String username) {
        try {
            clients.get(username).sendEncrypeted("SERVER: You have been kicked.");
            clients.get(username).clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printListOfClients() {
        System.out.println("Active users(" + clients.size() + "):");
        for (String username : clients.keySet()) {
            System.out.println("\t" + username);
        }
    }

    private void broadcast(String message) {
        for (ClientConnection cC : clients.values()) {
            cC.sendEncrypeted("SERVER: " + message);
        }
    }
}