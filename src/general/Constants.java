package general;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    public enum UserRole {
        CUSTOMER,
        BANKER
    }

    public enum AccountType {
        CHECKING,
        SAVINGS
    }

    public enum AccountStatus {
        ACTIVE,
        DISABLED
    }

    public enum TransactionType {
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        OVERDRAFT_FEE
    }

    public static final int OVERDRAFT_COUNT_DEFAULT = 0;
    public static final int OVERDRAFT_PENALTY_DEFAULT = 35;
    public static final double ACCOUNT_BALANCE_DEFAULT = 0.0;
    public static final String EMPTY_STRING = "";
    public static final String CSV_SEPARATOR = ",";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String ZERO = "0";

    public static final String BALANCE_UPDATE_SUCCESS = "The balance updated successfully!";
    public static final String TABLE_WRITE_ERROR = "An error occurred while updating table field";
    public static final String TABLE_READ_ERROR = "An error occurred while adding new columns";
    public static final String TABLE_APPEND_ERROR = "An error occurred while reading table";
    public static final String GENERAL_ERROR = "Error";
    public static final String INVALID_USERNAME = "Invalid username. Try again.";
    public static final String INVALID_ACCOUNT_TYPE = "Invalid account type. Try again.";

    public static final String INCORRECT_CREDENTIALS = "Incorrect Credentials";
    public static final String TOO_MANY_FAILED_ATTEMPTS = "Too many failed attempts. Try again later";
    public static final String USER_ALREADY_LOGGED_IN = "User already logged in";
    public static final String LOGIN_SUCCESSFUL = "Login successful";
    public static final String LOGOUT_SUCCESSFUL = "Logout successful";
    public static final String DEPOSIT_SUCCESS = "Account deposit success !";
    public static final String WITHDRAW_SUCCESS = "Account withdraw success !";
    public static final String ACCOUNT_ALREADY_EXISTS = "The account already exists!";
    public static final String REGISTRATION_ERROR = "An error occurred during user registration";
    public static final String CUSTOMER_ALREADY_EXISTS = "The Customer already exists! Use create account instead!";
    public static final String ACCOUNT_ALREADY_EXISTS_FOR_CUSTOMER = " account already exists for this customer";
    public static final String ACCOUNT_CREATED_FOR_CUSTOMER = " account created for this customer of Id: ";

    public static final Path USER_TABLE = Paths.get("src", "database", "users.csv");
    public static final Path ACCOUNT_TABLE = Paths.get("src", "database", "accounts.csv");
    public static final Path TRANSACTION_TABLE = Paths.get("src", "database", "transactions.csv");
    public static final Path AUTH_TRACKER_TABLE = Paths.get("src", "database", "auth_tracker.csv");

    public static final String USER_TABLE_HEADER = String.join(CSV_SEPARATOR, "ID", "USERNAME", "HASHED_PASSWORD", "ROLE", "CREATED_AT");
    public static final String ACCOUNT_TABLE_HEADER = String.join(CSV_SEPARATOR, "ID", "CUSTOMER_ID", "ACCOUNT_TYPE", "BALANCE", "STATUS", "OVERDRAFT_COUNT", "CREATED_AT");
    public static final String AUTH_TRACKER_TABLE_HEADER = String.join(CSV_SEPARATOR, "ID", "USER_ID", "IS_LOGGED_IN", "FAILED_ATTEMPTS", "LOCKED_UNTIL", "UPDATED_AT");
}
