package it.polimi.ingsw.controller.servercontroller.operationexceptions;

public class ElementNotFoundException extends InvalidOperationException {
    public ElementNotFoundException(String message){
        super(message);
    }
    public ElementNotFoundException(){
        super("The requested operation cannot be performed in the current situation");
    }
}