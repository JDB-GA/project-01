package transaction;

import general.Constants;
import general.Functions;

import java.util.List;

public abstract class TransactionService {

    public abstract String withdraw();

    public abstract String deposit();

    public abstract String transfer(String accountOneId, String accountTwoId);

    public String transaction(String fromId, String transactionType, double amount, String description, String toId) {
        String now = Functions.getNow();
        String amountString = String.valueOf(amount);
        List<String> row = List.of(fromId, transactionType, amountString, description, toId, now);
        Functions.addRow(Constants.TRANSACTION_TABLE, row);
        return String.join(Constants.EMPTY_STRING, transactionType, Constants.TRANSACTION_EXECUTED_SUCCESSFULLY);
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
