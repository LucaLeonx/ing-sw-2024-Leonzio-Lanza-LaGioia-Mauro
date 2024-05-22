package it.polimi.ingsw.controller.socket;

import com.sun.security.auth.LdapPrincipal;
import it.polimi.ingsw.controller.clientcontroller.NotificationStore;
import it.polimi.ingsw.controller.servercontroller.*;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidCredentialsException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.WrongPhaseException;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidOperationException;
import it.polimi.ingsw.model.card.CardOrientation;

import java.rmi.RemoteException;
import java.util.*;

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

            case GET_JOINED_LOBBY_INFO -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                return new Message(null,null,server.getJoinedLobbyInfo(user));
            }

            case JOIN_LOBBY -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                server.joinLobby(user,(Integer) message.getObj());
            }

            case EXIT_FROM_LOBBY -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                server.exitFromLobby(user);
            }

            case GET_CURRENT_PLAYER_NAME -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                return new Message(null,null,server.getCurrentPlayer(user));
            }

            case GET_PLAYER_NAMES -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                return new Message(null,null,server.getPlayerNames(user));
            }

            case GET_PLAYER_SETUP -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                return new Message(null,null,server.getPlayerSetupInfo(user));
            }

            case GET_COMMON_OBJECTIVES -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                return new Message(null,null,server.getCommonObjectives(user));
            }

            case GET_CONTROLLED_PLAYER_INFO -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                return new Message(null,null,server.getControlledPlayerInfo(user));
            }

            case GET_OPPONENT_INFORMATION -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                return new Message(null,null,server.getOpponentInfo(user,message.getObj().toString()));
            }

            case GET_DRAWABLE_CARDS -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                return new Message(null,null,server.getDrawableCardsInfo(user));
            }

            case IS_LAST_TURN -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                return new Message(null,null,server.isLastTurn(user));
            }

            case HAS_GAME_ENDED -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                return new Message(null,null,server.hasGameEnded(user));
            }

            case GET_LEADERBOARD -> {
                User user;
                List<ControlledPlayerInfo> lead = null;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                try {
                    lead = server.getLeaderboard(user);
                }catch(WrongPhaseException e){
                    return new Message(null,null,e);
                }
                return new Message(null,null,lead);
            }

            case MAKE_MOVE -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                MoveInfo mInfo = (MoveInfo) message.getObj();
                try{
                    server.registerPlayerMove(user,mInfo.card().id(), mInfo.placementPoint(),mInfo.chosenSide(),mInfo.drawChoice());
                }catch(WrongPhaseException e){
                    return new Message(null,null,e);
                }
            }

            case EXIT_GAME -> {
                User user;
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                server.exitFromGame(user);
            }

            case SET_PLAYER_SETUP -> {
                User user;
                Tuple data = (Tuple) message.getObj();
                try {
                    user = checkLogin(message.getCredentials().getKey(),message.getCredentials().getValue());
                }catch(InvalidOperationException e){
                    return new Message(null,null,e);
                }
                server.registerPlayerSetup(user,((ObjectiveInfo) data.first()).id(),(CardOrientation) data.second() );
            }



        }

        return null;
    }


}
