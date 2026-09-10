package transaction;

public abstract class TransactionService {

    public abstract String deposit(String userId, String accountId, double amount);

    public abstract String withdraw(String userId, String accountId, double amount);

    public abstract String transfer(
            String userId,
            String fromAccountId,
            String toAccountId,
            double amount
    );
}


// Account Transactions
//
//Withdraw Money (requires login)
//From savings or checking accounts.
//Deposit Money (requires login)
//Into savings or checking accounts.
//Transfer Money (requires login)
//Between a customer's own accounts or to another customer's account.
