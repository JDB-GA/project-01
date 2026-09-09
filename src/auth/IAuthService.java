package auth;

import java.util.List;

public interface IAuthService {
    String login(String username, String password);

    String register(String username, String password, String role);

    boolean checkRole(String id, String requiredRole);

    boolean resetPassword(String userId, String newPassword);

    List<String[]> getUsers();

    String[] getUserById(String id);

    String[] getUserByUsername(String id);

    boolean checkExists(String identifier);

    boolean authState(String userId);

    void createAuthTracker(String userId);

    void countFailedAttempts(String userId);

    String[] getAuthTrackerByUserId(String userId);

    boolean logout(String userId);
}
