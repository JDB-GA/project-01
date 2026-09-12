package repositories;

import general.Constants;
import general.Functions;

import java.util.List;

public class TransactionRepository {

    public void save(
            String accountId,
            String transactionType,
            double amount,
            String relatedAccountId,
            double newBalance
    ) {
        String row = String.join(
                Constants.CSV_SEPARATOR,
                Functions.generateUUID(),
                accountId,
                transactionType,
                String.valueOf(amount),
                String.valueOf(newBalance),
                Constants.EMPTY_STRING,
                relatedAccountId,
                Functions.getNow()
        );

        Functions.addRow(Constants.TRANSACTION_TABLE, List.of(row));
    }

    public void save(String accountId, String transactionType, double amount, double newBalance) {
        save(accountId, transactionType, amount, Constants.EMPTY_STRING, newBalance);
    }

    public List<String[]> getTransactionTable() {
        return Functions.getTable(Constants.TRANSACTION_TABLE);
    }
}
