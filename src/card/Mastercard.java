package card;

import general.Constants;

public final class Mastercard extends DebitCard {

    public Mastercard(
            String id,
            String accountId,
            String createdAt
    ) {
        super(
                id,
                accountId,
                Constants.CardType.MASTERCARD,
                Constants.MASTERCARD_WITHDRAW_LIMIT,
                Constants.MASTERCARD_TRANSFER_LIMIT,
                Constants.MASTERCARD_OWN_TRANSFER_LIMIT,
                Constants.CARD_DEPOSIT_LIMIT,
                Constants.CARD_OWN_DEPOSIT_LIMIT,
                createdAt
        );
    }
}