import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static void main(String[] args) {
        RSA encryptor = new RSA();
        final int PORT = 4077;

        ServerSocket chatServer = null;
        try {
            chatServer = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, ClientConnection> clients = new HashMap<>();
        Authenticator auth = new Authenticator();


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