package auth;

import general.AppLogger;
import general.Constants;
import general.FileControl;
import general.Functions;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;


public class Auth implements IAuth {

    @Override
    public boolean login(String username, String password) {
        String[] user = getUserByUsername(username);
        if (user.length == 0) {

            return false;
        }

        return BCrypt.checkpw(password, user[2]);
    }

    @Override
    public List<String[]> getUsers() {
        try {

            return FileControl.read(Constants.USER_TABLE)
                    .stream()
                    .skip(1)
                    .map(row -> row.split(","))
                    .toList();

        } catch (Exception e) {
            AppLogger.error("An error occurred while reading users", e);

            return List.of();
        }
    }

    @Override
    public String[] getUserById(String id) {

        return getUsers().stream()
                .filter(user -> user[0].equals(id))
                .findFirst()
                .orElse(new String[]{});
    }

    @Override
    public boolean register(String username, String password, String role) {
        try {
            if (checkExists(username)) {

                return false;
            }

            String id = Functions.generateUUID();
            String hashedPassword = hashPassword(password);
            String now = Functions.getNow();
            String row = id + "," + username + "," + hashedPassword + "," + role + "," + now;
            FileControl.append(Constants.USER_TABLE, List.of(row));

            return true;
        } catch (Exception e) {
            AppLogger.error("An error occurred during user registration", e);

            return false;
        }
    }

    @Override
    public boolean checkExists(String identifier) {

        return getUserByUsername(identifier).length > 0 || getUserById(identifier).length > 0;
    }

    @Override
    public String[] getUserByUsername(String username) {

        return getUsers()
                .stream()
                .filter(user -> user[1].equals(username))
                .findFirst().orElse(new String[]{});
    }

    @Override
    public String checkRole(String id) {
        String[] user = getUserById(id);

        return user.length > 0 ? user[3] : "Not Found";
    }

    @Override
    public String hashPassword(String password) {

        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


}
