import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Set;

public class ClientConnection implements Runnable {
    private PrintWriter socketWriter;
    private Set<ClientConnection> clients;
    private RSA decryptor;
    private RSA encryptor;
    private BufferedReader socketReader;
    private String username;

    ClientConnection(Socket clientSocket, Set<ClientConnection> clients, RSA decryptor) {
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
            this.username = decryptor.decryptString(socketReader.readLine());
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
                message = username + " :  " + message;
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

    private void send(String message) {
        try {
            socketWriter.println(message);
            socketWriter.flush();
        } catch (Exception e) {
            clients.remove(this);
            System.err.println("Client disconnected");
        }
    }
}