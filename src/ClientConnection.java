import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Set;

public class ClientConnection implements Runnable {
    private Socket clientSocket;
    private PrintWriter socketWriter;
    private Set<ClientConnection> clients;
    private RSA decryptor;
    private RSA encryptor;
    private BufferedReader socketReader;


    public ClientConnection(Socket clientSocket, Set clients, RSA decryptor) {
        this.clientSocket = clientSocket;
        this.clients = clients;
        this.decryptor = decryptor;
        try {
            this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.socketWriter = new PrintWriter(clientSocket.getOutputStream());
            BigInteger e = new BigInteger(socketReader.readLine());
            BigInteger n = new BigInteger(socketReader.readLine());
            encryptor = new RSA(e, n);
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

                for (ClientConnection cC : clients) {
                    if (cC != this) {
                        cC.send(cC.encryptor.encryptString(message));
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