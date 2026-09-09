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
    public static final double ACCOUNT_BALANCE_DEFAULT = 0.0;

    public static final Path USER_TABLE = Paths.get("src", "database", "users.csv");
    public static final Path ACCOUNT_TABLE = Paths.get("src", "database", "accounts.csv");
    public static final Path TRANSACTION_TABLE = Paths.get("src", "database", "transactions.csv");
    public static final Path AUTH_TRACKER_TABLE = Paths.get("src", "database", "auth_tracker.csv");

    public static final String USER_TABLE_HEADER = "ID,USERNAME,HASHED_PASSWORD,ROLE,CREATED_AT";
    public static final String ACCOUNT_TABLE_HEADER = "ID,CUSTOMER_ID,ACCOUNT_TYPE,BALANCE,STATUS,OVERDRAFT_COUNT,CREATED_AT";
    public static final String TRANSACTION_TABLE_HEADER = "ID,ACCOUNT_ID,TRANSACTION_TYPE,AMOUNT,DESCRIPTION,RELATED_ACCOUNT_ID,CREATED_AT";
    public static final String AUTH_TRACKER_TABLE_HEADER = "ID,USER_ID,IS_LOGGED_IN,FAILED_ATTEMPTS,LOCKED_UNTIL,UPDATED_AT";
}
