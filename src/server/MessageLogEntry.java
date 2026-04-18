package server;

public class MessageLogEntry {
    private final String clientKey;
    private final String message;
    private final long timestamp;

    public MessageLogEntry(String clientKey, String message, long timestamp) {
        this.clientKey = clientKey;
        this.message = message;
        this.timestamp = timestamp;
    }
}
