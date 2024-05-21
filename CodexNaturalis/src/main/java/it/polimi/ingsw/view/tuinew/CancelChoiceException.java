package it.polimi.ingsw.view.tuinew;

public class CancelChoiceException extends RuntimeException {

    public CancelChoiceException(){
        super("The dialog was cancelled");
    }
}
