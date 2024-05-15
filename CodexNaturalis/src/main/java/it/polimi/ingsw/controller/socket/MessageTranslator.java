package it.polimi.ingsw.controller.socket;

import it.polimi.ingsw.controller.clientcontroller.RMIClientController;
import it.polimi.ingsw.controller.servercontroller.AuthenticatedSession;
import it.polimi.ingsw.controller.servercontroller.AuthenticationManager;
import it.polimi.ingsw.controller.servercontroller.FrontierServerLayer;
import it.polimi.ingsw.controller.servercontroller.Lobby;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;
import it.polimi.ingsw.dataobject.MessageType;

import javax.management.InstanceAlreadyExistsException;
import java.rmi.RemoteException;

/**
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

    public static Message processMessage(Message message) throws RemoteException {
        switch (message.getMessageType()){
            case REGISTER_USER -> {
                if(isAuthenticated()){
                    session.logout();
                }
                int tempCode = manager.register((String) message.getObj());
                return new Message(MessageType.TEMP_CODE,tempCode);
            }
            case CREATE_LOBBY -> {
                LobbyInfo lobby = (LobbyInfo) message.getObj();
                try{
                    session.addLobby(lobby.name(), lobby.reqPlayers());
                }catch(RemoteException e){
                    return null;
                }
            }
            case JOIN_LOBBY -> {

            }
        }

        return null;
    }


}
