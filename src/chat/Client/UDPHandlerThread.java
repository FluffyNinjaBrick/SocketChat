package chat.Client;

import chat.Message;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPHandlerThread extends Thread {

    private final DatagramSocket datagramSocket;
    private final InetAddress localhost = InetAddress.getLocalHost();
    private final int serverPort;
    private final int bufferSize = 1024;

    public UDPHandlerThread(DatagramSocket datagramSocket, int serverPort) throws UnknownHostException {
        this.datagramSocket = datagramSocket;
        this.serverPort = serverPort;
    }

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
