package test;

import auth.AuthService;
import banker.BankerService;
import card.CardService;
import card.CardLimitService;
import customer.CustomerService;
import general.Constants;
import org.junit.jupiter.api.Test;
import repositories.*;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    @Test
    void runsTheFullCustomerScenario() {
        UserRepository users = new UserRepository();
        AuthTrackerRepository loginTracker = new AuthTrackerRepository();
        AccountRepository accounts = new AccountRepository();
        TransactionRepository transactionHistory = new TransactionRepository();

        AuthService auth = new AuthService(users, loginTracker);
        CardRepository cards = new CardRepository();
        CardService cardService = new CardService(cards, accounts);
        CardLimitService cardLimits = new CardLimitService(cardService, transactionHistory);
        BankerService banker = new BankerService(accounts, auth, transactionHistory, cardService, cardLimits);

        String setupBanker =
                "testSetupBanker" + System.currentTimeMillis();

        auth.register(
                setupBanker,
                "password",
                Constants.UserRole.BANKER.name()
        );

        assertEquals(
                Constants.LOGIN_SUCCESSFUL,
                auth.login(setupBanker, "password")
        );

        String bankerId = users.findByUsername(setupBanker)[0];

        String customerUsername =
                "testCustomer" + System.currentTimeMillis();

        String otherCustomerUsername =
                "testOtherCustomer" + System.currentTimeMillis();

        banker.addCustomer(
                bankerId,
                customerUsername,
                "password",
                Constants.AccountType.CHECKING.name(),
                Constants.CardType.MASTERCARD
        );

        banker.addCustomer(
                bankerId,
                otherCustomerUsername,
                "password",
                Constants.AccountType.SAVINGS.name(),
                Constants.CardType.MASTERCARD_TITANIUM
        );

        String customerId =
                users.findByUsername(customerUsername)[0];

        String otherCustomerId =
                users.findByUsername(otherCustomerUsername)[0];

        String customerAccount = findAccount(
                accounts,
                customerId,
                Constants.AccountType.CHECKING.name()
        );

        String otherAccount = findAccount(
                accounts,
                otherCustomerId,
                Constants.AccountType.SAVINGS.name()
        );

        assertTrue(auth.logout(bankerId));

        assertEquals(
                Constants.LOGIN_SUCCESSFUL,
                auth.login(customerUsername, "password")
        );

        CustomerService customer =
                new CustomerService(accounts, transactionHistory, auth, cardLimits);

        assertTrue(
                customer.deposit(
                        customerId,
                        customerAccount,
                        200
                )
        );

        assertTrue(
                customer.withdraw(
                        customerId,
                        customerAccount,
                        50
                )
        );

        assertFalse(
                customer.deposit(
                        customerId,
                        customerAccount,
                        -10
                )
        );

        assertFalse(
                customer.deposit(
                        customerId,
                        otherCustomerId,
                        otherAccount,
                        10
                )
        );

        assertFalse(
                customer.deposit(
                        customerId,
                        customerAccount,
                        Constants.CARD_OWN_DEPOSIT_LIMIT
                )
        );

        assertFalse(
                customer.withdraw(
                        customerId,
                        customerAccount,
                        Constants.MASTERCARD_WITHDRAW_LIMIT
                )
        );

        assertTrue(
                customer.deposit(
                        customerId,
                        customerAccount,
                        6_000
                )
        );

        assertEquals(
                Constants.BALANCE_UPDATE_SUCCESS,
                customer.transfer(
                        customerId,
                        customerAccount,
                        otherAccount,
                        6_000
                )
        );

        assertEquals(
                Constants.GENERAL_ERROR,
                customer.transfer(
                        customerId,
                        customerAccount,
                        otherAccount,
                        4_001
                )
        );

        assertEquals(
                Constants.BALANCE_UPDATE_SUCCESS,
                customer.transfer(
                        customerId,
                        customerAccount,
                        otherAccount,
                        25
                )
        );

        assertTrue(
                customer.displayAllTransactions(
                        customerId,
                        otherCustomerId,
                        customerAccount
                ).isEmpty()
        );

        assertTrue(
                customer.displayAllTransactions(
                        customerId,
                        customerId,
                        customerAccount
                ).size() >= 2
        );

        assertTrue(
                auth.resetPassword(customerId, "new-password")
        );

        assertEquals(
                Constants.LOGIN_SUCCESSFUL,
                auth.login(customerUsername, "new-password")
        );

        assertTrue(auth.logout(customerId));
    }

    private String findAccount(
            AccountRepository accounts,
            String customerId,
            String accountType
    ) {
        return accounts.findAll()
                .stream()
                .filter(account ->
                        account[1].equals(customerId)
                                && account[2].equals(accountType)
                )
                .findFirst()
                .orElseThrow()[0];
    }
}
