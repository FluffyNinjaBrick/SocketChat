package chat.Server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    public static void main(String[] args) throws IOException {

        // --------- SETUP ---------
        // generic
        final int serverPort = 9008;

        // TCP
        final CopyOnWriteArrayList<TCPHandlerThread> tcpHandlerThreads = new CopyOnWriteArrayList<>();
        ServerSocket tcpSocket = null;

        // UDP
        DatagramSocket udpSocket = null;

        try {
            // --------- RUN ---------

            System.out.println("Server: warming up...");

            // TCP
            tcpSocket = new ServerSocket(serverPort);
            TCPListenerThread tcpListenerThread = new TCPListenerThread(tcpHandlerThreads, tcpSocket);
            tcpListenerThread.start();

            // UDP
            udpSocket = new DatagramSocket(serverPort);
            InetAddress localhost = InetAddress.getLocalHost();
            UDPThread udpThread = new UDPThread(tcpHandlerThreads, udpSocket, localhost);
            udpThread.start();

            while(true) {}

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tcpSocket != null) tcpSocket.close();
        }

    }

}
