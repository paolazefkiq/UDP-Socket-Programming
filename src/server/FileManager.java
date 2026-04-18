package server;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager {

    private static final String BASE_FOLDER = "server_files";
    private static final String ADMIN_SECRET = "admin123";

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
        boolean isAdmin = false;
        String actualCommand = message;

        // Formati per admin:
        // ADMIN|admin123|/delete test.txt
        if (message.startsWith("ADMIN|")) {
            String[] parts = message.split("\\|", 3);
            if (parts.length < 3) {
                return "Format i gabuar i komandes admin.";
            }

            String secret = parts[1];
            actualCommand = parts[2];

            if (!ADMIN_SECRET.equals(secret)) {
                return "Gabim: admin secret nuk eshte i sakte.";
            }

            isAdmin = true;
        }

        try {
            if (actualCommand.equalsIgnoreCase("/list")) {
                return listFiles();
            }

            if (actualCommand.startsWith("/read ")) {
                String filename = actualCommand.substring(6).trim();
                return readFile(filename);
            }

            if (actualCommand.startsWith("/download ")) {
                String filename = actualCommand.substring(10).trim();
                return downloadFile(filename);
            }

            if (actualCommand.startsWith("/search ")) {
                String keyword = actualCommand.substring(8).trim();
                return searchFiles(keyword);
            }

            if (actualCommand.startsWith("/info ")) {
                String filename = actualCommand.substring(6).trim();
                return fileInfo(filename);
            }

            // Komandat poshte lejohen vetem per admin
            if (actualCommand.startsWith("/upload ")) {
                if (!isAdmin) {
                    return "Access denied. Vetem admin mund te beje upload.";
                }
                return uploadFile(actualCommand.substring(8).trim());
            }

            if (actualCommand.startsWith("/delete ")) {
                if (!isAdmin) {
                    return "Access denied. Vetem admin mund te fshije file.";
                }
                String filename = actualCommand.substring(8).trim();
                return deleteFile(filename);
            }

            return null; // nuk eshte komandë e file manager-it

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
        Path path = resolveSafePath(filename);

        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            return "File nuk u gjet: " + filename;
        }

        String content = Files.readString(path);
        return "Permbajtja e file-it " + filename + ":\n" + content;
    }

    private String downloadFile(String filename) throws IOException {
        Path path = resolveSafePath(filename);

        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            return "File nuk u gjet: " + filename;
        }

        String content = Files.readString(path);
        return "DOWNLOAD " + filename + ":\n" + content;
    }

    private String uploadFile(String uploadData) throws IOException {
        // Formati:
        // /upload test.txt|kjo eshte permbajtja
        String[] parts = uploadData.split("\\|", 2);

        if (parts.length < 2) {
            return "Format i gabuar. Perdore: /upload filename|permbajtja";
        }

        String filename = parts[0].trim();
        String content = parts[1];

        Path path = resolveSafePath(filename);
        Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return "File u ngarkua me sukses: " + filename;
    }

    private String deleteFile(String filename) throws IOException {
        Path path = resolveSafePath(filename);

        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            return "File nuk u gjet: " + filename;
        }

        Files.delete(path);
        return "File u fshi me sukses: " + filename;
    }

    private String searchFiles(String keyword) throws IOException {
        Path basePath = Paths.get(BASE_FOLDER);
        List<String> matched = new ArrayList<>();

        try (Stream<Path> stream = Files.list(basePath)) {
            stream.filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .filter(name -> name.toLowerCase().contains(keyword.toLowerCase()))
                    .forEach(matched::add);
        }

        if (matched.isEmpty()) {
            return "Nuk u gjet asnje file me fjalen kyce: " + keyword;
        }

        return "File te gjetura: " + String.join(", ", matched);
    }

    private String fileInfo(String filename) throws IOException {
        Path path = resolveSafePath(filename);

        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            return "File nuk u gjet: " + filename;
        }

        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);

        return "Info per " + filename +
                "\nMadhesia: " + attrs.size() + " bytes" +
                "\nData e krijimit: " + attrs.creationTime() +
                "\nData e modifikimit: " + attrs.lastModifiedTime();
    }

    private Path resolveSafePath(String filename) throws IOException {
        Path basePath = Paths.get(BASE_FOLDER).toAbsolutePath().normalize();
        Path requested = basePath.resolve(filename).normalize();

        if (!requested.startsWith(basePath)) {
            throw new IOException("Qasje e palejuar ne path.");
        }

        return requested;
    }
}