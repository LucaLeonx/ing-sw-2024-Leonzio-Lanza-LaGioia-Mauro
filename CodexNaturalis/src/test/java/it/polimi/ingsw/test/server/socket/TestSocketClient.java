package it.polimi.ingsw.test.server.socket;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.controller.clientcontroller.SocketClientController;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;
import it.polimi.ingsw.dataobject.MessageType;
import it.polimi.ingsw.controller.socket.SocketClient;

import java.util.ArrayList;
import java.util.Scanner;

public class TestSocketClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient(7000);

        try{
            Message msg = new Message(MessageType.LOGIN,null,"user");
            //client.startClientConnection();

            ClientController skClientController = new SocketClientController();

            System.out.println("Type 'send' for send an info, Type 'quit' for close the client ");
            Scanner scanner = new Scanner(System.in);
            while(true) {
                String line = scanner.nextLine();
                if(line.equals("quit")) {break; }
                else if (line.equals("send")) {
                    //client.sendMessage((Object) msg);
                    skClientController.login("Luca",145829);

                }
            }
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}
