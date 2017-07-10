import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 4077);
            ResponsePrinterLoop responsePrinterLoop = new ResponsePrinterLoop(clientSocket);
            Thread listner = new Thread(responsePrinterLoop);
            BigInteger e = new BigInteger(responsePrinterLoop.readLine());
            BigInteger n = new BigInteger(responsePrinterLoop.readLine());
            RSA encryptor = new RSA(e, n);
            listner.start();
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String messageToServer = consoleReader.readLine();
                writer.println(encryptor.encryptString(messageToServer));
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
