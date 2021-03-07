package chat.Client;

import chat.Message;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {

        String name;
        int serverPort = 9008;
        Socket tcpSocket = null;
        DatagramSocket udpSocket = null;
        Mode mode = Mode.TCP;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Input username: ");
        name = scanner.nextLine();

        try {
            // TCP
            tcpSocket = new Socket("localhost", serverPort);
            TCPHandlerThread tcpThread = new TCPHandlerThread(tcpSocket);
            tcpThread.start();

            // UDP
            udpSocket = new DatagramSocket(tcpSocket.getLocalPort());
            UDPHandlerThread udpThread = new UDPHandlerThread(udpSocket, serverPort);
            udpThread.start();

            System.out.println("All set, chat is go");
            System.out.println("--------------------------");

            // listening loop
            while(true) {
                String text = scanner.nextLine();

                if (text.equals("U")) mode = Mode.UDP;
                else if (text.equals("T")) mode = Mode.TCP;

                else {
                    Message message = new Message(text, name);
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