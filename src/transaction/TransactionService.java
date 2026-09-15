package transaction;

import java.util.List;

import auth.AuthService;
import card.CardLimitService;
import common.CommonService;
import general.Constants;
import repositories.AccountRepository;
import repositories.TransactionRepository;

public class TransactionService extends CommonService implements ITransactionService {

    protected final TransactionRepository transactionRepository;
    private final CardLimitService cardLimitService;

    public TransactionService(AccountRepository accountRepository, TransactionRepository transactionRepository, AuthService authService, CardLimitService cardLimitService) {
        super(authService, accountRepository);
        this.transactionRepository = transactionRepository;
        this.cardLimitService = cardLimitService;
    }

    @Override
    public boolean deposit(String userId, String accountId, double amount) {
        return Constants.DEPOSIT_SUCCESS.equals(
                depositWithResult(userId, userId, accountId, amount));
    }

    @Override
    public boolean deposit(String actorUserId, String accountOwnerId, String accountId, double amount) {
        return Constants.DEPOSIT_SUCCESS.equals(
                depositWithResult(actorUserId, accountOwnerId, accountId, amount));
    }

    public String depositWithResult(
            String actorUserId, String accountOwnerId, String accountId, double amount
    ) {
        if (isNotAuthorized(actorUserId, accountOwnerId)) return Constants.GENERAL_ERROR;
        if (invalidAmount(amount)) return Constants.GENERAL_ERROR;
        if (!cardLimitService.canDeposit(accountId, amount, actorUserId.equals(accountOwnerId))) {
            return Constants.DEPOSIT_LIMIT_ERROR;
        }

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

        return Constants.DEPOSIT_SUCCESS;
    }

    @Override
    public boolean withdraw(String userId, String accountId, double amount) {

        return withdraw(userId, userId, accountId, amount);
    }

    private boolean withdrawFromAccount(
            String actorUserId,
            String accountOwnerId,
            String accountId,
            double amount,
            Constants.TransactionType transactionType,
            String relatedAccountId
    ) {
        if (invalidAmount(amount)) return false;
        if (isNotAuthorized(actorUserId, accountOwnerId)) return false;
        if (transactionType == Constants.TransactionType.WITHDRAW
                && !cardLimitService.canWithdraw(accountId, amount)) {
            return false;
        }

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
        transactionRepository.save(accountId, transactionType.name(), amount, relatedAccountId, newBalance);

        return true;
    }

    @Override
    public String transfer(String userIdFrom, String fromAccountId, String toAccountId, double amount) {
        return transfer(userIdFrom, userIdFrom, fromAccountId, toAccountId, amount);
    }

    @Override
    public boolean withdraw(
            String actorUserId,
            String accountOwnerId,
            String accountId,
            double amount
    ) {
        return Constants.WITHDRAW_SUCCESS.equals(withdrawWithResult(
                actorUserId, accountOwnerId, accountId, amount));
    }

    public String withdrawWithResult(
            String actorUserId, String accountOwnerId, String accountId, double amount
    ) {
        if (invalidAmount(amount) || isNotAuthorized(actorUserId, accountOwnerId)) {
            return Constants.GENERAL_ERROR;
        }
        if (!cardLimitService.canWithdraw(accountId, amount)) {
            return Constants.WITHDRAW_LIMIT_ERROR;
        }
        boolean withdrawn = withdrawFromAccount(
                actorUserId,
                accountOwnerId,
                accountId,
                amount,
                Constants.TransactionType.WITHDRAW,
                Constants.EMPTY_STRING
        );
        return withdrawn ? Constants.WITHDRAW_SUCCESS : Constants.GENERAL_ERROR;
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

        String[] destinationAccount = accountRepository.getUserAccount(toAccountId);
        if (destinationAccount.length == 0) return Constants.GENERAL_ERROR;

        boolean ownAccount = fromAccountOwnerId.equals(destinationAccount[1]);
        if (!cardLimitService.canTransfer(fromAccountId, amount, ownAccount)) {
            return Constants.GENERAL_ERROR + " transfer Limit Reached Operation cannot be done !";
        }

        if (withdrawFromAccount(
                actorUserId,
                fromAccountOwnerId,
                fromAccountId,
                amount,
                Constants.TransactionType.TRANSFER_OUT,
                toAccountId
        ) && depositToAccount(
                toAccountId,
                amount,
                fromAccountId
        )) {
            return Constants.BALANCE_UPDATE_SUCCESS;
        }

        return Constants.GENERAL_ERROR + " transfer Limit Reached Operation cannot be done !";
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

    private boolean depositToAccount(
            String accountId,
            double amount,
            String relatedAccountId
    ) {
        if (invalidAmount(amount)) return false;
        String[] account = accountRepository.getUserAccount(accountId);
        if (account.length == 0) return false;

        double newBalance = Double.parseDouble(account[3]) + amount;

        commonDepositCheck(newBalance, account);

        accountRepository.updateBalance(accountId, String.valueOf(newBalance));
        transactionRepository.save(accountId, Constants.TransactionType.TRANSFER_IN.name(), amount, relatedAccountId, newBalance);

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
