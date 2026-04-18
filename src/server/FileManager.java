import java.nio.file.*;
import java.util.stream.*;

public class FileService {

    private static final String BASE_FOLDER = "server_files";

    public FileService() throws Exception {
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