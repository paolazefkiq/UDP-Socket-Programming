package server;

public class ClientSession {
    private final String key;
    private final String ipAddress;
    private final int port;
    private long lastSeen;
    private boolean active;

    public ClientSession(String key, String ipAddress, int port) {
        this.key = key;
        this.ipAddress = ipAddress;
        this.port = port;
        this.lastSeen = System.currentTimeMillis();
        this.active = true;
    }
    public String getKey() {
    return key;
}

public String getIpAddress() {
    return ipAddress;
}

public int getPort() {
    return port;
}

public long getLastSeen() {
    return lastSeen;
}
    public void updateLastSeen() {
    this.lastSeen = System.currentTimeMillis();
    this.active = true;
}
}
