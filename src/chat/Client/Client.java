package chat.Client;

import chat.Message;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {

        // generic
        String clientName;
        int serverPort = 9008;
        int bufferSize = 1024;
        Mode mode = Mode.TCP;

        // sockets
        Socket tcpSocket = null;
        DatagramSocket udpSocket;

        // ask user for name
        Scanner scanner = new Scanner(System.in);
        System.out.print("Input username: ");
        clientName = scanner.nextLine();

        // run main program
        try {

            // TCP
            tcpSocket = new Socket("localhost", serverPort);
            TCPHandlerThread tcpThread = new TCPHandlerThread(tcpSocket);
            tcpThread.start();

            // UDP
            udpSocket = new DatagramSocket(tcpSocket.getLocalPort());
            UDPHandlerThread udpThread = new UDPHandlerThread(udpSocket, serverPort, bufferSize);
            udpThread.start();

            System.out.println("All set, chat is go");
            System.out.println("--------------------------");

            // listening loop
            while(true) {

                // get message
                String text = scanner.nextLine();

                // toggle mode if requested
                if (text.equals("U")) mode = Mode.UDP;
                else if (text.equals("T")) mode = Mode.TCP;

                // if not, send message
                else {
                    Message message = new Message(text, clientName);
                    switch (mode) {
                        case TCP:
                            tcpThread.send(message);
                            break;
                        case UDP:
                            udpThread.send(message);
                            break;
                        default:
                            throw new IllegalStateException("Unrecognized mode");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tcpSocket != null) tcpSocket.close();
        }

    }

}