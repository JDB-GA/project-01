package repositories;

import general.Constants;
import general.Functions;

import java.util.List;

public class TransactionRepository {

    public void save(String accountId, String transactionType, double amount, String relatedAccountId) {
        String row = String.join(
                Constants.CSV_SEPARATOR,
                Functions.generateUUID(),
                accountId,
                transactionType,
                String.valueOf(amount),
                Constants.EMPTY_STRING,
                relatedAccountId,
                Functions.getNow()
        );

        Functions.addRow(Constants.TRANSACTION_TABLE, List.of(row));
    }

    public void save(String accountId, String transactionType, double amount) {
        save(accountId, transactionType, amount, Constants.EMPTY_STRING);
    }

    public List<String[]> getTransactionTable() {
        return Functions.getTable(Constants.TRANSACTION_TABLE);
    }
}
