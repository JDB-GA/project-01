package app;

import auth.AuthService;
import customer.CustomerService;
import general.Constants;
import common.CommonService;
import repositories.AccountRepository;

import java.util.Map;
import java.util.Scanner;

public class Customer {

    CustomerService customerService;
    CommonService common;
    AuthService authService;

    public Customer(CustomerService customerService, AuthService authService, AccountRepository accountRepository) {
        this.customerService = customerService;
        this.authService = authService;
        this.common = new CommonService(authService, accountRepository);
    }

    private void withdraw(Scanner scanner, String[] actor) {
        String accountId = common.chooseFromAccounts(scanner, actor[0], "Choose account to withdraw:");

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        boolean result = customerService.withdraw(
                actor[0],
                actor[0],
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
        String accountId = common.chooseFromAccounts(scanner, actor[0], "Choose account to deposit:");

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        boolean result = customerService.deposit(
                actor[0],
                actor[0],
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
        String fromAccountId = common.chooseFromAccounts(scanner, actor[0], "Choose source account:");
        String destinationAccountOwnerId = common.chooseFromUsernames(scanner, "Choose Destination customer:");
        String toAccountId = common.chooseFromAccounts(scanner, destinationAccountOwnerId, "Choose destination account:");

        System.out.print("Amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        String result = customerService.transfer(
                actor[0],
                actor[0],
                fromAccountId,
                toAccountId,
                amount
        );

        System.out.println(result);
    }

    private void resetPassword(Scanner scanner, String[] actor) {
        System.out.print("New Password: ");
        String newPassword = scanner.nextLine();
        boolean passwordUpdated = authService.resetPassword(actor[0], newPassword);
        if (passwordUpdated) {
            System.out.println(Constants.PASSWORD_UPDATE_SUCCESS);
        } else {
            System.out.println(Constants.GENERAL_ERROR);
        }
    }

    public Map<Integer, Runnable> customerCaller(
            Scanner scanner,
            String[] actor
    ) {
        return Map.of(
                1, () -> withdraw(scanner, actor),
                2, () -> deposit(scanner, actor),
                3, () -> transfer(scanner, actor),
                4, () -> common.displayTransactions(scanner, actor, customerService, true),
                5, () -> resetPassword(scanner, actor),
                6, () -> common.logout(actor)
        );
    }
}
