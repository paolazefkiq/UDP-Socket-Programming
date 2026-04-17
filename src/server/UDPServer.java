package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UDPServer {

    private static final String SERVER_IP = "0.0.0.0";
    private static final int SERVER_PORT = 5051;
    private static final int BUFFER_SIZE = 2048;

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

                    System.out.println("U pranua nje pakete");

                } catch (SocketTimeoutException e) {
                    continue;
                }
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}