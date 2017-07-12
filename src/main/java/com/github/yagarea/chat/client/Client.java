package com.github.yagarea.chat.client;

import com.github.yagarea.chat.shared.LoginResponse;
import com.github.yagarea.chat.shared.security.RSA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket(args[0], Integer.parseInt(args[1]));

            RSA decryptor = new RSA();
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            writer.println(decryptor.getE());
            writer.println(decryptor.getN());
            writer.flush();

            ResponsePrinterLoop responsePrinterLoop = new ResponsePrinterLoop(clientSocket, decryptor);
            BigInteger e = new BigInteger(responsePrinterLoop.readLine());
            BigInteger n = new BigInteger(responsePrinterLoop.readLine());
            RSA encryptor = new RSA(e, n);
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            LoginResponse usernameResponse;
            do {
                System.out.print("Nickname: ");
                String username = consoleReader.readLine();
                System.out.print("Password: ");
                String password = consoleReader.readLine();
                writer.println(encryptor.encryptString(username));
                writer.println(encryptor.encryptString(password));
                writer.flush();
                usernameResponse = LoginResponse.valueOf(decryptor.decryptString(responsePrinterLoop.readLine()));
                System.out.println(usernameResponse.name());
            } while (usernameResponse != LoginResponse.LOGIN_ACCPETED && usernameResponse != LoginResponse.REGISTERED);
            new Thread(responsePrinterLoop).start();
            while (true) {
                String messageToServer = consoleReader.readLine();
                if (!messageToServer.equals("")) {
                    writer.println(encryptor.encryptString(messageToServer));
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}