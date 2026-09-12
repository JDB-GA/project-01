package app;

import general.AppLogger;
import general.Constants;

import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class App {
    private final Dependencies dependencies;

    public App(Dependencies dependencies) {
        this.dependencies = dependencies;
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            Optional<String[]> actor = Auth.authenticateActor(this.dependencies.authService, scanner);

            if (actor.isEmpty()) {
                return;
            }

            String[] actorRecord = actor.get();
            System.out.println("Welcome " + actorRecord[1] + " (" + actorRecord[3] + ")");

            if (actorRecord[3].equals(Constants.UserRole.BANKER.name())) {
                startBanker(scanner, actorRecord);

            } else if (actorRecord[3].equals(Constants.UserRole.CUSTOMER.name())) {
                startCustomer(scanner, actorRecord);
            }

        } catch (Exception e) {
            AppLogger.error(Constants.GENERAL_ERROR, e);
        }
    }


    private void startBanker(Scanner scanner, String[] actorRecord) {
        Banker banker = new Banker(
                dependencies.bankerService,
                dependencies.authService,
                dependencies.accountRepository
        );

        Map<Integer, Runnable> options = banker.bankerCaller(scanner, actorRecord);
        boolean running = true;
        while (running) {
            System.out.println("""
                    1. Withdraw
                    2. Deposit
                    3. Transfer
                    4. Add customer
                    5. Display transactions
                    6. Create account
                    7. Logout
                    """);

            System.out.print("Choose option: ");
            int option = Integer.parseInt(scanner.nextLine());

            Runnable selectedOption = options.get(option);

            if (selectedOption == null) {
                System.out.println(Constants.GENERAL_ERROR);
                continue;
            }

            selectedOption.run();

            if (option == 7) {
                running = false;
            }
        }

    }

    private void startCustomer(Scanner scanner, String[] actorRecord) {
        Customer customer = new Customer(
                dependencies.customerService,
                dependencies.authService,
                dependencies.accountRepository
        );

        Map<Integer, Runnable> options = customer.customerCaller(scanner, actorRecord);
        boolean running = true;
        while (running) {
            System.out.println("""
                    1. Withdraw
                    2. Deposit
                    3. Transfer
                    4. Display transactions
                    5. Reset password
                    6. Logout
                    """);

            System.out.print("Choose option: ");
            int option = Integer.parseInt(scanner.nextLine());

            Runnable selectedOption = options.get(option);

            if (selectedOption == null) {
                System.out.println(Constants.GENERAL_ERROR);
                continue;
            }

            selectedOption.run();

            if (option == 6) {
                running = false;
            }
        }

    }


}
