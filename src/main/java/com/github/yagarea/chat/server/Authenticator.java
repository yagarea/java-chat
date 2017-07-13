package com.github.yagarea.chat.server;

import com.github.yagarea.chat.shared.security.ShaUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authenticator {


    private Pattern login = Pattern.compile("(\\w+):(.+)");
    private Map<String, String> users;

    private String file;




    public Authenticator(String file) {
        this.file = file;
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            this.users = new HashMap<>();
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
        return users.get(username) != null;
    }

    public boolean authenticate(String descryptedUsername, String descryptedPassword) {
        String hashPassword = ShaUtil.hash(descryptedPassword);
        return hashPassword.equals(users.get(descryptedUsername));
    }

    public void registerUser(String newUsername, String newPassword) {
        try {
            PrintWriter fileWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            String hashedPassword = ShaUtil.hash(newPassword);
            fileWriter.println(newUsername + ":" + hashedPassword);
            fileWriter.flush();
            users.put(newUsername, hashedPassword);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(String username, String newPassword) {
        try {
            PrintWriter fileWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            users.put(username, ShaUtil.hash(newPassword));
            for (String userNameInSet : users.keySet()) {
                fileWriter.println(userNameInSet + ":" + users.get(userNameInSet));
            }
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}