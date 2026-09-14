package card;

import general.Constants;

public final class MastercardPlatinum extends DebitCard {

    public MastercardPlatinum(
            String id,
            String accountId,
            String createdAt
    ) {
        super(
                id,
                accountId,
                Constants.CardType.MASTERCARD_PLATINUM,
                Constants.PLATINUM_WITHDRAW_LIMIT,
                Constants.PLATINUM_TRANSFER_LIMIT,
                Constants.PLATINUM_OWN_TRANSFER_LIMIT,
                Constants.CARD_DEPOSIT_LIMIT,
                Constants.CARD_OWN_DEPOSIT_LIMIT,
                createdAt
        );
    }
}