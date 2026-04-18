package server;

public class StatsHttpServer {
    private final StatsStore statsStore;
    private final int port;
    private HttpServer httpServer;

    public StatsHttpServer(StatsStore statsStore, int port) {
        this.statsStore = statsStore;
        this.port = port;
    }
}
public void start() {
    try {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);

        httpServer.setExecutor(null);
        httpServer.start();

        System.out.println("HTTP Stats Server is running on port  " + port);

    } catch (IOException e) {
        System.out.println("Gabim gjate nisjes se HTTP serverit: " + e.getMessage());
    }
}