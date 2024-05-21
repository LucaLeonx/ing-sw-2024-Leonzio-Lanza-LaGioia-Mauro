package it.polimi.ingsw.view.tuinew;

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
import java.util.Arrays;
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

            TUIMethods.showHand(controlledPlayer);
            TUIMethods.drawMap(controlledPlayer.color(), controlledPlayer.field(), true);
            TUIMethods.showCardsOnTable(objective1, objective2, controller.getDrawableCards());

            while(!controller.hasGameEnded()){
                String currentPlayerName = controller.getCurrentPlayerName();
                DrawableCardsInfo drawableCards = controller.getDrawableCards();

                if(currentPlayerName.equals(controlledPlayerName)){
                    controlledPlayer = controller.getControlledPlayerInformation();
                    System.out.println("It's your turn");
                    System.out.println("Your score: " + controlledPlayer.score());

                    if(controller.isLastTurn()){
                        System.out.println("It's last turn");
                    }

                    TUIMethods.showHand(controlledPlayer);
                    TUIMethods.drawMap(controlledPlayer.color(), controlledPlayer.field(), true);
                    TUIMethods.showCardsOnTable(objective1, objective2, drawableCards);

                    boolean confirmed = true;

                    do {
                        confirmed = true;
                        try {
                            TUIMethods.showHand(controlledPlayer);
                            CardInfo chosenCard = new AssignmentDialog<>(
                                    "Choose card to play: ",
                                    generateCardOptions(controlledPlayer.cardsInHand()))
                                    .askForChoice(scanner);


                            CardOrientation chosenOrientation = new AssignmentDialog<>(
                                    "Choose the orientation:",
                                    generateOrientationOption(chosenCard))
                                    .askForChoice(scanner);

                            TUIMethods.drawMap(controlledPlayer.color(), controlledPlayer.field(), true);
                            Point position = new AssignmentDialog<>(
                                    "Choose position to play card:",
                                    generatePositionOptions(controlledPlayer.field().availablePositions()))
                                    .askForChoice(scanner);

                            TUIMethods.showCardsOnTable(objective1, objective2, drawableCards);

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
                    System.out.println("Now it is playing " + currentPlayerName + " Score: " + currentPlayingOpponent.score());
                    TUIMethods.showCardsOnTable(objective1, objective2, drawableCards);
                    TUIMethods.drawMap(currentPlayingOpponent.color(), currentPlayingOpponent.field(), false);
                    controller.waitForTurnChange();
                    System.out.println("After move");
                    System.out.println("Now it is playing " + currentPlayerName + " Score: " + currentPlayingOpponent.score());
                    TUIMethods.showCardsOnTable(objective1, objective2, controller.getDrawableCards());
                    TUIMethods.drawMap(currentPlayingOpponent.color(), controller.getOpponentInformation(currentPlayerName).field(), false);
                }
            }

            transitionState(new NewGameEndState(tui, scanner, controller));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private List<DialogOption<DrawChoice>> generateDrawChoiceOptions(DrawableCardsInfo drawableCards) {
        return drawableCards.drawableCards()
                .keySet().stream()
                .map((drawChoice) -> new DialogOption<>(drawChoice.toString(), drawChoice))
                .toList();
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
