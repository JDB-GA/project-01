package customer;

import auth.AuthService;
import repositories.AccountRepository;
import repositories.TransactionRepository;
import transaction.TransactionService;

public class CustomerService extends TransactionService {

    public CustomerService(AccountRepository accountRepository, TransactionRepository transactionRepository, AuthService authService) {
        super(accountRepository, transactionRepository, authService);
    }
}
