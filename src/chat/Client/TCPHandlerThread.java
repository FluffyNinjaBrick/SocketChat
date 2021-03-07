package chat.Client;

import chat.Message;

import java.io.*;
import java.net.Socket;

public class TCPHandlerThread extends Thread {

    private final Socket socket;
    private final ObjectOutputStream os;

    public TCPHandlerThread(Socket socket) throws IOException {
        this.socket = socket;
        this.os = new ObjectOutputStream(socket.getOutputStream());
    }

    public void send(Message message) {
        try {
            os.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            while(true) {
                Message message = (Message) objectInputStream.readObject();
                System.out.println(message.toString());
            }

        } catch (IOException e) {
            System.out.println("----- Connection closed by server; chat disabled -----");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
