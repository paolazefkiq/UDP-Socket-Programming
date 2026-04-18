package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class StatsHttpServer {

    private final StatsStore statsStore;
    private final int port;
    private HttpServer httpServer;

    public StatsHttpServer(StatsStore statsStore, int port) {
        this.statsStore = statsStore;
        this.port = port;
    }

    public void start() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);

            httpServer.createContext("/stats", this::handleStatsRequest);

            httpServer.setExecutor(null);
            httpServer.start();

            System.out.println("HTTP Stats Server is running on port " + port);
            System.out.println("Open: http://localhost:" + port + "/stats");

        } catch (IOException e) {
            System.out.println("Gabim gjate nisjes se HTTP serverit: " + e.getMessage());
        }
    }

    private void handleStatsRequest(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (!method.equalsIgnoreCase("GET")) {
            String response = "Method Not Allowed";
            exchange.sendResponseHeaders(405, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
            return;
        }

        String response = statsStore.toJson();

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
