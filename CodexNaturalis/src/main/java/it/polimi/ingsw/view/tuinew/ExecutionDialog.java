package it.polimi.ingsw.view.tuinew;

import java.util.Scanner;

public class ExecutionDialog extends AssignmentDialog<Runnable> {

    @SafeVarargs
    public ExecutionDialog(String header, DialogOption<Runnable>... options){
        super(header, options);
    }

    public void askAndExecuteChoice(Scanner inputScanner) throws CancelChoiceException {
        Runnable optionToExecute = super.askForChoice(inputScanner);
        optionToExecute.run();
    }
}
