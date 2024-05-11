package it.polimi.ingsw.test.server.socket;

import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;
import it.polimi.ingsw.dataobject.MessageType;
import it.polimi.ingsw.networking.socket.SocketClient;

import java.util.ArrayList;

public class TestSocketClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient(6660);

        try{
            Message msg = new Message(MessageType.CREATE_LOBBY,
                    new LobbyInfo(1,"Seconda Lobby di Prova","Luca",new ArrayList<>(),4,0));
            client.startClientConnection();
            client.sendMessage((Object) msg);
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}
