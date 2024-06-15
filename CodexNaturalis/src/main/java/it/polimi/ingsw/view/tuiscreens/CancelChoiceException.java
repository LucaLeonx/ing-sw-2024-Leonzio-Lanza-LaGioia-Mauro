package it.polimi.ingsw.view.tuiscreens;

/**
 * exception arised when user want to come back.
 * Used so depending on the TUIScreen this exception can be handled in different way.
 */
public class CancelChoiceException extends RuntimeException {

    public CancelChoiceException(){
        super("The dialog was cancelled");
    }
}
