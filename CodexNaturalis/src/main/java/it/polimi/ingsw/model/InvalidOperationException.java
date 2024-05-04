package it.polimi.ingsw.model;

public class InvalidOperationException extends Exception{
    public InvalidOperationException(String message){
        super(message);
    }
    public InvalidOperationException(){
        super("The requested operation is invalid");
    }
}
