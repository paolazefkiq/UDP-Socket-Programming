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
