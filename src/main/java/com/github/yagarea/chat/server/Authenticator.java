package com.github.yagarea.chat.server;

import com.github.yagarea.chat.shared.security.ShaUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Authenticator {


    private Pattern login = Pattern.compile("(\\w+):(\\d+):(.+)");
    private Map<String, PasswordHolder> users;

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
                    users.put(lineMatcher.group(1), new PasswordHolder(lineMatcher.group(2), lineMatcher.group(3)));
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
        String hashPassword = ShaUtil.hash(users.get(descryptedUsername).getSalt() + descryptedPassword);
        return hashPassword.equals(users.get(descryptedUsername).getHash());
    }

    public void registerUser(String newUsername, String newPassword) {
        try {
            PrintWriter fileWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            PasswordHolder newPasswordHolder = new PasswordHolder(newPassword);
            fileWriter.println(newUsername + ":" + newPasswordHolder.getSalt() + ":" + newPasswordHolder.getHash());
            fileWriter.flush();
            users.put(newUsername, newPasswordHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changePassword(String username, String newPassword) {
        try {
            PrintWriter fileWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
            PasswordHolder newPasswordHolder = new PasswordHolder(newPassword);
            users.put(username, newPasswordHolder);
            for (String userNameInSet : users.keySet()) {
                fileWriter.println(userNameInSet + ":" + users.get(userNameInSet).getSalt() + ":" + users.get(userNameInSet).getHash());
            }
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}