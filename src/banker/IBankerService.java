package banker;

import general.Constants;

import java.util.List;

public interface IBankerService {
    String addCustomer(String actorId, String username, String password, String accountType, Constants.CardType cardType);

    String createAccount(String actorId, String customerId, String accountType, Constants.CardType cardType);

    boolean checkAccountExists(String userId, String accountType);

}
