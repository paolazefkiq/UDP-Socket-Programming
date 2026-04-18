import java.nio.file.*;

public class PathUtil {

    private static final String BASE_FOLDER = "server_files";

    public static Path safePath(String filename) throws Exception {

        Path base = Paths.get(BASE_FOLDER).toAbsolutePath().normalize();
        Path requested = base.resolve(filename).normalize();

        if (!requested.startsWith(base)) {
            throw new Exception("Path i palejuar");
        }

        return requested;
    }
}