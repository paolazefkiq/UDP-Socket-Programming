package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class UDPClient {

    private static final String SERVER_IP = "127.0.0.1"; // nese serveri eshte ne pajisje tjeter, ndryshoje me IP reale
    private static final int SERVER_PORT = 5051;
    private static final int BUFFER_SIZE = 2048;

    public static void main(String[] args) {
        UDPClient client = new UDPClient();
        client.start();
    }

    public void start() {
        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            socket.setSoTimeout(5000);
            InetAddress serverAddress = InetAddress.getByName(SERVER_IP);

            System.out.println("UDP Client is connected to server " + SERVER_IP + ":" + SERVER_PORT);
            System.out.println("Shkruaj mesazh per serverin.");
            System.out.println("Komanda:");
            System.out.println("  /ping");
            System.out.println("  /clients");
            System.out.println("  exit");

            while (true) {
                System.out.print("You: ");
                String message = scanner.nextLine().trim();

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Client closed.");
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }

    private void sendMessage(DatagramSocket socket, InetAddress serverAddress, String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, SERVER_PORT);
        socket.send(packet);
    }
}
