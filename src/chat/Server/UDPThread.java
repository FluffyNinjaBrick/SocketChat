package chat.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.CopyOnWriteArrayList;

public class UDPThread extends Thread {

    private final CopyOnWriteArrayList<TCPHandlerThread> handlerThreads;
    private final DatagramSocket udpSocket;
    private final InetAddress localhost;

    public UDPThread(CopyOnWriteArrayList<TCPHandlerThread> handlerThreads, DatagramSocket udpSocket, InetAddress localhost) {
        this.handlerThreads = handlerThreads;
        this.udpSocket = udpSocket;
        this.localhost = localhost;
    }

    @Override
    public void run() {

        byte[] buffer = new byte[1024];
        System.out.println("UDPListener: listening for UDP on port " + udpSocket.getLocalPort());

        while(true) {
            try {

                // receive
                DatagramPacket recvPacket = new DatagramPacket(buffer, buffer.length);
                udpSocket.receive(recvPacket);
                byte[] sendBuffer = recvPacket.getData();
                int senderPort = recvPacket.getPort();

                // forward
                for (TCPHandlerThread t: handlerThreads) {
                    if (t.getClientPort() == senderPort) continue;
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, localhost, t.getClientPort());
                    udpSocket.send(sendPacket);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
