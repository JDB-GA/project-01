package customer;

import transaction.TransactionService;
import general.Constants;

public class CustomerService extends TransactionService {

    @Override
    public String withdraw() {
        return Constants.EMPTY_STRING;
    }

    @Override
    public String deposit() {
        return Constants.EMPTY_STRING;
    }

    @Override
    public String transfer(String accountOneId, String accountTwoId) {
        return Constants.EMPTY_STRING;
    }
}
