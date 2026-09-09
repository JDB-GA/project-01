package banker;

import java.util.List;

public interface IBankerService {
    String addCustomer(String username, String password, String accountType);
    String createAccount(String customerId, String accountType);
    boolean checkAccountExists(String userId, String accountType);
    List<String[]> getAccounts();
}
