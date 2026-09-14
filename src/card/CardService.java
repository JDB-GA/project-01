package card;

import general.Constants;
import general.Functions;
import repositories.AccountRepository;
import repositories.CardRepository;

import java.util.Optional;

public class CardService implements ICardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    public CardService(
            CardRepository cardRepository,
            AccountRepository accountRepository
    ) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public String assignCard(
            String accountId,
            Constants.CardType cardType
    ) {
        if (accountRepository.getUserAccount(accountId).length == 0) {
            return Constants.ACCOUNT_NOT_FOUND;
        }

        if (cardRepository.existsForAccount(accountId)) {
            return Constants.CARD_ALREADY_EXISTS;
        }

        DebitCard card = createCard(
                Functions.generateUUID(),
                accountId,
                cardType,
                Functions.getNow()
        );

        cardRepository.save(card);

        return Constants.CARD_CREATED_SUCCESSFULLY;
    }

    @Override
    public Optional<DebitCard> getCardByAccountId(String accountId) {
        String[] record = cardRepository.findByAccountId(accountId);

        if (record.length == 0) {
            return Optional.empty();
        }

        Constants.CardType cardType =
                Constants.CardType.valueOf(record[2]);

        return Optional.of(
                createCard(
                        record[0],
                        record[1],
                        cardType,
                        record[3]
                )
        );
    }

    private DebitCard createCard(
            String id,
            String accountId,
            Constants.CardType cardType,
            String createdAt
    ) {
        return switch (cardType) {
            case MASTERCARD -> new Mastercard(id, accountId, createdAt);

            case MASTERCARD_TITANIUM -> new MastercardTitanium(id, accountId, createdAt);

            case MASTERCARD_PLATINUM -> new MastercardPlatinum(id, accountId, createdAt);
        };
    }
}