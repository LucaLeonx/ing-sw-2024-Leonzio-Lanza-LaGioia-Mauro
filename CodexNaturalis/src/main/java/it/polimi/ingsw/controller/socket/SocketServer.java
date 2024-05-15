package it.polimi.ingsw.controller.socket;

import it.polimi.ingsw.controller.servercontroller.AuthenticationManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    public int port;
    private AuthenticationManager authenticator;

    public SocketServer(int port, AuthenticationManager authenticator) {
        this.authenticator = authenticator;
        this.port = port;
    }

    public void startServer() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        MessageTranslator messageTranslator = new MessageTranslator(authenticator);
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
                executor.submit(new SocketMultiHandler(socket,messageTranslator));
            } catch(IOException e) {
                break;
            }
        }
        executor.shutdown();
    }
}
