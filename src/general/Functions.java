package general;

import java.util.UUID;
import java.time.LocalDateTime;

public class Functions {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getNow() {
        return LocalDateTime.now().toString();
    }
}
