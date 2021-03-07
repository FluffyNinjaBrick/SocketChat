package chat.Client;

import chat.Message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * A thread which listens for UDP messages sent from the server. It also handles sending messages to the server.
 */
public class UDPHandlerThread extends Thread {

    private final DatagramSocket datagramSocket;
    private final InetAddress localhost = InetAddress.getLocalHost();
    private final int serverPort;
    private final int bufferSize;


    public UDPHandlerThread(DatagramSocket datagramSocket, int serverPort, int bufferSize) throws UnknownHostException {
        this.datagramSocket = datagramSocket;
        this.serverPort = serverPort;
        this.bufferSize = bufferSize;
    }


    // method for sending UDP messages to the server, independent of run()
    public void send(Message message) {

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream(bufferSize);
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(message);
            byte[] data = baos.toByteArray();

            DatagramPacket packet = new DatagramPacket(data, data.length, localhost, serverPort);
            datagramSocket.send(packet);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // main thread loop for listening for incoming UDP messages and displaying them
    @Override
    public void run() {

        byte[] buffer = new byte[bufferSize];

        try {

            while (true) {

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(packet);

                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Message message = (Message) ois.readObject();

                System.out.println(message.toString());
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
