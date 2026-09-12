import app.App;
import app.Dependencies;
import auth.AuthService;
import banker.BankerService;
import repositories.AccountRepository;
import repositories.AuthTrackerRepository;
import repositories.TransactionRepository;
import repositories.UserRepository;

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
        BankerService bankerService = new BankerService(accountRepository, authService, transactionRepository);

        return new Dependencies(authService, userRepository, authTrackerRepository, bankerService, accountRepository);
    }

}
