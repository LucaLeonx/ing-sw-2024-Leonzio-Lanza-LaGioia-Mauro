package it.polimi.ingsw.networking.socket;

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
            InputStream in = socket.getInputStream();
            //PrintWriter out = new PrintWriter(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(in);
            Message msg = (Message) ois.readObject();

            System.out.println("Object received:\n\t"+ msg.getObj());

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
