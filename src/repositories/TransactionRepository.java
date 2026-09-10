package repositories;

import general.Constants;
import general.Functions;

import java.util.List;

public class TransactionRepository {
    public String save(String fromAccountId, String transactionType, double amount, String description, String toAccountId) {
        String now = Functions.getNow();
        String amountString = String.valueOf(amount);
        List<String> row = List.of(fromAccountId, transactionType, amountString, description, toAccountId, now);
        Functions.addRow(Constants.TRANSACTION_TABLE, row);
        return String.join(Constants.SPACE, transactionType, Constants.TRANSACTION_EXECUTED_SUCCESSFULLY);
    }
}
