package it.polimi.ingsw.test.server.socket;

import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;
import it.polimi.ingsw.dataobject.MessageType;
import it.polimi.ingsw.controller.socket.SocketClient;

import java.util.ArrayList;
import java.util.Scanner;

public class TestSocketClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient(6660);
        /*
        try{
            Message msg = new Message(MessageType.CREATE_LOBBY,
                    new LobbyInfo(1,"Seconda Lobby di Prova",null,"Luca",new ArrayList<>(),4,0));
            client.startClientConnection();

            System.out.println("Type 'send' for send an info, Type 'quit' for close the client ");
            Scanner scanner = new Scanner(System.in);
            while(true) {
                String line = scanner.nextLine();
                if(line.equals("quit")) {break; }
                else if (line.equals("send")) {
                    client.sendMessage((Object) msg);
                }
            }
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }*/
    }
}
