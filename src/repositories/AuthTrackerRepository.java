package repositories;

import general.Constants;
import general.Functions;

import java.util.List;

public class AuthTrackerRepository {

    public String[] findByUserId(String userId) {
        List<String[]> authTrackers = Functions.getTable(Constants.AUTH_TRACKER_TABLE);

        if (authTrackers == null) {
            return new String[]{};
        }

        return authTrackers.stream()
                .filter(authTracker -> authTracker[1].equals(userId))
                .findFirst()
                .orElse(new String[]{});
    }

    public void create(String userId) {
        if (findByUserId(userId).length > 0) {
            return;
        }

        String row = String.join(
                Constants.CSV_SEPARATOR,
                Functions.generateUUID(),
                userId,
                Constants.TRUE,
                Constants.ZERO,
                Constants.EMPTY_STRING,
                Functions.getNow()
        );

        Functions.addRow(Constants.AUTH_TRACKER_TABLE, List.of(row));
    }

    public void markLoggedIn(String userId) {
        update(userId, 2, Constants.TRUE);
        update(userId, 3, Constants.ZERO);
        update(userId, 4, Constants.EMPTY_STRING);
        update(userId, 5, Functions.getNow());
    }

    public boolean markLoggedOut(String userId) {
        update(userId, 2, Constants.FALSE);
        return Functions.updateTableField(
                Constants.AUTH_TRACKER_TABLE,
                Constants.AUTH_TRACKER_TABLE_HEADER,
                1,
                userId,
                5,
                Functions.getNow()
        );
    }

    public void createFailedAttempt(String userId, int failedAttempts, String lockedUntil) {
        String row = String.join(
                Constants.CSV_SEPARATOR,
                Functions.generateUUID(),
                userId,
                Constants.FALSE,
                String.valueOf(failedAttempts),
                lockedUntil,
                Functions.getNow()
        );
        Functions.addRow(Constants.AUTH_TRACKER_TABLE, List.of(row));
    }

    public void updateFailedAttempt(String userId, int failedAttempts, String lockedUntil) {
        update(userId, 2, Constants.FALSE);
        update(userId, 3, String.valueOf(failedAttempts));
        update(userId, 4, lockedUntil);
        update(userId, 5, Functions.getNow());
    }

    private void update(String userId, int fieldIndex, String value) {
        Functions.updateTableField(
                Constants.AUTH_TRACKER_TABLE,
                Constants.AUTH_TRACKER_TABLE_HEADER,
                1,
                userId,
                fieldIndex,
                value
        );
    }
}
