package it.polimi.ingsw.controller.servercontroller.operationexceptions;

public class InvalidOperationException extends RuntimeException{
    public InvalidOperationException(String message){
        super(message);
    }
    public InvalidOperationException(){
        super("The requested operation is invalid");
    }
}
