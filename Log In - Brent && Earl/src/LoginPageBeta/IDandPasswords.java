package LoginPageBeta;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class IDandPasswords {

    private HashMap<String, User> logininfo = new HashMap<>();

    public IDandPasswords() {
        loadUsersFromTextFile("users.txt");
    }

    public HashMap<String, User> getLoginInfo() {
        return logininfo;
    }

    private void loadUsersFromTextFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3) {
                    String username = userData[0].trim();
                    String password = userData[1].trim();
                    String position = userData[2].trim();
                    logininfo.put(username, new User(username, password, position));
                } else {
                    System.err.println("Invalid line in users.txt: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
