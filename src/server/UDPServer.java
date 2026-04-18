package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UDPServer {

    private static final String SERVER_IP = "0.0.0.0";
    private static final int SERVER_PORT = 5051;
    private static final int MAX_CLIENTS = 10;
    private static final int BUFFER_SIZE = 2048;

    private final Map<String, ClientSession> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        UDPServer server = new UDPServer();
        server.start();
    }

    public void start() {
        try (DatagramSocket socket = new DatagramSocket(SERVER_PORT, InetAddress.getByName(SERVER_IP))) {
            socket.setSoTimeout(2000);

            System.out.println("UDP Server is running on port " + SERVER_PORT);

            while (true) {
                try {
                    byte[] buffer = new byte[BUFFER_SIZE];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    socket.receive(packet);

                    handlePacket(socket, packet);

                } catch (SocketTimeoutException e) {
                    continue;
                }
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private void handlePacket(DatagramSocket socket, DatagramPacket packet) throws IOException {
        String clientIp = packet.getAddress().getHostAddress();
        int clientPort = packet.getPort();
        String clientKey = clientIp + ":" + clientPort;

        String message = new String(packet.getData(), 0, packet.getLength()).trim();

        ClientSession session = clients.get(clientKey);

        if (session == null) {
            long activeClients = clients.values().stream()
                    .filter(ClientSession::isActive)
                    .count();

            if (activeClients >= MAX_CLIENTS) {
                sendResponse(socket, packet.getAddress(), clientPort,
                        "SERVER_FULL: Serveri ka arritur limitin e klienteve.");
                return;
            }

            session = new ClientSession(clientKey, clientIp, clientPort);
            clients.put(clientKey, session);

            System.out.println("New client added: " + clientKey);
        } else {
            session.updateLastSeen();
        }

        System.out.println("Message from " + clientKey + " -> " + message);

        String response = processMessage(message);

        sendResponse(socket, packet.getAddress(), clientPort, response);
    }

    private String processMessage(String message) {

        if (message.equalsIgnoreCase("/ping")) {
            return "PONG";
        }

        if (message.equalsIgnoreCase("/clients")) {
            return "Active clients: " + getActiveClientsCount();
        }

        return "Server received: " + message;
    }

    private long getActiveClientsCount() {
        return clients.values().stream()
                .filter(ClientSession::isActive)
                .count();
    }

    private void sendResponse(DatagramSocket socket, InetAddress address, int port, String response)
            throws IOException {
        byte[] responseBytes = response.getBytes();
        DatagramPacket responsePacket =
                new DatagramPacket(responseBytes, responseBytes.length, address, port);
        socket.send(responsePacket);
    }
}