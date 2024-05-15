package it.polimi.ingsw.controller.socket;

import it.polimi.ingsw.controller.servercontroller.AuthenticationManager;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketMultiHandler implements Runnable {
    private Socket socket;
    private MessageTranslator translator;

    public SocketMultiHandler(Socket socket,MessageTranslator translator) {
        this.socket = socket;
        this.translator = translator;
    }

    public void run() {
        try {
            //PrintWriter out = new PrintWriter(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            //Here we cloud start another thread (Server side) which his purpose is to send message to the client whenever you want
            while (true) {
                Message InMsg = (Message) ois.readObject();
                System.out.println("Object received:\t"+ InMsg.getObj());

                Message OutMsg =  MessageTranslator.processMessage(InMsg);
                oos.writeObject(OutMsg);
                oos.flush();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
