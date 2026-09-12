package app;

import auth.AuthService;
import banker.BankerService;
import general.Constants;
import common.CommonService;
import repositories.AccountRepository;

import java.util.Map;
import java.util.Scanner;

public class Banker {

    BankerService bankerService;
    CommonService common;
    AuthService authService;

    public Banker(BankerService bankerService, AuthService authService, AccountRepository accountRepository) {
        this.bankerService = bankerService;
        this.authService = authService;
        this.common = new CommonService(authService, accountRepository);
    }

    private void withdraw(Scanner scanner, String[] actor) {
        String customerId = common.chooseFromUsernames(scanner, "Choose customer:");
        String accountId = common.chooseFromAccounts(scanner, customerId, "Choose account to withdraw:");

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        boolean result = bankerService.withdraw(
                actor[0],
                customerId,
                accountId,
                amount
        );

        if (result) {
            System.out.println(Constants.WITHDRAW_SUCCESS);
        } else {
            System.out.println(Constants.GENERAL_ERROR);
        }
    }

    private void deposit(Scanner scanner, String[] actor) {
        String customerId = common.chooseFromUsernames(scanner, "Choose customer:");
        String accountId = common.chooseFromAccounts(scanner, customerId, "Choose account to deposit:");

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        boolean result = bankerService.deposit(
                actor[0],
                customerId,
                accountId,
                amount
        );

        if (result) {
            System.out.println(Constants.DEPOSIT_SUCCESS);
        } else {
            System.out.println(Constants.GENERAL_ERROR);
        }
    }

    private void transfer(Scanner scanner, String[] actor) {
        String sourceAccountOwnerId = common.chooseFromUsernames(scanner, "Choose Source customer:");
        String fromAccountId = common.chooseFromAccounts(scanner, sourceAccountOwnerId, "Choose source account:");
        String destinationAccountOwnerId = common.chooseFromUsernames(scanner, "Choose Destination customer:");
        String toAccountId = common.chooseFromAccounts(scanner, destinationAccountOwnerId, "Choose destination account:");

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        String result = bankerService.transfer(
                actor[0],
                sourceAccountOwnerId,
                fromAccountId,
                toAccountId,
                amount
        );

        System.out.println(result);
    }

    private void addCustomer(Scanner scanner, String[] actor) {
        System.out.print("Customer Username: ");
        String username = scanner.nextLine();
        System.out.print("Customer Password: ");
        String password = scanner.nextLine();

        String initialAccountType = common.chooseAccountType(scanner, "Customer Initial Account Type:").name();
        String result = this.bankerService.addCustomer(actor[0], username, password, initialAccountType);
        System.out.println(result);
    }


    private void createAccount(Scanner scanner, String[] actor) {
        String customerId = common.chooseFromUsernames(
                scanner,
                "Choose customer: "
        );

        String accountType = common.chooseAccountType(scanner, "Account Type:").name();

        String result = bankerService.createAccount(
                actor[0],
                customerId,
                accountType
        );

        System.out.println(result);
    }

    public Map<Integer, Runnable> bankerCaller(
            Scanner scanner,
            String[] actor
    ) {
        return Map.of(
                1, () -> withdraw(scanner, actor),
                2, () -> deposit(scanner, actor),
                3, () -> transfer(scanner, actor),
                4, () -> addCustomer(scanner, actor),
                5, () -> common.displayTransactions(scanner, actor, bankerService, false),
                6, () -> createAccount(scanner, actor),
                7, () -> common.logout(actor)
        );
    }
}
