package auth;

import general.AppLogger;
import general.Constants;
import general.FileControl;
import general.Functions;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;


public class Auth implements IAuth {

    @Override
    public String login(String username, String password) {
        String[] user = getUserByUsername(username.toLowerCase());
        if (user.length == 0) {

            return "Incorrect Credentials";
        }

        String[] authTracker = getAuthTrackerByUserId(user[0]);

        if (isLoginLocked(authTracker)) {

            return "Too many failed attempts. Try again later";
        }

        if (authState(user[0])) {

            return "User already logged in";
        }

        if (!BCrypt.checkpw(password, user[2])) {
            countFailedAttempts(user[0]);

            return "Incorrect Credentials";
        }

        if (authTracker.length == 0) {
            createSession(user[0]);
        } else {
            Functions.updateTableField(Constants.AUTH_TRACKER_TABLE, Constants.AUTH_TRACKER_TABLE_HEADER, 1, user[0], 2, "true");
            Functions.updateTableField(Constants.AUTH_TRACKER_TABLE, Constants.AUTH_TRACKER_TABLE_HEADER, 1, user[0], 3, "0");
            Functions.updateTableField(Constants.AUTH_TRACKER_TABLE, Constants.AUTH_TRACKER_TABLE_HEADER, 1, user[0], 4, "");
            Functions.updateTableField(Constants.AUTH_TRACKER_TABLE, Constants.AUTH_TRACKER_TABLE_HEADER, 1, user[0], 5, Functions.getNow());
        }

        return "Login successful";
    }

    @Override
    public void createSession(String userId) {
        String authTrackerId = Functions.generateUUID();
        String now = Functions.getNow();
        String row = String.join(",", authTrackerId, userId, "true", "0", "", now);
        Functions.addRow(Constants.AUTH_TRACKER_TABLE, List.of(row));
    }

    @Override
    public String register(String username, String password, String role) {
        username = username.toLowerCase();

        try {
            if (checkExists(username)) {

                return "The account already exists!";
            }

            String id = Functions.generateUUID();
            String hashedPassword = hashPassword(password);
            String now = Functions.getNow();
            String row = id + "," + username + "," + hashedPassword + "," + role + "," + now;
            FileControl.append(Constants.USER_TABLE, List.of(row));

            return id;
        } catch (Exception e) {
            AppLogger.error("An error occurred during user registration", e);

            return "An error occurred during user registration";
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
                .filter(user -> user[1].equals(username.toLowerCase()))
                .findFirst().orElse(new String[]{});
    }

    @Override
    public boolean logout(String userId) {
        Functions.updateTableField(
                Constants.AUTH_TRACKER_TABLE,
                Constants.AUTH_TRACKER_TABLE_HEADER,
                1,
                userId,
                2,
                "false"
        );

        return Functions.updateTableField(
                Constants.AUTH_TRACKER_TABLE,
                Constants.AUTH_TRACKER_TABLE_HEADER,
                1,
                userId,
                5,
                Functions.getNow()
        );
    }

    @Override
    public boolean checkRole(String id, String requiredRole) {
        String[] user = getUserById(id);

        return user.length > 0 && user[3].equals(requiredRole);
    }

    private String hashPassword(String newPassword) {

        return BCrypt.hashpw(newPassword, BCrypt.gensalt());
    }

    @Override
    public boolean resetPassword(String userId, String newPassword) {
        String hashedPassword = hashPassword(newPassword);

        return Functions.updateTableField(Constants.USER_TABLE, Constants.USER_TABLE_HEADER, 0, userId, 2, hashedPassword);
    }

    @Override
    public boolean authState(String userId) {

        String[] authTracker = getAuthTrackerByUserId(userId);

        return authTracker.length > 0 && Boolean.parseBoolean(authTracker[2]);
    }

    @Override
    public int countFailedAttempts(String userId) {
        String[] authTracker = getAuthTrackerByUserId(userId);
        int failedAttempts = authTracker.length == 0 ? 1 : Integer.parseInt(authTracker[3]) + 1;
        String lockedUntil = failedAttempts >= 3 ? LocalDateTime.now().plusSeconds(30).toString() : "";

        if (authTracker.length == 0) {
            String row = String.join(",", Functions.generateUUID(), userId, "false", String.valueOf(failedAttempts), lockedUntil, Functions.getNow());
            Functions.addRow(Constants.AUTH_TRACKER_TABLE, List.of(row));
        } else {
            Functions.updateTableField(Constants.AUTH_TRACKER_TABLE, Constants.AUTH_TRACKER_TABLE_HEADER, 1, userId, 2, "false");
            Functions.updateTableField(Constants.AUTH_TRACKER_TABLE, Constants.AUTH_TRACKER_TABLE_HEADER, 1, userId, 3, String.valueOf(failedAttempts));
            Functions.updateTableField(Constants.AUTH_TRACKER_TABLE, Constants.AUTH_TRACKER_TABLE_HEADER, 1, userId, 4, lockedUntil);
            Functions.updateTableField(Constants.AUTH_TRACKER_TABLE, Constants.AUTH_TRACKER_TABLE_HEADER, 1, userId, 5, Functions.getNow());
        }

        return failedAttempts;
    }

    @Override
    public String[] getAuthTrackerByUserId(String userId) {

        List<String[]> authTrackers = Functions.getTable(Constants.AUTH_TRACKER_TABLE);

        if (authTrackers == null) {

            return new String[]{};
        }

        return authTrackers.stream()
                .filter(authTracker -> authTracker[1].equals(userId))
                .findFirst()
                .orElse(new String[]{});
    }

    private boolean isLoginLocked(String[] authTracker) {

        return authTracker.length > 0
                && !authTracker[4].isEmpty()
                && LocalDateTime.now().isBefore(LocalDateTime.parse(authTracker[4]));
    }

    @Override
    public List<String[]> getUsers() {

        return Functions.getTable(Constants.USER_TABLE);
    }

    @Override
    public String[] getUserById(String id) {

        return getUsers().stream()
                .filter(user -> user[0].equals(id))
                .findFirst()
                .orElse(new String[]{});
    }
}
