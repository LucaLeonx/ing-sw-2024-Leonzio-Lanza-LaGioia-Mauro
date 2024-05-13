package it.polimi.ingsw.controller.socket;

import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketMultiHandler implements Runnable {
    private Socket socket;

    public SocketMultiHandler(Socket socket) {this.socket = socket;}

    public void run() {
        try {
            //PrintWriter out = new PrintWriter(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            Message InMsg = (Message) ois.readObject();

            //Message OutMsg =  MessageTranslator.processMessage(InMsg)
            //oos.writeObject(OutMsg)
            //oos.flush()
            System.out.println("Object received:\n\t"+ InMsg.getObj());

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
