package app;

import auth.AuthService;
import general.Constants;

import java.util.Optional;
import java.util.Scanner;

public class Auth {

    public static Optional<String[]> authenticateActor(AuthService authService, Scanner scanner) {
        boolean notLoggedIn = true;
        String[] actor = null;
        while (notLoggedIn) {
            System.out.print("Username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Password: ");
            String password = scanner.nextLine();

            String loginResult = authService.login(username, password);
            System.out.println(loginResult);

            if (Constants.LOGIN_SUCCESSFUL.equals(loginResult) || Constants.USER_ALREADY_LOGGED_IN.equals(loginResult)) {
                notLoggedIn = false;
                actor = authService.getUserByUsername(username);
            }

        }

        return actor.length == 0 ? Optional.empty() : Optional.of(actor);
    }
}
