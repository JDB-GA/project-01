package repositories;

import general.Constants;
import general.Functions;

import java.util.List;

public class UserRepository {

    public List<String[]> findAll() {
        return Functions.getTable(Constants.USER_TABLE);
    }

    public String[] findById(String id) {
        return findAll()
                .stream()
                .filter(user -> user[0].equals(id))
                .findFirst()
                .orElse(new String[]{});
    }

    public String[] findByUsername(String username) {
        return findAll()
                .stream()
                .filter(user -> user[1].equals(username.toLowerCase()))
                .findFirst()
                .orElse(new String[]{});
    }

    public boolean exists(String identifier) {
        return findByUsername(identifier).length > 0 || findById(identifier).length > 0;
    }

    public void save(String id, String username, String hashedPassword, String role, String createdAt) {
        String row = String.join(
                Constants.CSV_SEPARATOR,
                id,
                username,
                hashedPassword,
                role,
                createdAt
        );
        Functions.addRow(Constants.USER_TABLE, List.of(row));
    }

    public boolean updatePassword(String userId, String hashedPassword) {
        return Functions.updateTableField(
                Constants.USER_TABLE,
                Constants.USER_TABLE_HEADER,
                0,
                userId,
                2,
                hashedPassword
        );
    }
}
