package chat.Server;

import chat.Message;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class TCPHandlerThread extends Thread {

    private final Socket clientSocket;
    private final CopyOnWriteArrayList<TCPHandlerThread> handlerThreads;
    private final ObjectOutputStream objectOutputStream;
    private final int clientPort;

    public TCPHandlerThread(Socket clientSocket, CopyOnWriteArrayList<TCPHandlerThread> handlerThreads) throws IOException {
        this.clientSocket = clientSocket;
        this.clientPort = clientSocket.getPort();
        this.objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        this.handlerThreads = handlerThreads;
    }

    // publicly accessible thread for other threads to share their messages through
    public void send(Message message) {
        try {
            this.objectOutputStream.writeObject(message);
        } catch (IOException e) {
            System.out.println("HandlerThread: socket disconnected - " + clientSocket);
        }
    }

    // perpetually active run method which listens for incoming messages and shares them with other threads
    @Override
    public void run() {
        try {

            InputStream inputStream = clientSocket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            while(true) {
                Message message = (Message) objectInputStream.readObject();
                for (TCPHandlerThread t: handlerThreads) if (t != this) t.send(message);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("HandlerThread: socket disconnected - " + clientSocket);
        } finally {
            System.out.println("HandlerThread: removing self from thread pool");
            this.handlerThreads.remove(this);
        }
    }

    // getter for client port which UDP needs
    public int getClientPort() {
        return this.clientPort;
    }
}
