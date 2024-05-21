package it.polimi.ingsw.view.tuiscreens;

import java.util.*;

public class AssignmentDialog<T> {

    private final String header;
    private final List<DialogOption<T>> optionList;

    @SafeVarargs
    public AssignmentDialog(String header, DialogOption<T>... options){
        this.header = header;
        this.optionList = Arrays.asList(options);

        if(optionList.isEmpty()){
            throw new IllegalArgumentException("Cannot use an AssignmentDialog without options");
        }
    }

    public AssignmentDialog(String header, List<DialogOption<T>> options){
        this.header = header;
        this.optionList = new ArrayList<>(options);
    }

    public T askForChoice(Scanner input) throws CancelChoiceException {

        boolean invalidInput = true;
        T chosenValue = null;

        while(invalidInput){
            System.out.println(header);

            int optionIndex = 1;

            for(DialogOption<T> option : optionList){
                System.out.println(optionIndex + ". " + option.label());
                optionIndex++;
            }
            System.out.println("0. Cancel");

            System.out.print(">> ");

            String inputChoice = input.nextLine().trim();
            int chosenOption = 0;

            try {
               chosenOption = Integer.parseInt(inputChoice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                continue;
            }

            if(chosenOption == 0){
                throw new CancelChoiceException();
            } else if(chosenOption < 0 || chosenOption > optionList.size()){
                System.out.println("Input out of range");
                continue;
            }

            chosenValue = optionList.get(chosenOption - 1).assignedValue();
            invalidInput = false;
        }

        System.out.println(); // Just to pad different dialogs
        return chosenValue;
    }
}
