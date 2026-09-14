import app.App;
import app.Dependencies;
import auth.AuthService;
import banker.BankerService;
import card.CardService;
import card.CardLimitService;
import customer.CustomerService;
import repositories.*;

public class Main {

    public static void main(String[] args) {

        App app = new App(getDependencies());

        app.start();
    }


    private static Dependencies getDependencies() {
        UserRepository userRepository = new UserRepository();
        AuthTrackerRepository authTrackerRepository = new AuthTrackerRepository();
        AuthService authService = new AuthService(userRepository, authTrackerRepository);
        TransactionRepository transactionRepository = new TransactionRepository();
        AccountRepository accountRepository = new AccountRepository();
        CardRepository cardRepository = new CardRepository();
        CardService cardService = new CardService(cardRepository, accountRepository);
        CardLimitService cardLimitService = new CardLimitService(cardService, transactionRepository);
        BankerService bankerService = new BankerService(accountRepository, authService, transactionRepository, cardService, cardLimitService);
        CustomerService customerService = new CustomerService(accountRepository, transactionRepository, authService, cardLimitService);

        return new Dependencies(authService,
                userRepository,
                authTrackerRepository,
                bankerService,
                accountRepository,
                customerService,
                cardService,
                cardRepository
        );
    }

}
