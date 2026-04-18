package server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager {

    private static final String BASE_FOLDER = "server_files";

    public FileManager() {
        createBaseFolderIfNeeded();
    }

    private void createBaseFolderIfNeeded() {
        try {
            Path basePath = Paths.get(BASE_FOLDER);
            if (!Files.exists(basePath)) {
                Files.createDirectories(basePath);
            }
        } catch (IOException e) {
            System.out.println("Gabim gjate krijimit te folderit server_files: " + e.getMessage());
        }
    }

    public String handleCommand(String message) {
        try {
            if (message.equalsIgnoreCase("/list")) {
                return listFiles();
            }

            if (message.startsWith("/read ")) {
                String filename = message.substring(6).trim();
                return readFile(filename);
            }

            return null;

        } catch (IOException e) {
            return "Gabim gjate ekzekutimit te komandes: " + e.getMessage();
        }
    }

    private String listFiles() throws IOException {
        Path basePath = Paths.get(BASE_FOLDER);

        try (Stream<Path> stream = Files.list(basePath)) {
            List<String> files = stream
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.toList());

            if (files.isEmpty()) {
                return "Nuk ka file ne server.";
            }

            return "File-at ne server: " + String.join(", ", files);
        }
    }

    private String readFile(String filename) throws IOException {
        Path path = Paths.get(BASE_FOLDER, filename);

        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            return "File nuk u gjet: " + filename;
        }

        String content = Files.readString(path);
        return "Permbajtja e file-it " + filename + ":\n" + content;
    }
}