package it.polimi.ingsw.controller.socket;

import com.sun.security.auth.LdapPrincipal;
import it.polimi.ingsw.controller.clientcontroller.NotificationStore;
import it.polimi.ingsw.controller.servercontroller.*;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidCredentialsException;
import it.polimi.ingsw.dataobject.LobbyInfo;
import it.polimi.ingsw.dataobject.Message;
import it.polimi.ingsw.dataobject.MessageType;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.dataobject.ObjectiveInfo;

import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Server-side class.
 * Abstract class that will take the message sent from the client to the server and
 * will return the server response, this class is used only for socket connection
 */

public class MessageTranslator {

    private static CoreServer server;
    private static NotificationSubscriber dummySubs;

    public MessageTranslator(CoreServer manager) {
        this.server = manager;
        try {
            dummySubs = new NotificationStore();
        }catch (RemoteException e){
            e.printStackTrace();
        }

    }

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
                int tempCode;
                try{
                    tempCode = server.register((String) message.getObj());
                }catch (InvalidOperationException e){
                    return new Message(null,null,e);
                }
                return new Message(MessageType.TEMP_CODE,null,tempCode);
            }

            case LOGIN -> {
                AbstractMap.SimpleEntry<String,Integer> tuple = message.getCredentials();
                try {
                    server.login(tuple.getKey(), tuple.getValue(), dummySubs);
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
            }

            case LOGOUT -> {
                try {
                    server.logout(checkLogin(message.getCredentials().getKey(), message.getCredentials().getValue()));
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
            }

            case LOBBY_LIST -> {
                //return new Message (null,session.getLobbies());
                User user = checkLogin(message.getCredentials().getKey(), message.getCredentials().getValue());
                return new Message(null,null, server.getLobbies(user));
            }

            case CREATE_LOBBY -> {
                LobbyInfo tuple = (LobbyInfo) message.getObj();
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                return  new Message(null,null,server.createLobby(user,tuple.name(), tuple.reqPlayers()));
            }
            case JOIN_LOBBY -> {

            }


        }

        return null;
    }


}
