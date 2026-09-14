package common;

import java.util.List;
import java.util.Scanner;

import general.Constants;
import transaction.ITransactionService;

public interface ICommonService {
    String chooseFromUsernames(Scanner scanner, String message);

    String chooseFromAccounts(Scanner scanner, String customerId, String message);

    void printTransactions(List<String[]> transactions);

    void displayTransactions(
            Scanner scanner,
            String[] actor,
            ITransactionService transactionService,
            boolean isCustomer
    );

    Constants.CardType chooseCardType(
            Scanner scanner,
            String message
    );
}
