package card;

import general.Constants;

import java.util.Optional;

public interface ICardService {

    String assignCard(
            String accountId,
            Constants.CardType cardType
    );

    Optional<DebitCard> getCardByAccountId(String accountId);
}