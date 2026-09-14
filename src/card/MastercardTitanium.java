package card;

import general.Constants;

public final class MastercardTitanium extends DebitCard {

    public MastercardTitanium(
            String id,
            String accountId,
            String createdAt
    ) {
        super(
                id,
                accountId,
                Constants.CardType.MASTERCARD_TITANIUM,
                Constants.TITANIUM_WITHDRAW_LIMIT,
                Constants.TITANIUM_TRANSFER_LIMIT,
                Constants.TITANIUM_OWN_TRANSFER_LIMIT,
                Constants.CARD_DEPOSIT_LIMIT,
                Constants.CARD_OWN_DEPOSIT_LIMIT,
                createdAt
        );
    }
}