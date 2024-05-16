package it.polimi.ingsw.controller.servercontroller.operationexceptions;

import it.polimi.ingsw.model.InvalidOperationException;

public class WrongPhaseException extends InvalidOperationException {
    public WrongPhaseException(String message){
        super(message);
    }

    public WrongPhaseException(){
        super("The requested operation cannot be performed in the current situation");
    }
}
