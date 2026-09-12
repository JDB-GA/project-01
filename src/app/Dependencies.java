package app;

import auth.AuthService;
import banker.BankerService;
import customer.CustomerService;
import repositories.AccountRepository;
import repositories.AuthTrackerRepository;
import repositories.UserRepository;

public class Dependencies {
    UserRepository userRepository;
    AuthTrackerRepository authTrackerRepository;
    AuthService authService;
    BankerService bankerService;
    AccountRepository accountRepository;
    CustomerService customerService;

    public Dependencies
            (AuthService authService,
             UserRepository userRepository,
             AuthTrackerRepository authTrackerRepository,
             BankerService bankerService,
             AccountRepository accountRepository,
             CustomerService customerService
            ) {
        this.userRepository = userRepository;
        this.authTrackerRepository = authTrackerRepository;
        this.authService = authService;
        this.bankerService = bankerService;
        this.accountRepository = accountRepository;
        this.customerService = customerService;
    }

}
