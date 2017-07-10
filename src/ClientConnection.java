import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

public class ClientConnection implements Runnable {
    private Socket clientSocket;
    private PrintWriter socketWriter;
    private Set<ClientConnection> clients;
    private RSA decryptor;

    public ClientConnection(Socket clientSocket, Set clients, RSA decryptor) {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.decryptor = decryptor;
        try {
            this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (true) {
                String clientData = socketReader.readLine();
                if (clientData == null) {
                    break;
                }
                String message = decryptor.decryptString(clientData);

                for (ClientConnection cC : clients) {
                    if (cC != this) {
                        cC.send(message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        try {
            socketWriter.println(message);
            socketWriter.flush();
        } catch (Exception e) {
            clients.remove(this);
            System.err.println("Client disconnected");
        }
    }
}