package general;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class FileControl {
    // Note for me: throws IOException means handle exception when you call it
    public static void write(Path path, List<String> lines) throws IOException {
        Files.write(path, lines);
    }

    public static void append(Path path, List<String> lines) throws IOException {
        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public static List<String> read(Path path) throws IOException {
        return Files.readAllLines(path);
    }

}
