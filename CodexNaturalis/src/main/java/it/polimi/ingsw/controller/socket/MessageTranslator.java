package it.polimi.ingsw.controller.socket;

import it.polimi.ingsw.dataobject.Message;

/**
 * Abstract class that will take the message sent from the client to the server and
 * will return the server response, this class is used only for socket connection
 */

public abstract class MessageTranslator {

    public Message processMessage(Message message){
        switch (message.getMessageType()){
            case CREATE_LOBBY -> {
            }
            case JOIN_LOBBY -> {

            }
        }

        return null;
    }


}
