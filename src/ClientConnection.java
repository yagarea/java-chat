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

    protected String username;
    private PrintWriter socketWriter;
    private Map<String, ClientConnection> clients;
    private RSA decryptor;
    private RSA encryptor;
    private BufferedReader socketReader;

    ClientConnection(Socket clientSocket, Map<String, ClientConnection> clients, RSA decryptor) {
        this.clients = clients;
        this.decryptor = decryptor;
        try {
            this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
            send(decryptor.getE().toString());
            send(decryptor.getN().toString());
            BigInteger e = new BigInteger(socketReader.readLine());
            BigInteger n = new BigInteger(socketReader.readLine());
            encryptor = new RSA(e, n);
            while (true) {
                String encryptedUsername = socketReader.readLine();
                String decryptedUsername = decryptor.decryptString(encryptedUsername);
                if (NICKNAME_RULES.matcher(decryptedUsername).matches() && !clients.containsKey(decryptedUsername)) {
                    this.username = decryptedUsername;
                    send(encryptor.encryptString("USERNAME ACCEPTED"));
                    break;
                } else {
                    send(encryptor.encryptString("WRONG USERNAME"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            while (true) {
                String clientData = socketReader.readLine();
                if (clientData == null) {
                    break;
                }

                String message = decryptor.decryptString(clientData);
                Matcher privateMessageMatcher = PRIVATE_MESSAGE_NICKNAME_PATTERN.matcher(message);
                if (message.equals(":clients")) {
                    sendClientList();
                } else if (privateMessageMatcher.matches()) {
                    sendPrivateMessage(privateMessageMatcher);
                } else {
                    sendTextMessage(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendTextMessage(String message) {
        message = username + " :  " + message;
        for (ClientConnection cC : clients.values()) {
            if (cC != this) {
                cC.send(cC.encryptor.encryptString(message));
            }
        }
    }

    private void send(String message) {
        try {
            socketWriter.println(message);
            socketWriter.flush();
        } catch (Exception e) {
            clients.remove(this);
            System.err.println("Client disconnected");
        }
    }

    private void sendClientList() {
        for (String nickname : clients.keySet()) {
            send(encryptor.encryptString(nickname));
        }
    }

    private void sendPrivateMessage(Matcher privateMessageMatcher) {
        String nickname = privateMessageMatcher.group(1);
        String messageText = privateMessageMatcher.group(2);
        if (clients.keySet().contains(nickname)) {
            clients.get(nickname).send(clients.get(nickname).encryptor.encryptString("PRIVATE " + nickname + ": " + messageText));
        } else {
            send(encryptor.encryptString("SERVER: WRONG NICKNAME"));
        }
    }
}