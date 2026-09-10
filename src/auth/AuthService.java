package auth;

import general.AppLogger;
import general.Constants;
import general.Functions;
import org.mindrot.jbcrypt.BCrypt;
import repositories.AuthTrackerRepository;
import repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;


public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final AuthTrackerRepository authTrackerRepository;

    public AuthService(UserRepository userRepository, AuthTrackerRepository authTrackerRepository) {
        this.userRepository = userRepository;
        this.authTrackerRepository = authTrackerRepository;
    }

    @Override
    public String login(String username, String password) {
        String[] user = getUserByUsername(username.toLowerCase());
        if (user.length == 0) {

            return Constants.INCORRECT_CREDENTIALS;
        }

        String[] authTracker = getAuthTrackerByUserId(user[0]);

        if (isLoginLocked(authTracker)) {

            return Constants.TOO_MANY_FAILED_ATTEMPTS;
        }

        if (authState(user[0])) {

            return Constants.USER_ALREADY_LOGGED_IN;
        }

        if (!BCrypt.checkpw(password, user[2])) {
            countFailedAttempts(user[0]);

            return Constants.INCORRECT_CREDENTIALS;
        }

        if (authTracker.length == 0) {
            createAuthTracker(user[0]);
        } else {
            authTrackerRepository.markLoggedIn(user[0]);
        }

        return Constants.LOGIN_SUCCESSFUL;
    }

    @Override
    public void createAuthTracker(String userId) {
        authTrackerRepository.create(userId);
    }

    @Override
    public String register(String username, String password, String role) {
        username = username.toLowerCase();

        try {
            if (checkExists(username)) {

                return Constants.ACCOUNT_ALREADY_EXISTS;
            }

            String id = Functions.generateUUID();
            String hashedPassword = hashPassword(password);
            String now = Functions.getNow();
            userRepository.save(id, username, hashedPassword, role, now);

            return id;
        } catch (Exception e) {
            AppLogger.error(Constants.REGISTRATION_ERROR, e);

            return Constants.REGISTRATION_ERROR;
        }
    }

    @Override
    public boolean checkExists(String identifier) {

        return userRepository.exists(identifier);
    }

    @Override
    public String[] getUserByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    @Override
    public boolean logout(String userId) {
        return authTrackerRepository.markLoggedOut(userId);
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

        return userRepository.updatePassword(userId, hashedPassword);
    }

    @Override
    public boolean authState(String userId) {

        String[] authTracker = getAuthTrackerByUserId(userId);

        return authTracker.length > 0 && Boolean.parseBoolean(authTracker[2]);
    }

    @Override
    public void countFailedAttempts(String userId) {
        String[] authTracker = getAuthTrackerByUserId(userId);
        int failedAttempts = authTracker.length == 0 ? 1 : Integer.parseInt(authTracker[3]) + 1;
        String lockedUntil = failedAttempts >= 3 ? LocalDateTime.now().plusSeconds(30).toString() : Constants.EMPTY_STRING;

        if (authTracker.length == 0) {
            authTrackerRepository.createFailedAttempt(userId, failedAttempts, lockedUntil);
        } else {
            if (failedAttempts >= 3) {
                failedAttempts = 0;
            }
            authTrackerRepository.updateFailedAttempt(userId, failedAttempts, lockedUntil);
        }

    }

    @Override
    public String[] getAuthTrackerByUserId(String userId) {

        return authTrackerRepository.findByUserId(userId);
    }

    private boolean isLoginLocked(String[] authTracker) {

        return authTracker.length > 0
                && !authTracker[4].isEmpty()
                && LocalDateTime.now().isBefore(LocalDateTime.parse(authTracker[4]));
    }

    @Override
    public List<String[]> getUsers() {

        return userRepository.findAll();
    }

    @Override
    public String[] getUserById(String id) {

        return userRepository.findById(id);
    }
}
