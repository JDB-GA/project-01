package banker;

import transaction.TransactionService;
import auth.AuthService;
import general.AppLogger;
import general.Constants;
import general.Functions;
import repositories.AccountRepository;

import java.util.List;

public class BankerService extends TransactionService implements IBankerService {

    private final AccountRepository accountRepository = new AccountRepository();

    @Override
    public String addCustomer(String username, String password, String initialAccountType) {
        AuthService auth = new AuthService();
        String userId = auth.register(username, password, Constants.UserRole.CUSTOMER.name());
        if (userId.equals(Constants.ACCOUNT_ALREADY_EXISTS)) {

            return Constants.CUSTOMER_ALREADY_EXISTS;
        }

        return createAccount(userId, initialAccountType);
    }

    @Override
    public String createAccount(String customerId, String accountType) {
        try {
            if (checkAccountExists(customerId, accountType)) {

                return String.join(Constants.EMPTY_STRING, accountType, Constants.ACCOUNT_ALREADY_EXISTS_FOR_CUSTOMER);
            }
            String id = Functions.generateUUID();
            String now = Functions.getNow();
            String status = Constants.AccountStatus.ACTIVE.name();
            double balance = Constants.ACCOUNT_BALANCE_DEFAULT;
            int overdraftCount = Constants.OVERDRAFT_COUNT_DEFAULT;
            accountRepository.save(id, customerId, accountType, balance, status, overdraftCount, now);
        } catch (Exception e) {
            AppLogger.error("Create Account Error", e);
        }

        return String.join(Constants.EMPTY_STRING, accountType, Constants.ACCOUNT_CREATED_FOR_CUSTOMER, customerId);
    }

    @Override
    public boolean checkAccountExists(String userId, String accountType) {

        return accountRepository.existsForCustomer(userId, accountType);
    }

    @Override
    public List<String[]> getAccounts() {

        return accountRepository.findAll();
    }

    @Override
    public String withdraw() {
        return Constants.EMPTY_STRING;
    }

    @Override
    public String deposit() {
        return Constants.EMPTY_STRING;
    }

    @Override
    public String transfer(String accountOneId, String accountTwoId) {
        return Constants.EMPTY_STRING;
    }

}
