package it.polimi.ingsw.controller.servercontroller.operationexceptions;

import it.polimi.ingsw.model.InvalidOperationException;

public class InvalidCommandException extends InvalidOperationException {
    public InvalidCommandException(String message){
        super(message);
    }

    public InvalidCommandException(){
        super("The requested operation cannot be performed in the current situation");
    }
}