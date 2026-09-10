import auth.AuthService;
import general.Constants;
import repositories.AuthTrackerRepository;
import repositories.UserRepository;

import java.util.Optional;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        AuthService authService = new AuthService(
                new UserRepository(),
                new AuthTrackerRepository()
        );

        try (Scanner scanner = new Scanner(System.in)) {
            authService.resetPassword("54a3b54a-1874-4cc9-ba2d-e4d2858c303c", "something");
            Optional<String[]> actor = authenticateActor(authService, scanner);

            if (actor.isEmpty()) {
                return;
            }

            String[] actorRecord = actor.get();
            System.out.println("Welcome " + actorRecord[1] + " (" + actorRecord[3] + ")");

            authService.logout(actorRecord[0]);
        }
    }

    private static Optional<String[]> authenticateActor(
            AuthService authService,
            Scanner scanner
    ) {
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        String loginResult = authService.login(username, password);
        System.out.println(loginResult);

        if (!Constants.LOGIN_SUCCESSFUL.equals(loginResult)) {
            return Optional.empty();
        }

        String[] actor = authService.getUserByUsername(username);
        return actor.length == 0
                ? Optional.empty()
                : Optional.of(actor);
    }
}
