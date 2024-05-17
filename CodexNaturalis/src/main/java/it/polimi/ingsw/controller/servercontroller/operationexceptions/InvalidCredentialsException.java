package it.polimi.ingsw.controller.servercontroller.operationexceptions;

public class InvalidCredentialsException extends InvalidOperationException {
    public InvalidCredentialsException(String message){
        super(message);
    }

    public InvalidCredentialsException(){
        super("Wrong username or temporary code");
    }
}
