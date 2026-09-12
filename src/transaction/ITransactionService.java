package transaction;

import java.util.List;

public interface ITransactionService {
    boolean deposit(String userId, String accountId, double amount);

    boolean deposit(
            String actorUserId,
            String accountOwnerId,
            String accountId,
            double amount
    );

    boolean withdraw(String userId, String accountId, double amount);

    boolean withdraw(
            String actorUserId,
            String accountOwnerId,
            String accountId,
            double amount
    );

    String transfer(
            String userIdFrom,
            String fromAccountId,
            String toAccountId,
            double amount
    );

    String transfer(
            String actorUserId,
            String fromAccountOwnerId,
            String fromAccountId,
            String toAccountId,
            double amount
    );

    List<String[]> displayAllTransactions(
            String actorId,
            String customerId,
            String accountId
    );
}
