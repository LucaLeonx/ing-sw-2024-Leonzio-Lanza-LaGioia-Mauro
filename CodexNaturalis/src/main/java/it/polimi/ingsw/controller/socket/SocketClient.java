package it.polimi.ingsw.controller.socket;

import it.polimi.ingsw.controller.clientcontroller.ConnectionDefaultSettings;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;
import it.polimi.ingsw.dataobject.MessageType;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketClient {
    private int port;
    private Socket socket;
    private ObjectOutputStream socketOut;
    private ObjectInputStream socketIn;

    public SocketClient(int port) {
        this.port = port;
    }

    public void startClientConnection() throws IOException {
        this.socket = new Socket(ConnectionDefaultSettings.RMIRegistryHost, port);
        System.out.println("connection established");
        socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketIn = new ObjectInputStream(socket.getInputStream());
    }

    public void sendMessage(Object obj){
        try{
            socketOut.writeObject(obj);
            socketOut.flush();
            //System.out.println("message sent");
        }catch(NoSuchElementException | IOException e) {
            System.out.println("Connection closed "+e.getMessage());
        }
    }

    public Message receiveMessage()  {
        try {
            return (Message) socketIn.readObject();
        }
        catch(IOException | ClassNotFoundException e) {
            return new Message(null,null,e);
        }
    }

    public void closeConnection() throws IOException {
        socketIn.close();
        socketOut.close();
        this.socket.close();
    }
}
