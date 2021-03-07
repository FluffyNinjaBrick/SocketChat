package chat.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class TCPListenerThread extends Thread {

    private final CopyOnWriteArrayList<TCPHandlerThread> tcpHandlerThreads;
    private final ServerSocket tcpSocket;

    public TCPListenerThread(CopyOnWriteArrayList<TCPHandlerThread> tcpHandlerThreads, ServerSocket tcpSocket) {
        this.tcpHandlerThreads = tcpHandlerThreads;
        this.tcpSocket = tcpSocket;
    }

    @Override
    public void run() {

        while (true) {

            System.out.println("TCPListener: accepting connections on port " + tcpSocket.getLocalPort());

            try {
                // accept incoming connection
                Socket clientSocket = tcpSocket.accept();
                System.out.println("TCPListener: accepted connection from " + clientSocket);

                // set up TCP infrastructure for it
                TCPHandlerThread newTCPThread = new TCPHandlerThread(clientSocket, tcpHandlerThreads);
                newTCPThread.start();
                tcpHandlerThreads.add(newTCPThread);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
