package it.polimi.ingsw.controller.socket;

import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketClient {
    private int port;
    private Socket socket;

    public SocketClient(int port) {
        this.port = port;
    }

    public void startClientConnection() throws IOException {
        this.socket = new Socket("localhost", port);
        System.out.println("connection established");
    }

    public void sendMessage(Object obj) throws IOException {
        ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());

        try{
            socketOut.writeObject(obj);
            socketOut.flush();
            System.out.println("message sent");
        }catch(NoSuchElementException e) {
            System.out.println("Connection closed");
        } finally {
            socketOut.close();
            this.socket.close();
        }
    }

    public Message receiveMessage(Object obj) throws IOException, ClassNotFoundException {
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        return (Message) socketIn.readObject();
    }
}
