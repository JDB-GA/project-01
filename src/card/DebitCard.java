package card;

import general.Constants;

public abstract class DebitCard {

    private final String id;
    private final String accountId;
    private final Constants.CardType cardType;
    private final double dailyWithdrawLimit;
    private final double dailyTransferLimit;
    private final double dailyOwnTransferLimit;
    private final double dailyDepositLimit;
    private final double dailyOwnDepositLimit;
    private final String createdAt;

    protected DebitCard(
            String id,
            String accountId,
            Constants.CardType cardType,
            double dailyWithdrawLimit,
            double dailyTransferLimit,
            double dailyOwnTransferLimit,
            double dailyDepositLimit,
            double dailyOwnDepositLimit,
            String createdAt
    ) {
        this.id = id;
        this.accountId = accountId;
        this.cardType = cardType;
        this.dailyWithdrawLimit = dailyWithdrawLimit;
        this.dailyTransferLimit = dailyTransferLimit;
        this.dailyOwnTransferLimit = dailyOwnTransferLimit;
        this.dailyDepositLimit = dailyDepositLimit;
        this.dailyOwnDepositLimit = dailyOwnDepositLimit;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public Constants.CardType getCardType() {
        return cardType;
    }

    public double getDailyWithdrawLimit() {
        return dailyWithdrawLimit;
    }

    public double getDailyTransferLimit() {
        return dailyTransferLimit;
    }

    public double getDailyOwnTransferLimit() {
        return dailyOwnTransferLimit;
    }

    public double getDailyDepositLimit() {
        return dailyDepositLimit;
    }

    public double getDailyOwnDepositLimit() {
        return dailyOwnDepositLimit;
    }

    public String getCreatedAt() {
        return createdAt;
    }


}