package customer;

import auth.AuthService;
import card.CardLimitService;
import repositories.AccountRepository;
import repositories.TransactionRepository;
import transaction.TransactionService;

public class CustomerService extends TransactionService {

    public CustomerService(AccountRepository accountRepository, TransactionRepository transactionRepository, AuthService authService, CardLimitService cardLimitService) {
        super(accountRepository, transactionRepository, authService, cardLimitService);
    }
}
