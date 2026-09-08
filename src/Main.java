import auth.Auth;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Auth auth = new Auth();
//        System.out.println(auth.hashPassword("something"));
//        System.out.println(Functions.generateUUID());
        System.out.println(auth.login("muntadher", "something"));
        System.out.println(auth.login("almutawaj", "securePass"));
        String userId = "bd0c11a6-7083-47c5-8070-d978b4f07161";
        System.out.println(Arrays.toString(auth.getUserById(userId)));
        System.out.println(auth.checkRole(userId));
//        System.out.println(auth.register("muntadher", "something", "BANKER"));
    }

}
