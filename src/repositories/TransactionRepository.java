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

    public double getTodayTotal(
            String accountId,
            String transactionType
    ) {
        String today = Functions.getNow().substring(0, 10);

        return getTransactionTable()
                .stream()
                .filter(transaction ->
                        transaction[1].equals(accountId)
                                && transaction[2].equals(transactionType)
                                && transaction[7].startsWith(today)
                )
                .mapToDouble(transaction ->
                        Double.parseDouble(transaction[3])
                )
                .sum();
    }
}
