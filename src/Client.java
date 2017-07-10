import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 4077);
            Thread listner = new Thread(new ResponsePrinterLoop(clientSocket));
            listner.start();
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                writer.println(consoleReader.readLine());
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
