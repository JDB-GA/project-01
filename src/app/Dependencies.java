package app;

import auth.AuthService;
import banker.BankerService;
import repositories.AccountRepository;
import repositories.AuthTrackerRepository;
import repositories.UserRepository;

public class Dependencies {
    UserRepository userRepository;
    AuthTrackerRepository authTrackerRepository;
    AuthService authService;
    BankerService bankerService;
    AccountRepository accountRepository;

    public Dependencies
            (AuthService authService,
             UserRepository userRepository,
             AuthTrackerRepository authTrackerRepository,
             BankerService bankerService,
             AccountRepository accountRepository
            ) {
        this.userRepository = userRepository;
        this.authTrackerRepository = authTrackerRepository;
        this.authService = authService;
        this.bankerService = bankerService;
        this.accountRepository = accountRepository;
    }

}
