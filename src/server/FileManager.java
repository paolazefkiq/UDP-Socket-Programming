package server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
}