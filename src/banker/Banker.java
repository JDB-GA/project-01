package banker;

import Operation.Operation;
import auth.Auth;
import general.AppLogger;
import general.Constants;
import general.FileControl;
import general.Functions;

import java.util.List;

public class Banker extends Operation implements IBanker {

    @Override
    public String addCustomer(String username, String password, String initialAccountType) {
        Auth auth = new Auth();
        String userId = auth.register(username, password, Constants.UserRole.CUSTOMER.name());
        if (userId.equals("The account already exists!")) {

            return "The Customer already exists! Use create account instead!";
        }

        return createAccount(userId, initialAccountType);
    }

    @Override
    public String createAccount(String customerId, String accountType) {
        try {
            if (checkAccountExists(customerId, accountType)) {

                return accountType + " account already exists for this customer";
            }
            String id = Functions.generateUUID();
            String now = Functions.getNow();
            String status = Constants.AccountStatus.ACTIVE.name();
            double balance = Constants.ACCOUNT_BALANCE_DEFAULT;
            int overdraftCount = Constants.OVERDRAFT_COUNT_DEFAULT;
            String row = id + "," + customerId + "," + accountType + "," + balance + "," + status + "," + overdraftCount + "," + now;
            FileControl.append(Constants.ACCOUNT_TABLE, List.of(row));
        } catch (Exception e) {
            AppLogger.error("Create Account Error", e);
        }

        return accountType + " account created for this customer of Id: " + customerId;
    }

    @Override
    public boolean checkAccountExists(String userId, String accountType) {

        return getAccounts().stream().anyMatch(account -> account[1].equals(userId) && account[2].equals(accountType));
    }

    @Override
    public List<String[]> getAccounts() {

        return Functions.getTable(Constants.ACCOUNT_TABLE);
    }

    @Override
    public String withdraw() {
        return "";
    }

    @Override
    public String deposit() {
        return "";
    }

    @Override
    public String transfer(String accountOneId, String accountTwoId) {
        return "";
    }

}
