package it.polimi.ingsw.view.tuiscreens;

import it.polimi.ingsw.controller.clientcontroller.ClientController;
import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.view.tui.TUI;
import it.polimi.ingsw.view.tui.TUIMethods;
import it.polimi.ingsw.view.tui.TUIScreen;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NewGamePlayScreen extends TUIScreen {
    public NewGamePlayScreen(TUI tui, Scanner scanner, ClientController controller) {
        super(tui, scanner, controller);
    }

    @Override
    public void display() {
        try {
            ControlledPlayerInfo controlledPlayer = controller.getControlledPlayerInformation();
            String controlledPlayerName = controlledPlayer.nickname();
            List<ObjectiveInfo> objectives = controller.getCommonObjectives();
            ObjectiveInfo objective1 = objectives.getFirst();
            ObjectiveInfo objective2 = objectives.getLast();


            while(!controller.hasGameEnded()){
                String currentPlayerName = controller.getCurrentPlayerName();
                DrawableCardsInfo drawableCards = controller.getDrawableCards();

                if(currentPlayerName.equals(controlledPlayerName)){
                    controlledPlayer = controller.getControlledPlayerInformation();
                    System.out.println("It's your turn");
                    System.out.println("Your score: " + controlledPlayer.score());

                    if(controller.isLastTurn()){
                        TUIMethods.printStylishMessage("ATTENTION: IT IS YOUR LAST TURN!!", "\u001B[31m", "\u001B[33m");
                    }

                    System.out.println();
                    TUIMethods.drawMap(controlledPlayer.color(), controlledPlayer.field(), true);
                    TUIMethods.showCardsOnTable(objective1, objective2, drawableCards);
                    TUIMethods.showHand(controlledPlayer);

                    boolean confirmed = true;

                    do {
                        confirmed = true;
                        try {
                            CardInfo chosenCard = new AssignmentDialog<>(
                                    "Choose card to play: ",
                                    generateCardOptions(controlledPlayer.cardsInHand()))
                                    .askForChoice(scanner);


                            CardOrientation chosenOrientation = new AssignmentDialog<>(
                                    "Choose the orientation:",
                                    generateOrientationOption(chosenCard))
                                    .askForChoice(scanner);


                            Point position = new AssignmentDialog<>(
                                    "Choose position to play card:",
                                    generatePositionOptions(controlledPlayer.field().availablePositions()))
                                    .askForChoice(scanner);


                            DrawChoice cardToDraw = null;
                            if(!drawableCards.drawableCards().isEmpty()){
                                cardToDraw = new AssignmentDialog<>(
                                        "Choose card to draw:",
                                        generateDrawChoiceOptions(drawableCards))
                                        .askForChoice(scanner);
                            }

                            controller.makeMove(chosenCard, position, chosenOrientation, cardToDraw);


                        } catch (CancelChoiceException e){
                            confirmed = false;
                        }
                    } while(!confirmed);


                } else {
                    OpponentInfo currentPlayingOpponent = controller.getOpponentInformation(currentPlayerName);
                    System.out.println("Waiting for " + currentPlayerName + " Score: " + currentPlayingOpponent.score() + " to make his move");
                    controller.waitForTurnChange();
                    System.out.println(currentPlayingOpponent.nickname() + " has done its move, this is his final field: ");
                    TUIMethods.drawMap(currentPlayingOpponent.color(), controller.getOpponentInformation(currentPlayerName).field(), false);
                    System.out.println("And these the remaining cards on table: ");
                    TUIMethods.showCardsOnTable(objective1, objective2, controller.getDrawableCards());

                }
            }
            transitionState(new NewGameEndState(tui, scanner, controller));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DialogOption<DrawChoice>> generateDrawChoiceOptions(DrawableCardsInfo drawableCards) {
       List<DialogOption<DrawChoice>> result = new ArrayList<>();
        result.add(new DialogOption<>("Deck Resource",DrawChoice.DECK_RESOURCE));
        result.add(new DialogOption<>("Resource Card 1",DrawChoice.RESOURCE_CARD_1));
        result.add(new DialogOption<>("Resource Card 2",DrawChoice.RESOURCE_CARD_2));
        result.add(new DialogOption<>("Deck Gold",DrawChoice.DECK_GOLD));
        result.add(new DialogOption<>("Gold Card 1",DrawChoice.GOLD_CARD_1));
        result.add(new DialogOption<>("Gold Card 2",DrawChoice.GOLD_CARD_2));
        return result;
    }

    private List<DialogOption<Point>> generatePositionOptions(ArrayList<Point> points) {
        return points.stream()
                .map((point) -> new DialogOption<>(point.toString(), point))
                .toList();
    }

    private List<DialogOption<CardInfo>> generateCardOptions(List<CardInfo> cardsInHand){
        List<DialogOption<CardInfo>> options = new ArrayList<>();

        for(int i = 0; i < cardsInHand.size(); i++){
            options.add(new DialogOption<>("Card" + (i + 1), cardsInHand.get(i)));
        }

        return options;
    }

    private List<DialogOption<CardOrientation>> generateOrientationOption(CardInfo chosenCard) {
        List<DialogOption<CardOrientation>> options = new ArrayList<>();

        if(chosenCard.getSide(CardOrientation.FRONT).isPlayable()){
            options.add(new DialogOption<>("FRONT", CardOrientation.FRONT));
        }

        if(chosenCard.getSide(CardOrientation.BACK).isPlayable()){
            options.add(new DialogOption<>("BACK", CardOrientation.BACK));
        }

        return options;

    }
}
