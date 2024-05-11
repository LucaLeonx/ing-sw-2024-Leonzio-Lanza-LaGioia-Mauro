package it.polimi.ingsw.networking.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    public int port;

    public SocketServer(int port) {
        this.port = port;
    }

    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Server ready to listen from Socket");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                executor.submit(new SocketMultiHandler(socket));
            } catch(IOException e) {
                break;
            }
        }
        executor.shutdown();
    }
}
