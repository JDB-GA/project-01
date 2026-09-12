package transaction;

import auth.AuthService;
import general.Constants;
import repositories.AccountRepository;
import repositories.TransactionRepository;
import common.CommonService;

import java.util.List;

public class TransactionService extends CommonService implements ITransactionService {

    protected final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository, AuthService authService) {
        super(authService, accountRepository);
        this.transactionRepository = transactionRepository;
    }

    @Override
    public boolean deposit(String userId, String accountId, double amount) {
        return deposit(userId, userId, accountId, amount);
    }

    @Override
    public boolean deposit(String actorUserId, String accountOwnerId, String accountId, double amount) {
        if (isNotAuthorized(actorUserId, accountOwnerId)) return false;
        if (invalidAmount(amount)) return false;

        String[] account = accountRepository.getUserAccount(accountId);
        double currentBalance = Double.parseDouble(account[3]);
        double newBalance = currentBalance + amount;

        commonDepositCheck(newBalance, account);

        accountRepository.updateBalance(accountId, String.valueOf(newBalance));
        transactionRepository.save(
                accountId,
                Constants.TransactionType.DEPOSIT.name(),
                amount,
                Constants.EMPTY_STRING,
                newBalance
        );

        return true;
    }

    @Override
    public boolean withdraw(String userId, String accountId, double amount) {

        return withdraw(userId, userId, accountId, amount);
    }

    @Override
    public boolean withdraw(String actorUserId, String accountOwnerId, String accountId, double amount) {
        if (invalidAmount(amount)) return false;
        if (isNotAuthorized(actorUserId, accountOwnerId)) return false;

        String[] userAccount = accountRepository.getUserAccount(accountId);
        double currentBalance = Double.parseDouble(userAccount[3]);
        int overdraftCount = Integer.parseInt(userAccount[5]);
        if ((currentBalance < 0 && amount > 100)
                || (overdraftCount >= 2)
                || (userAccount[4].equals(Constants.AccountStatus.DISABLED.name()))
        ) {
            return false;
        } else if (currentBalance < amount) {
            currentBalance -= Constants.OVERDRAFT_PENALTY_DEFAULT;
            transactionRepository.save(accountId, Constants.TransactionType.OVERDRAFT_FEE.name(), Constants.OVERDRAFT_PENALTY_DEFAULT, Constants.EMPTY_STRING, currentBalance);
            overdraftCount++;
            accountRepository.updateOverdraftCount(accountId, overdraftCount);
            if (overdraftCount >= 2) {
                accountRepository.updateStatus(accountId, Constants.AccountStatus.DISABLED.name());
            }
        }

        double newBalance = currentBalance - amount;

        accountRepository.updateBalance(accountId, String.valueOf(newBalance));
        transactionRepository.save(accountId, Constants.TransactionType.WITHDRAW.name(), amount, Constants.EMPTY_STRING, newBalance);

        return true;
    }

    @Override
    public String transfer(String userIdFrom, String fromAccountId, String toAccountId, double amount) {
        return transfer(userIdFrom, userIdFrom, fromAccountId, toAccountId, amount);
    }

    @Override
    public String transfer(
            String actorUserId,
            String fromAccountOwnerId,
            String fromAccountId,
            String toAccountId,
            double amount
    ) {
        if (isNotAuthorized(actorUserId, fromAccountOwnerId)) return Constants.GENERAL_ERROR;

        if (withdraw(actorUserId, fromAccountOwnerId, fromAccountId, amount)
                && depositToAccount(toAccountId, amount)) {
            return Constants.BALANCE_UPDATE_SUCCESS;
        }

        return Constants.GENERAL_ERROR;
    }

    private boolean isNotAuthorized(String actorUserId, String accountOwnerId) {
        if (!authService.authState(actorUserId)) {
            return true;
        }

        boolean isAccountOwner = actorUserId.equals(accountOwnerId);
        boolean isBanker = authService.checkRole(
                actorUserId,
                Constants.UserRole.BANKER.name()
        );

        return !isAccountOwner && !isBanker;
    }

    private boolean depositToAccount(String accountId, double amount) {
        if (invalidAmount(amount)) return false;
        String[] account = accountRepository.getUserAccount(accountId);
        if (account.length == 0) return false;

        double newBalance = Double.parseDouble(account[3]) + amount;

        commonDepositCheck(newBalance, account);

        accountRepository.updateBalance(accountId, String.valueOf(newBalance));
        transactionRepository.save(accountId, Constants.TransactionType.DEPOSIT.name(), amount, newBalance);

        return true;
    }

    private void commonDepositCheck(double newBalance, String[] account) {
        if (newBalance >= 0 && account[4].equals(Constants.AccountStatus.DISABLED.name())) {
            accountRepository.updateStatus(account[0], Constants.AccountStatus.ACTIVE.name());
            accountRepository.updateOverdraftCount(
                    account[0],
                    Constants.OVERDRAFT_COUNT_DEFAULT
            );
        }
    }

    private boolean invalidAmount(double amount) {
        return !(amount > 0);
    }

    @Override
    public List<String[]> displayAllTransactions(
            String actorId,
            String customerId,
            String accountId
    ) {
        if (isNotAuthorized(actorId, customerId)) {
            return List.of();
        }

        String[] account = accountRepository.getUserAccount(accountId);

        if (account.length == 0 || !account[1].equals(customerId)) {
            return List.of();
        }

        return transactionRepository.getTransactionTable()
                .stream()
                .filter(transaction -> transaction[1].equals(accountId))
                .toList();
    }

}
