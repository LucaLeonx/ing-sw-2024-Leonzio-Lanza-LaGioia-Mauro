package it.polimi.ingsw.dataobject;

import java.io.Serializable;
import java.util.AbstractMap;

public class Message implements Serializable {
    private final MessageType messageType;
    private AbstractMap.SimpleEntry<String,Integer> credentials;
    private final Object obj;

    public Message(MessageType messageType,AbstractMap.SimpleEntry<String,Integer> cred, Object obj){
        this.messageType = messageType;
        this.credentials = cred;
        this.obj = obj;
    }

    public MessageType getMessageType() {return messageType;}
    public AbstractMap.SimpleEntry<String,Integer> getCredentials() {return credentials;}
    public Object getObj() { return obj; }
}
