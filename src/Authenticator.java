import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authenticator {

    private static final String FILE = "Authentication.txt";
    private Pattern login = Pattern.compile("(\\w+):(.+)");
    private Map<String, String> users;


    public Authenticator() {
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE)));
            users = new HashMap<>();
            while (true) {
                String line = fileReader.readLine();
                if (line != null) {
                    Matcher lineMatcher = login.matcher(line);
                    lineMatcher.find();
                    users.put(lineMatcher.group(1), lineMatcher.group(2));
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean authenticate(String descryptedUsername, String descryptedPassword) {
        return descryptedPassword.equals(users.get(descryptedUsername));
    }
}
