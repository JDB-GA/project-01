package banker;

import java.util.List;

public interface IBankerService {
    String addCustomer(String actorId, String username, String password, String accountType);

    String createAccount(String actorId, String customerId, String accountType);

    boolean checkAccountExists(String userId, String accountType);
}
