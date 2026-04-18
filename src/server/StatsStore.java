package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatsStore {

    private final Map<String, String> activeClients = new ConcurrentHashMap<>();
    private final List<MessageLogEntry> messages = new ArrayList<>();

    public synchronized void updateClient(String clientKey, String ipAddress) {
        activeClients.put(clientKey, ipAddress);
    }

    public synchronized void removeClient(String clientKey) {
        activeClients.remove(clientKey);
    }

    public synchronized void addMessage(String clientKey, String message) {
        messages.add(new MessageLogEntry(clientKey, message, System.currentTimeMillis()));
    }

    public synchronized int getActiveClientCount() {
        return activeClients.size();
    }

    public synchronized int getMessageCount() {
        return messages.size();
    }

    public synchronized String toJson() {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");
        sb.append("  \"activeClientCount\": ").append(getActiveClientCount()).append(",\n");

        sb.append("  \"activeClients\": [\n");
        int i = 0;
        for (Map.Entry<String, String> entry : activeClients.entrySet()) {
            sb.append("    { \"clientKey\": \"").append(escape(entry.getKey()))
                    .append("\", \"ip\": \"").append(escape(entry.getValue())).append("\" }");
            if (i < activeClients.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
            i++;
        }
        sb.append("  ],\n");

        sb.append("  \"messageCount\": ").append(getMessageCount()).append(",\n");

        sb.append("  \"messages\": [\n");
        for (int j = 0; j < messages.size(); j++) {
            MessageLogEntry msg = messages.get(j);
            sb.append("    { ");
            sb.append("\"clientKey\": \"").append(escape(msg.getClientKey())).append("\", ");
            sb.append("\"message\": \"").append(escape(msg.getMessage())).append("\", ");
            sb.append("\"timestamp\": ").append(msg.getTimestamp());
            sb.append(" }");

            if (j < messages.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append("  ]\n");
        sb.append("}");

        return sb.toString();
    }

    private String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}