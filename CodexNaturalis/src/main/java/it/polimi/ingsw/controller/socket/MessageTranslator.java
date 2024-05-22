package it.polimi.ingsw.controller.socket;

import com.sun.security.auth.LdapPrincipal;
import it.polimi.ingsw.controller.clientcontroller.NotificationStore;
import it.polimi.ingsw.controller.servercontroller.*;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;
import it.polimi.ingsw.dataobject.MessageType;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.dataobject.ObjectiveInfo;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Server-side class.
 * Abstract class that will take the message sent from the client to the server and
 * will return the server response, this class is used only for socket connection
 */

public class MessageTranslator {

    private static CoreServer server;
    //private static AuthenticatedSession session;
    private static NotificationSubscriber dummySubs;
    //private static MessageTranslator instance;

    public MessageTranslator(CoreServer manager) {
        this.server = manager;
        try {
            dummySubs = new NotificationStore();
        }catch (RemoteException e){
            e.printStackTrace();
        }

    }
    /*
    private static boolean isAuthenticated(){
        return session != null;
    }
    */
    private static User checkLogin(String username, int password){
        User user = null;
        try{
            user = server.checkLoggedIn( username, password);
        }
        catch (RemoteException e){
            throw new InvalidOperationException("Cannot perform this operation when not logged in");
        }
        if(user == null){
            throw new InvalidOperationException("Cannot perform this operation when not logged in");
        };
        return user;
    }

//change message type, it must have always the username and password
    public static Message processMessage(Message message) throws RemoteException {
        switch (message.getMessageType()){
            case REGISTER_USER -> {
                //if(isAuthenticated()){
                //    session.logout();
                //}Do we need this if?
                int tempCode;

                try{
                    tempCode = server.register((String) message.getObj());
                }catch (InvalidOperationException e){
                    return new Message(null,(String) "Username already exists");
                }
                return new Message(MessageType.TEMP_CODE,tempCode);
            }
            case LOGIN -> {
                List<Object> tuple = List.of(message.getObj());
                String username = (String) tuple.get(0);
                int password = (int) tuple.get(1);

                server.login(username,password,dummySubs);

            }
            case CREATE_LOBBY -> {
                LobbyInfo lobby = (LobbyInfo) message.getObj();
                //User user = checkLogin(lobby.name(),);
                server.createLobby(null,lobby.name(), lobby.reqPlayers());

            }
            case LOBBY_LIST -> {
                //return new Message (null,session.getLobbies());
            }
            case JOIN_LOBBY -> {

            }

        }

        return null;
    }


}
