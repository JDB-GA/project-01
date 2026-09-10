package general;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

public class Functions {
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getNow() {
        return LocalDateTime.now().toString();
    }

    public static List<String[]> getTable(Path tablePath) {
        try {
            return FileControl.read(tablePath)
                    .stream()
                    .skip(1)
                    .map(row -> row.split(","))
                    .toList();

        } catch (Exception e) {
            AppLogger.error(Constants.TABLE_READ_ERROR, e);

            return null;
        }
    }

    public static void addRow(Path tablePath, List<String> row) {
        try {
            FileControl.append(tablePath, row);
        } catch (Exception e) {
            AppLogger.error(Constants.TABLE_APPEND_ERROR, e);
        }
    }

    public static boolean updateTableField(Path tablePath,
                                           String tableHeader,
                                           int identifierIndex,
                                           String identifier,
                                           int fieldIndex,
                                           String newValue) {
        try {
            List<String[]> rows = getTable(tablePath);

            if (rows == null) {
                return false;
            }

            List<String> updatedRows = new ArrayList<>();
            updatedRows.add(tableHeader);

            updatedRows.addAll(rows.stream()
                    .map(row -> {
                        if (row[identifierIndex].equals(identifier)) {
                            String[] updatedRow = row.clone();
                            updatedRow[fieldIndex] = newValue;

                            return String.join(",", updatedRow);
                        }

                        return String.join(",", row);
                    })
                    .toList());

            FileControl.write(tablePath, updatedRows);
            return true;
        } catch (Exception e) {
            AppLogger.error(Constants.TABLE_WRITE_ERROR, e);

            return false;
        }
    }
}
