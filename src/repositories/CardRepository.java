package repositories;

import card.DebitCard;
import general.Constants;
import general.Functions;

import java.util.List;

public class CardRepository {

    public String[] findByAccountId(String accountId) {
        List<String[]> cards =
                Functions.getTable(Constants.DEBIT_CARD_TABLE);

        if (cards == null) {
            return new String[0];
        }

        return cards.stream()
                .filter(card -> card[1].equals(accountId))
                .findFirst()
                .orElse(new String[0]);
    }

    public boolean existsForAccount(String accountId) {
        return findByAccountId(accountId).length > 0;
    }

    public void save(DebitCard card) {
        String row = String.join(
                Constants.CSV_SEPARATOR,
                card.getId(),
                card.getAccountId(),
                card.getCardType().name(),
                card.getCreatedAt()
        );

        Functions.addRow(
                Constants.DEBIT_CARD_TABLE,
                List.of(row)
        );
    }
}