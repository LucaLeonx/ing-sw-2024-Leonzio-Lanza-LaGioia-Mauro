package it.polimi.ingsw.controller.servercontroller.operationexceptions;

import it.polimi.ingsw.model.InvalidOperationException;

public class ElementNotFoundException extends InvalidOperationException {
    public ElementNotFoundException(String message){
        super(message);
    }

    public ElementNotFoundException(){
        super("The requested operation cannot be performed in the current situation");
    }
}