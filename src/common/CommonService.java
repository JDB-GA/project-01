package common;

import auth.AuthService;
import general.Constants;
import repositories.AccountRepository;
import transaction.ITransactionService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

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

    public Constants.AccountType chooseAccountType(
            Scanner scanner,
            String message
    ) {
        Map<String, Constants.AccountType> accountTypes = Map.of(
                Constants.AccountType.SAVINGS.name(), Constants.AccountType.SAVINGS,
                Constants.AccountType.CHECKING.name(), Constants.AccountType.CHECKING
        );

        while (true) {
            System.out.println(" - SAVINGS");
            System.out.println(" - CHECKING");
            System.out.print(message);

            String choice = scanner.nextLine();

            Constants.AccountType accountType = accountTypes.get(choice.toUpperCase());

            if (accountType != null) {
                return accountType;
            }

            System.out.println(Constants.INVALID_ACCOUNT_TYPE);
        }
    }

    public void printTransactions(List<String[]> transactions) {
        System.out.println("---------------------------------------------------");
        transactions.forEach(transaction ->
                System.out.printf(
                        "ID: %s | Type: %s | Amount: %s | New balance: %s | Date: %s%n",
                        transaction[0],
                        transaction[2],
                        transaction[3],
                        transaction[4],
                        transaction[7]
                ));
        System.out.println("---------------------------------------------------");
    }

    public void logout(String[] actor) {
        boolean result = authService.logout(actor[0]);
        if (result) {
            System.out.println(Constants.LOGOUT_SUCCESSFUL);
        }
    }

    public void displayTransactions(
            Scanner scanner,
            String[] actor,
            ITransactionService transactionService,
            boolean isCustomer
    ) {
        String customerId = actor[0];
        if (!isCustomer) {
            customerId = chooseFromUsernames(scanner, "Choose customer: ");
        }
        String accountId = chooseFromAccounts(scanner, customerId, "Choose account: ");
        List<String[]> transactions = transactionService.displayAllTransactions(
                actor[0], customerId, accountId);
        printTransactions(filterTransactions(scanner, transactions));
    }

    private List<String[]> filterTransactions(Scanner scanner, List<String[]> transactions) {
        while (true) {
            System.out.println(Constants.TRANSACTION_FILTER_TITLE);
            System.out.println(Constants.TRANSACTION_FILTER_TODAY_OPTION + ". " + Constants.TRANSACTION_FILTER_TODAY);
            System.out.println(Constants.TRANSACTION_FILTER_YESTERDAY_OPTION + ". " + Constants.TRANSACTION_FILTER_YESTERDAY);
            System.out.println(Constants.TRANSACTION_FILTER_LAST_7_DAYS_OPTION + ". " + Constants.TRANSACTION_FILTER_LAST_7_DAYS);
            System.out.println(Constants.TRANSACTION_FILTER_LAST_30_DAYS_OPTION + ". " + Constants.TRANSACTION_FILTER_LAST_30_DAYS);
            System.out.println(Constants.TRANSACTION_FILTER_THIS_MONTH_OPTION + ". " + Constants.TRANSACTION_FILTER_THIS_MONTH);
            System.out.println(Constants.TRANSACTION_FILTER_CUSTOM_OPTION + ". " + Constants.TRANSACTION_FILTER_CUSTOM);
            System.out.println(Constants.TRANSACTION_FILTER_BACK_OPTION + ". " + Constants.TRANSACTION_FILTER_BACK);
            System.out.print("Choose option: ");

            String choice = scanner.nextLine().trim();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime start;
            LocalDateTime end = now;

            try {
                int option = Integer.parseInt(choice);
                switch (option) {
                    case Constants.TRANSACTION_FILTER_TODAY_OPTION -> start = now.toLocalDate().atStartOfDay();
                    case Constants.TRANSACTION_FILTER_YESTERDAY_OPTION -> {
                        LocalDate yesterday = now.toLocalDate().minusDays(1);
                        start = yesterday.atStartOfDay();
                        end = yesterday.plusDays(1).atStartOfDay();
                    }
                    case Constants.TRANSACTION_FILTER_LAST_7_DAYS_OPTION -> start = now.minusDays(7);
                    case Constants.TRANSACTION_FILTER_LAST_30_DAYS_OPTION -> start = now.minusDays(30);
                    case Constants.TRANSACTION_FILTER_THIS_MONTH_OPTION -> start = YearMonth.from(now).atDay(1).atStartOfDay();
                    case Constants.TRANSACTION_FILTER_CUSTOM_OPTION -> {
                        System.out.print(Constants.TRANSACTION_CUSTOM_RANGE_PROMPT);
                        String[] values = scanner.nextLine().trim().split("/", 2);
                        if (values.length != 2) throw new DateTimeParseException("Invalid range", "", 0);
                        start = LocalDateTime.parse(values[0], Constants.TRANSACTION_DATE_TIME_FORMATTER);
                        end = LocalDateTime.parse(values[1], Constants.TRANSACTION_DATE_TIME_FORMATTER);
                        if (!start.isBefore(end)) throw new DateTimeParseException("Invalid range", "", 0);
                    }
                    case Constants.TRANSACTION_FILTER_BACK_OPTION -> { return transactions; }
                    default -> throw new DateTimeParseException("Invalid option", choice, 0);
                }
                LocalDateTime rangeStart = start;
                LocalDateTime rangeEnd = end;
                return transactions.stream()
                        .filter(transaction -> isWithinRange(transaction, rangeStart, rangeEnd))
                        .toList();
            } catch (NumberFormatException | DateTimeParseException exception) {
                System.out.println(Constants.INVALID_TRANSACTION_RANGE);
            }
        }
    }

    private boolean isWithinRange(String[] transaction, LocalDateTime start, LocalDateTime end) {
        LocalDateTime createdAt = LocalDateTime.parse(transaction[7]);
        return !createdAt.isBefore(start) && createdAt.isBefore(end);
    }

    @Override
    public Constants.CardType chooseCardType(
            Scanner scanner,
            String message
    ) {
        Map<String, Constants.CardType> cardTypes = Map.of(
                "1", Constants.CardType.MASTERCARD,
                "2", Constants.CardType.MASTERCARD_TITANIUM,
                "3", Constants.CardType.MASTERCARD_PLATINUM
        );

        while (true) {
            System.out.println("1. Mastercard");
            System.out.println("2. Mastercard Titanium");
            System.out.println("3. Mastercard Platinum");
            System.out.print(message);

            Constants.CardType selected =
                    cardTypes.get(scanner.nextLine().trim());

            if (selected != null) {
                return selected;
            }

            System.out.println(Constants.INVALID_CARD_TYPE);
        }
    }

}
