package common;

import auth.AuthService;
import general.Constants;
import repositories.AccountRepository;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CommonService implements ICommonService {

    protected final AuthService authService;
    protected final AccountRepository accountRepository;

    public CommonService(AuthService authService, AccountRepository accountRepository) {
        this.authService = authService;
        this.accountRepository = accountRepository;
    }

    @Override
    public String chooseFromUsernames(Scanner scanner, String message) {
        List<String[]> users = authService.getUsers();
        users.stream()
                .filter(user -> user[3].equals(Constants.UserRole.CUSTOMER.name()))
                .forEach(user -> System.out.println("- " + user[1]));

        while (true) {
            System.out.print(message);
            String username = scanner.nextLine().trim().toLowerCase();
            Optional<String[]> selectedUser = users.stream()
                    .filter(user -> user[1].equals(username)
                            && user[3].equals(Constants.UserRole.CUSTOMER.name()))
                    .findFirst();

            if (selectedUser.isPresent()) return selectedUser.get()[0];
            System.out.println(Constants.INVALID_USERNAME);
        }
    }

    @Override
    public String chooseFromAccounts(Scanner scanner, String customerId, String message) {
        List<String[]> accounts = accountRepository.findAll().stream()
                .filter(account -> account[1].equals(customerId))
                .toList();

        accounts.forEach(account ->
                System.out.println("- " + account[2] + " (" + account[0] + ")"));

        while (true) {
            System.out.print(message + " ");
            String accountType = scanner.nextLine().trim().toUpperCase();
            Optional<String[]> account = accounts.stream()
                    .filter(row -> row[2].equalsIgnoreCase(accountType))
                    .findFirst();

            if (account.isPresent()) return account.get()[0];
            System.out.println(Constants.INVALID_ACCOUNT_TYPE);
        }
    }

    public void printTransactions(List<String[]> transactions) {
        transactions.forEach(transaction ->
                System.out.printf(
                        "ID: %s | Type: %s | Amount: %s | Date: %s%n",
                        transaction[0],
                        transaction[2],
                        transaction[3],
                        transaction[6]
                )
        );
    }

}
