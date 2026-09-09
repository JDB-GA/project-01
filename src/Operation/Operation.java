package Operation;

import auth.Auth;
import general.Constants;
import general.Functions;

import java.util.List;

public abstract class Operation {

    public abstract String withdraw();

    public abstract String deposit();

    public abstract String transfer(String accountOneId, String accountTwoId);

    public String transaction(String accountId, String transactionType, double amount, String description, String relatedAccountId) {
        String now = Functions.getNow();
        String amountString = String.valueOf(amount);
        List<String> row = List.of(accountId, transactionType, amountString, description, relatedAccountId, now);
        Functions.addRow(Constants.TRANSACTION_TABLE, row);
        return transactionType + " transaction executed successfully";
    }
}


// Account Transactions
//
//Withdraw Money (requires login)
//From savings or checking accounts.
//Deposit Money (requires login)
//Into savings or checking accounts.
//Transfer Money (requires login)
//Between a customer's own accounts or to another customer's account.