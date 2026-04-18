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
    private static final int BUFFER_SIZE = 2048;

    // ruan klientet aktiv
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

                    handlePacket(packet);

                } catch (SocketTimeoutException e) {
                    continue;
                }
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    private void handlePacket(DatagramPacket packet) {

        String clientIp = packet.getAddress().getHostAddress();
        int clientPort = packet.getPort();
        String clientKey = clientIp + ":" + clientPort;

        String message = new String(packet.getData(), 0, packet.getLength()).trim();

        // kontrollo a ekziston klienti
        ClientSession session = clients.get(clientKey);

        if (session == null) {
            session = new ClientSession(clientKey, clientIp, clientPort);
            clients.put(clientKey, session);

            System.out.println("New client added: " + clientKey);
        } else {
            session.updateLastSeen();
        }

        System.out.println("Message from " + clientKey + " -> " + message);
    }
}