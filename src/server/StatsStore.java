package server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatsStore {

    private final Map<String, String> activeClients = new ConcurrentHashMap<>();
    private final List<MessageLogEntry> messages = new ArrayList<>();

    // Menaxhimi i klientëve
    public synchronized void updateClient(String clientKey, String ipAddress) {
        activeClients.put(clientKey, ipAddress);
    }

    public synchronized void removeClient(String clientKey) {
        activeClients.remove(clientKey);
    }

    // Ruajtja e mesazheve
    public synchronized void addMessage(String clientKey, String message) {
        messages.add(new MessageLogEntry(clientKey, message, System.currentTimeMillis()));
    }

    // Statistikat bazë
    public synchronized int getActiveClientCount() {
        return activeClients.size();
    }

    public synchronized int getMessageCount() {
        return messages.size();
    }
}