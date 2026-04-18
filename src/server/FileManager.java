package server;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager {

    private static final String BASE_FOLDER = "server_files";

    public FileManager() throws Exception {
        Path path = Paths.get(BASE_FOLDER);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    public String listFiles() throws Exception {
        try (Stream<Path> stream = Files.list(Paths.get(BASE_FOLDER))) {
            return stream
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.joining(", "));
        }
    }

    public String readFile(String filename) throws Exception {
        Path path = PathUtil.safePath(filename);
        return Files.readString(path);
    }

    public String deleteFile(String filename) throws Exception {
        Path path = PathUtil.safePath(filename);
        Files.delete(path);
        return "Deleted";
    }
}