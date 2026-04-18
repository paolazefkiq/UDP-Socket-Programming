public class FileManager {

    private CommandHandler commandHandler;

    public FileManager() {
        commandHandler = new CommandHandler();
    }

    public String handleCommand(String message) {
        return commandHandler.process(message);
    }
}