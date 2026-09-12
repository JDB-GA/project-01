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
        String accountOwnerId = common.chooseFromUsernames(scanner, "Choose customer:");
        String fromAccountId = common.chooseFromAccounts(scanner, accountOwnerId, "Choose source account:");
        String toAccountId = common.chooseFromAccounts(scanner, accountOwnerId, "Choose destination account:");

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        String result = bankerService.transfer(
                actor[0],
                accountOwnerId,
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
        System.out.print("Customer Initial Account Type: ");
        String initialAccountType = scanner.nextLine();

        String result = this.bankerService.addCustomer(actor[0], username, password, initialAccountType);
        System.out.println(result);
    }


    private void displayTransactions(Scanner scanner, String[] actor) {
        String customerId = common.chooseFromUsernames(
                scanner,
                "Choose customer: "
        );

        String accountId = common.chooseFromAccounts(
                scanner,
                customerId,
                "Choose account: "
        );

        common.printTransactions(
                bankerService.displayAllTransactions(
                        actor[0],
                        customerId,
                        accountId
                )
        );
    }

    private void createAccount(Scanner scanner, String[] actor) {
        String customerId = common.chooseFromUsernames(
                scanner,
                "Choose customer: "
        );

        System.out.print("Account type: ");
        String accountType = scanner.nextLine()
                .trim()
                .toUpperCase();

        String result = bankerService.createAccount(
                actor[0],
                customerId,
                accountType
        );

        System.out.println(result);
    }


    private void logout(String[] actor) {
        boolean result = authService.logout(actor[0]);
        if (result) {
            System.out.println(Constants.LOGOUT_SUCCESSFUL);
        }
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
                5, () -> displayTransactions(scanner, actor),
                6, () -> createAccount(scanner, actor),
                7, () -> logout(actor)
        );
    }
}
