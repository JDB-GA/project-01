import auth.AuthService;
import banker.BankerService;
import repositories.AccountRepository;
import repositories.AuthTrackerRepository;
import repositories.UserRepository;

public class Main {
    public static void main(String[] args) {
        AuthService auth = new AuthService(new UserRepository(), new AuthTrackerRepository());
        BankerService banker = new BankerService(new AccountRepository(), auth);
//        System.out.println(auth.hashPassword("something"));
//        System.out.println(Functions.generateUUID());
//        System.out.println(auth.login("muntadher", "something"));
//        System.out.println(auth.login("muntadher", "somethings"));
        System.out.println(auth.login("almutawaj", "somethingsk"));
        String userId = "bd0c11a6-7083-47c5-8070-d978b4f07161";
//        System.out.println(auth.logout(userId));
//        System.out.println(Arrays.toString(auth.getUserById(userId)));
//        System.out.println(auth.checkRole(userId));
//        System.out.println(auth.register("muntadher", "something", "BANKER"));
        //        System.out.println(banker.addCustomer("muntadhers", "something", Constants.AccountType.CHECKING.name()));
//        System.out.println(banker.createAccount(userId, Constants.AccountType.SAVINGS.name()));
//        System.out.println(auth.resetPassword(userId, "somethings"));
    }

}
