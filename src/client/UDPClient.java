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

    }
}
