import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by John on 09.07.17.
 */
public class ResponsePrinterLoop implements Runnable {
    Socket clientSocket;
    private BufferedReader socketReader;
    private RSA decryptor;


    public ResponsePrinterLoop(Socket input, RSA decryptor) throws IOException {
        this.clientSocket = input;
        this.decryptor = decryptor;
        this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                String line = socketReader.readLine();
                if (line != null) {
                    System.out.println(decryptor.decryptString(line));
                } else {
                    System.err.println("SERVER DISCONNECTED");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine() throws IOException {
        return socketReader.readLine();
    }
}
