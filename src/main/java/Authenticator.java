import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authenticator {


    private Pattern login = Pattern.compile("(\\w+):(.+)");
    private Map<String, String> users;
    private PrintWriter fileWriter;


    public Authenticator(String file) {
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            fileWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            users = new HashMap<>();
            while (true) {
                String line = fileReader.readLine();
                if (line != null && !line.equals("")) {
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

    public boolean userIsRegistered(String username) {
        return users.containsKey(username);
    }

    public boolean authenticate(String descryptedUsername, String descryptedPassword) {
        String hashPassword = ShaUtil.hash(descryptedPassword);
        return hashPassword.equals(users.get(descryptedUsername));
    }

    public void registerUser(String newUsername, String newPassword) {
        String hashedPassword = ShaUtil.hash(newPassword);
        fileWriter.println(newUsername + ":" + hashedPassword);
        fileWriter.flush();
        users.put(newUsername, hashedPassword);
    }
}
