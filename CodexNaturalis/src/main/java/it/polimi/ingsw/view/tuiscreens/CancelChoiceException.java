package it.polimi.ingsw.view.tuiscreens;

public class CancelChoiceException extends RuntimeException {

    public CancelChoiceException(){
        super("The dialog was cancelled");
    }
}
