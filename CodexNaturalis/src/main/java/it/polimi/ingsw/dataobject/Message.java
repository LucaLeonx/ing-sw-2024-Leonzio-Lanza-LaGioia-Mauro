package it.polimi.ingsw.dataobject;

import java.io.Serializable;

public class Message implements Serializable {
    private final MessageType messageType;
    private final Object obj;

    public Message(MessageType messageType, Object obj){
        this.messageType = messageType;
        this.obj = obj;
    }

    public MessageType getMessageType() {return messageType;}
    public Object getObj() { return obj; }
}
