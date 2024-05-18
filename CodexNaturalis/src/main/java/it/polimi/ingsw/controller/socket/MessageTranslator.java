package it.polimi.ingsw.controller.socket;

import it.polimi.ingsw.controller.servercontroller.AuthenticatedSession;
import it.polimi.ingsw.controller.servercontroller.AuthenticationManager;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;
import it.polimi.ingsw.dataobject.MessageType;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.dataobject.ObjectiveInfo;

import java.rmi.RemoteException;
import java.util.List;

/**
 * Server-side class.
 * Abstract class that will take the message sent from the client to the server and
 * will return the server response, this class is used only for socket connection
 */

public class MessageTranslator {

    private static AuthenticationManager manager;
    private static AuthenticatedSession session;
    //private static MessageTranslator instance;

    public MessageTranslator(AuthenticationManager manager) {
        this.manager = manager;
    }

    private static boolean isAuthenticated(){
        return session != null;
    }

    private void checkLogin(){
        if(!isAuthenticated()){
            throw new InvalidOperationException("Cannot perform this operation when not logged in");
        }
    }

    public static Message processMessage(Message message) throws RemoteException {
        switch (message.getMessageType()){
            case REGISTER_USER -> {
                if(isAuthenticated()){
                    session.logout();
                }//Do we need this if?
                int tempCode;
                try{
                    tempCode = manager.register((String) message.getObj());
                }catch (InvalidOperationException e){
                    return new Message(null,(String) "Username already exists");
                }
                return new Message(MessageType.TEMP_CODE,tempCode);
            }
            case LOGIN -> {
                List<Object> tuple = List.of(message.getObj());
                String username = (String) tuple.get(0);
                int password = (int) tuple.get(1);

                //todo
            }
            case CREATE_LOBBY -> {
                LobbyInfo lobby = (LobbyInfo) message.getObj();
                try{
                    session.addLobby(lobby.name(), lobby.reqPlayers());
                }catch(RemoteException e){
                    return null;
                }
            }
            case LOBBY_LIST -> {
                return new Message (null,session.getLobbies());
            }
            case JOIN_LOBBY -> {

            }

        }

        return null;
    }


}
