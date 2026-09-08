package general;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Constants {
    public static final String CUSTOMER_ROLE = "CUSTOMER";
    public static final String BANKER_ROLE = "BANKER";
    public static final String CHECKING_ACCOUNT = "CHECKING";
    public static final String SAVINGS_ACCOUNT = "SAVINGS";
    public static final String ACTIVE = "ACTIVE";
    public static final String DISABLED = "DISABLED";
    public static final Path USER_TABLE = Paths.get("src", "database", "users.csv");
    public static final Path CUSTOMER_TABLE = Paths.get("src", "database", "customers.csv");
    public static final Path BANKER_TABLE = Paths.get("src", "database", "bankers.csv");
    public static final Path ACCOUNT_TABLE = Paths.get("src", "database", "accounts.csv");
}
