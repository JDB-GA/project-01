package test;

import auth.AuthService;
import banker.BankerService;
import general.Constants;
import org.junit.jupiter.api.Test;
import repositories.AccountRepository;
import repositories.AuthTrackerRepository;
import repositories.TransactionRepository;
import repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

class BankerServiceTest {

    @Test
    void runsTheFullBankerScenario() throws InterruptedException {
        UserRepository users = new UserRepository();
        AuthTrackerRepository loginTracker = new AuthTrackerRepository();
        AccountRepository accounts = new AccountRepository();
        TransactionRepository transactionHistory = new TransactionRepository();

        AuthService auth = new AuthService(users, loginTracker);
        BankerService banker = new BankerService(accounts, auth, transactionHistory);

        String bankerUsername = "testBanker" + System.currentTimeMillis();
        String bankerPassword = "bankerPassword";

        auth.register(
                bankerUsername,
                bankerPassword,
                Constants.UserRole.BANKER.name()
        );

        assertEquals(
                Constants.INCORRECT_CREDENTIALS,
                auth.login(bankerUsername, "wrong")
        );

        assertEquals(
                Constants.INCORRECT_CREDENTIALS,
                auth.login(bankerUsername, "wrong")
        );

        assertEquals(
                Constants.INCORRECT_CREDENTIALS,
                auth.login(bankerUsername, "wrong")
        );

        assertEquals(
                Constants.TOO_MANY_FAILED_ATTEMPTS,
                auth.login(bankerUsername, bankerPassword)
        );

        Thread.sleep(31_000);

        assertEquals(
                Constants.LOGIN_SUCCESSFUL,
                auth.login(bankerUsername, bankerPassword)
        );

        String bankerId = users.findByUsername(bankerUsername)[0];

        String customerOneUsername =
                "testCustomerOne" + System.currentTimeMillis();

        String customerTwoUsername =
                "testCustomerTwo" + System.currentTimeMillis();

        assertTrue(
                banker.addCustomer(
                        bankerId,
                        customerOneUsername,
                        "password",
                        Constants.AccountType.SAVINGS.name()
                ).contains("created")
        );

        assertTrue(
                banker.addCustomer(
                        bankerId,
                        customerTwoUsername,
                        "password",
                        Constants.AccountType.CHECKING.name()
                ).contains("created")
        );

        String customerOneId =
                users.findByUsername(customerOneUsername)[0];

        String customerTwoId =
                users.findByUsername(customerTwoUsername)[0];

        assertTrue(
                banker.createAccount(
                        bankerId,
                        customerOneId,
                        Constants.AccountType.SAVINGS.name()
                ).contains("already exists")
        );

        assertTrue(
                banker.createAccount(
                        bankerId,
                        customerOneId,
                        Constants.AccountType.CHECKING.name()
                ).contains("created")
        );

        assertTrue(
                banker.createAccount(
                        bankerId,
                        customerTwoId,
                        Constants.AccountType.SAVINGS.name()
                ).contains("created")
        );

        String sourceAccount = findAccount(
                accounts,
                customerOneId,
                Constants.AccountType.SAVINGS.name()
        );

        String destinationAccount = findAccount(
                accounts,
                customerTwoId,
                Constants.AccountType.CHECKING.name()
        );

        assertTrue(
                banker.deposit(
                        bankerId,
                        customerOneId,
                        sourceAccount,
                        200
                )
        );

        assertTrue(
                banker.withdraw(
                        bankerId,
                        customerOneId,
                        sourceAccount,
                        200
                )
        );

        assertTrue(
                banker.withdraw(
                        bankerId,
                        customerOneId,
                        sourceAccount,
                        100
                )
        );

        assertTrue(
                banker.withdraw(
                        bankerId,
                        customerOneId,
                        sourceAccount,
                        100
                )
        );

        assertEquals(
                Constants.AccountStatus.DISABLED.name(),
                accounts.getUserAccount(sourceAccount)[4]
        );

        assertTrue(
                banker.deposit(
                        bankerId,
                        customerOneId,
                        sourceAccount,
                        270
                )
        );

        assertEquals(
                0.0,
                Double.parseDouble(accounts.getUserAccount(sourceAccount)[3])
        );

        assertEquals(
                Constants.AccountStatus.ACTIVE.name(),
                accounts.getUserAccount(sourceAccount)[4]
        );

        assertTrue(
                banker.deposit(
                        bankerId,
                        customerOneId,
                        sourceAccount,
                        100
                )
        );

        assertEquals(
                100.0,
                Double.parseDouble(accounts.getUserAccount(sourceAccount)[3])
        );

        assertEquals(
                Constants.BALANCE_UPDATE_SUCCESS,
                banker.transfer(
                        bankerId,
                        customerOneId,
                        sourceAccount,
                        destinationAccount,
                        25
                )
        );

        assertEquals(
                75.0,
                Double.parseDouble(accounts.getUserAccount(sourceAccount)[3])
        );

        assertEquals(
                25.0,
                Double.parseDouble(accounts.getUserAccount(destinationAccount)[3])
        );

        assertFalse(
                banker.displayAllTransactions(
                        bankerId,
                        customerOneId,
                        sourceAccount
                ).isEmpty()
        );

        assertTrue(auth.logout(bankerId));
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
