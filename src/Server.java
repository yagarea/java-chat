import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static void main(String[] args) {
        RSA encryptor = new RSA();
        final int PORT = 4077;

        try {
            ServerSocket chatServer = new ServerSocket(PORT);
            Map<String, ClientConnection> clients = new HashMap<>();
            while (true) {
                Socket clientSocket = chatServer.accept();
                ClientConnection newClient = new ClientConnection(clientSocket, clients, encryptor);

                new Thread(newClient).start();
                clients.put(newClient.username, newClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
