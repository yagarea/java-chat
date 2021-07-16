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

            RSA decryptor = new RSA();
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            writer.println(decryptor.getE());
            writer.println(decryptor.getN());
            writer.flush();

            ResponsePrinterLoop responsePrinterLoop = new ResponsePrinterLoop(clientSocket, decryptor);
            BigInteger e = new BigInteger(responsePrinterLoop.readLine());
            BigInteger n = new BigInteger(responsePrinterLoop.readLine());
            RSA encryptor = new RSA(e, n);
            Thread listener = new Thread(responsePrinterLoop);
            listener.start();
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Username: ");
            String username = consoleReader.readLine();
            writer.println(encryptor.encryptString(username));
            writer.flush();
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
