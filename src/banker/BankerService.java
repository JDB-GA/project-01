package banker;

import repositories.TransactionRepository;
import transaction.TransactionService;
import auth.AuthService;
import general.AppLogger;
import general.Constants;
import general.Functions;
import repositories.AccountRepository;

public class BankerService extends TransactionService implements IBankerService {

    private final AuthService authService;

    public BankerService(AccountRepository accountRepository, AuthService authService, TransactionRepository transactionRepository) {
        super(accountRepository, transactionRepository, authService);
        this.authService = authService;
    }

    @Override
    public String addCustomer(String actorId, String username, String password, String initialAccountType) {

        if (!authService.checkRole(actorId, Constants.UserRole.BANKER.name())) {
            return Constants.GENERAL_ERROR;
        }

        String userId = authService.register(username, password, Constants.UserRole.CUSTOMER.name());
        if (userId.equals(Constants.ACCOUNT_ALREADY_EXISTS)) {

            return Constants.CUSTOMER_ALREADY_EXISTS;
        }

        return createAccount(actorId, userId, initialAccountType);
    }

    @Override
    public String createAccount(String actorId, String customerId, String accountType) {
        if (!authService.checkRole(actorId, Constants.UserRole.BANKER.name())) {
            return Constants.GENERAL_ERROR;
        }

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
            AppLogger.error(Constants.GENERAL_ERROR, e);
        }

        return String.join(Constants.EMPTY_STRING, accountType, Constants.ACCOUNT_CREATED_FOR_CUSTOMER, customerId);
    }

    @Override
    public boolean checkAccountExists(String userId, String accountType) {

        return accountRepository.existsForCustomer(userId, accountType);
    }

}
