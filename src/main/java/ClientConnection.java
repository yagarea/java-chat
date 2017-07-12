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

    ClientConnection(Socket clientSocket, Map<String, ClientConnection> clients, RSA decryptor, Authenticator authenticator) throws IOException {
        this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
        this.clients = clients;
        this.decryptor = decryptor;

        sendEncryptionKeys();

        encryptor = makeEncryptor();
        this.username = authenticateUser(authenticator);
    }

    private String authenticateUser(Authenticator authenticator) throws IOException {
        while (true) {
            String encryptedUsername = socketReader.readLine();
            String decryptedUsername = decryptor.decryptString(encryptedUsername);
            String encryptedPassword = socketReader.readLine();
            String decryptedPassword = decryptor.decryptString(encryptedPassword);
            if (NICKNAME_RULES.matcher(decryptedUsername).matches() &&
                    !clients.containsKey(decryptedUsername) &&
                    authenticator.authenticate(decryptedUsername, decryptedPassword)) {

                send(encryptor.encryptString("LOGIN ACCEPTED"));
                return decryptedUsername;
            } else {
                send(encryptor.encryptString("WRONG LOGIN"));
            }
        }
    }

    @Override
    public void run() {
        try {

            while (true) {
                String clientData = socketReader.readLine();
                if (clientData == null) {
                    clients.remove(username);
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
            send(encryptor.encryptString("\t" + nickname));
        }
    }

    private void sendPrivateMessage(Matcher privateMessageMatcher) {
        String to = privateMessageMatcher.group(1);
        String messageText = privateMessageMatcher.group(2);
        if (clients.keySet().contains(to)) {
            clients.get(to).send(clients.get(to).encryptor.encryptString("PRIVATE " + username + ": " + messageText));
        } else {
            send(encryptor.encryptString("SERVER: WRONG NICKNAME"));
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