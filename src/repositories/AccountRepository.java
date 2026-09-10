package repositories;

import general.Constants;
import general.Functions;

import java.util.List;

public class AccountRepository {

    public List<String[]> findAll() {
        return Functions.getTable(Constants.ACCOUNT_TABLE);
    }

    public boolean existsForCustomer(String customerId, String accountType) {
        return findAll()
                .stream()
                .anyMatch(account -> account[1].equals(customerId) && account[2].equals(accountType));
    }

    public void save(
            String id,
            String customerId,
            String accountType,
            double balance,
            String status,
            int overdraftCount,
            String createdAt
    ) {
        String row = String.join(
                Constants.CSV_SEPARATOR,
                id,
                customerId,
                accountType,
                String.valueOf(balance),
                status,
                String.valueOf(overdraftCount),
                createdAt
        );
        Functions.addRow(Constants.ACCOUNT_TABLE, List.of(row));
    }

    public String[] getUserAccount(String accountId) {
        return findAll()
                .stream()
                .filter(row -> row[0].equals(accountId))
                .findFirst()
                .orElseGet(() -> new String[0]);
    }

    public void updateBalance(String accountId, String newBalance) {
        Functions.updateTableField(
                Constants.ACCOUNT_TABLE,
                Constants.ACCOUNT_TABLE_HEADER,
                0,
                accountId,
                3,
                newBalance
        );

    }

    public void updateOverdraftCount(String accountId, int overdraftCount) {
        Functions.updateTableField(
                Constants.ACCOUNT_TABLE,
                Constants.ACCOUNT_TABLE_HEADER,
                0,
                accountId,
                5,
                String.valueOf(overdraftCount)
        );
    }

    public void updateStatus(String accountId, String status) {
        Functions.updateTableField(
                Constants.ACCOUNT_TABLE,
                Constants.ACCOUNT_TABLE_HEADER,
                0,
                accountId,
                4,
                status
        );
    }
}
