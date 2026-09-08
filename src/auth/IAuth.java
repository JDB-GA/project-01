package auth;

import java.util.List;

public interface IAuth {
    boolean login(String username, String password);
    boolean register(String username, String password, String role);
    String checkRole(String role);
    String hashPassword(String password);
    List<String[]> getUsers();
    String[] getUserById(String id);
    String[] getUserByUsername(String id);
    boolean checkExists(String identifier);
}
