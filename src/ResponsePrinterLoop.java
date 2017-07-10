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


    public ResponsePrinterLoop(Socket input) throws IOException {
        this.clientSocket = input;
        this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println(socketReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine() throws IOException {
        return socketReader.readLine();
    }
}
