package customer;

import transaction.TransactionService;
import general.Constants;

public class CustomerService extends TransactionService {

    @Override
    public String deposit(String userId, String accountId, double amount) {
        return "";
    }

    @Override
    public String withdraw(String userId, String accountId, double amount) {
        return "";
    }

    @Override
    public String transfer(String userId, String fromAccountId, String toAccountId, double amount) {
        return "";
    }
}
