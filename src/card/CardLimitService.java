package card;

import general.Constants;
import repositories.TransactionRepository;

import java.util.Optional;

public class CardLimitService implements ICardLimitService {

    private final CardService cardService;
    private final TransactionRepository transactionRepository;

    public CardLimitService(
            CardService cardService,
            TransactionRepository transactionRepository
    ) {
        this.cardService = cardService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public boolean canWithdraw(
            String accountId,
            double amount
    ) {
        Optional<DebitCard> card =
                cardService.getCardByAccountId(accountId);

        if (card.isEmpty()) {
            return false;
        }

        double todayTotal = transactionRepository.getTodayTotal(
                accountId,
                Constants.TransactionType.WITHDRAW.name()
        );

        return todayTotal + amount
                <= card.get().getDailyWithdrawLimit();
    }

    @Override
    public boolean canDeposit(
            String accountId,
            double amount,
            boolean ownAccount
    ) {
        Optional<DebitCard> card =
                cardService.getCardByAccountId(accountId);

        if (card.isEmpty()) {
            return false;
        }

        double todayTotal = transactionRepository.getTodayTotal(
                accountId,
                Constants.TransactionType.DEPOSIT.name()
        );

        double limit = ownAccount
                ? card.get().getDailyOwnDepositLimit()
                : card.get().getDailyDepositLimit();

        return todayTotal + amount <= limit;
    }

    @Override
    public boolean canTransfer(
            String accountId,
            double amount,
            boolean ownAccount
    ) {
        Optional<DebitCard> card =
                cardService.getCardByAccountId(accountId);

        if (card.isEmpty()) {
            return false;
        }

        double todayTotal = transactionRepository.getTodayTotal(
                accountId,
                Constants.TransactionType.TRANSFER_OUT.name()
        );

        double limit = ownAccount
                ? card.get().getDailyOwnTransferLimit()
                : card.get().getDailyTransferLimit();

        return todayTotal + amount <= limit;
    }
}
