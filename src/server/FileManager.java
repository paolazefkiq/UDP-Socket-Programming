public class CommandHandler {

    private AdminService adminService = new AdminService();
    private FileService fileService = new FileService();

    public String process(String message) {

        boolean isAdmin = false;
        String command = message;

        if (adminService.isAdmin(message)) {
            String[] parts = message.split("\\|", 3);

            if (parts.length < 3) {
                return "Format i gabuar.";
            }

            if (!adminService.validate(parts[1])) {
                return "Admin secret gabim.";
            }

            command = parts[2];
            isAdmin = true;
        }

        return execute(command, isAdmin);
    }

    private String execute(String command, boolean isAdmin) {
        try {

            if (command.equals("/list")) {
                return fileService.listFiles();
            }

            if (command.startsWith("/read ")) {
                return fileService.readFile(command.substring(6));
            }

            if (command.startsWith("/delete ")) {
                if (!isAdmin) return "Access denied";
                return fileService.deleteFile(command.substring(8));
            }

            return "Komande e panjohur";

        } catch (Exception e) {
            return "Gabim: " + e.getMessage();
        }
    }
}