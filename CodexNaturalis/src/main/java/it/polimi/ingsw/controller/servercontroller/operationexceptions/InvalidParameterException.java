package it.polimi.ingsw.controller.servercontroller.operationexceptions;

public class InvalidParameterException extends InvalidOperationException {
    public InvalidParameterException(String message){
        super(message);
    }

    public InvalidParameterException(){
        super("The requested operation cannot be performed in the current situation");
    }
}