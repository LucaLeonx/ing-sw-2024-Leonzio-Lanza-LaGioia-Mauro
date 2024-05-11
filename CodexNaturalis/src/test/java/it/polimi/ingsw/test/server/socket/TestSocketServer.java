package it.polimi.ingsw.test.server.socket;

import it.polimi.ingsw.networking.socket.SocketServer;

public class TestSocketServer {
    public static void main(String[] args) {
        SocketServer server = new SocketServer(6660);
        server.startServer();
    }
}
