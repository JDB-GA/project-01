package common;

import java.util.List;
import java.util.Scanner;

public interface ICommonService {
    String chooseFromUsernames(Scanner scanner, String message);

    String chooseFromAccounts(Scanner scanner, String customerId, String message);

    void printTransactions(List<String[]> transactions);
}
