package card;

public interface ICardLimitService {

    boolean canWithdraw(
            String accountId,
            double amount
    );

    boolean canDeposit(
            String accountId,
            double amount,
            boolean ownAccount
    );

    boolean canTransfer(
            String accountId,
            double amount,
            boolean ownAccount
    );
}