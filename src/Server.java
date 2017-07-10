import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {

    public static void main(String[] args) {
        RSA encryptor = new RSA();
        final int PORT = 4077;

        try {
            ServerSocket chatServer = new ServerSocket(PORT);
            Set<ClientConnection> clients = new HashSet<>();
            while (true) {
                Socket clientSocket = chatServer.accept();
                ClientConnection newClient = new ClientConnection(clientSocket, clients, encryptor);
                newClient.send(encryptor.getE().toString());
                newClient.send(encryptor.getN().toString());
                new Thread(newClient).start();
                clients.add(newClient);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
