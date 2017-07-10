import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by John on 09.07.17.
 */
public class ResponsePrinterLoop implements Runnable {
    Socket clientSocket;

    @Override
    public void run() {
        try {
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (true) {
                System.out.println(socketReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResponsePrinterLoop(Socket input) {
        this.clientSocket = input;
    }
}
