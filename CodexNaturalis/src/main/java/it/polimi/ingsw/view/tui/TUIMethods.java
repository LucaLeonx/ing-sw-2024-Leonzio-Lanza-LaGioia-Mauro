package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.PlayerColor;

import java.util.List;

import static java.lang.Math.abs;


public class TUIMethods {
    public static void drawMap(ControlledPlayerInfo player, GameFieldInfo gameField, boolean isWithAvailablePosition) {

        // 1 find the max and min row and column in order to create a matrix of the right dimension
        int minX = 0; // x of the leftmost cell
        int maxX = 0; // x of the rightmost cell
        int minY = 0; // y of the nethermost cell
        int maxY = 0; // y of the topmost  cell

        //
        for (Point p : gameField.placedCards().keySet()) {
            if (p.x() < minX) {
                minX = p.x();
            }
            if (p.x() > maxX) {
                maxX = p.x();
            }
            if (p.y() < minY) {
                minY = p.y();
            }
            if (p.y() > maxY) {
                maxY = p.y();
            }
        }

        //if I need to show also avaiable position it is possible that the map enlarges by a little bit.
        if(isWithAvailablePosition){
            for (Point p : gameField.availablePositions()) {
                if (p.x() < minX) {
                    minX = p.x();
                }
                if (p.x() > maxX) {
                    maxX = p.x();
                }
                if (p.y() < minY) {
                    minY = p.y();
                }
                if (p.y() > maxY) {
                    maxY = p.y();
                }
            }
        }


        // we need to consider maxX+1 because the rightmost card also needs to have a right edge, same is true for minX, minY, maxY
        maxX++;
        minX--;
        maxY++;
        minY--;
        //the +1 is for the central cell (0,0)
        int rows = maxY - minY + 1;
        int columns = maxX - minX + 1;


        String[][] matrixMap = new String[rows][columns];

        //2 Populate the array with black square
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrixMap[i][j] = Symbol_String.BLACK_SQUARE_SYMBOL;
            }
        }


        //3 Populate maps with the correct card color and add central symbol if the orientation of the card is back
        for (Point p : gameField.placedCards().keySet()) {
            String centralSymbol = Symbol_String.ANIMAL_SYMBOL; //We need this to keep track of what we need to put in the central cell of the card in case the card is plaued on the back.
            String cardColor = Symbol_String.ANIMAL_SYMBOL;
            if (gameField.placedCards().get(p).card().color() == CardColor.SKYBLUE) {
                cardColor = Symbol_String.BLUE_SQUARE_SYMBOL;
                centralSymbol = Symbol_String.ANIMAL_SYMBOL;
            } else if (gameField.placedCards().get(p).card().color() == CardColor.RED) {
                cardColor = Symbol_String.RED_SQUARE_SYMBOL;
                centralSymbol = Symbol_String.FUNGI_SYMBOL;
            } else if (gameField.placedCards().get(p).card().color() == CardColor.GREEN) {
                cardColor = Symbol_String.GREEN_SQUARE_SYMBOL;
                centralSymbol = Symbol_String.PLANT_SYMBOL;
            } else if (gameField.placedCards().get(p).card().color() == CardColor.PURPLE) {
                cardColor = Symbol_String.PURPLE_SQUARE_SYMBOL;
                centralSymbol = Symbol_String.INSECT_SYMBOL;
            } else if (gameField.placedCards().get(p).card().color() == CardColor.WHITE) {
                cardColor = Symbol_String.BROWN_SQUARE_SYMBOL; // initial card
            }
            if (gameField.placedCards().get(p).orientation().equals(CardOrientation.FRONT)) {
                matrixMap[p.y() + abs(minY)][p.x() + abs(minX)] = cardColor;
            } else {
                matrixMap[p.y() + abs(minY)][p.x() + abs(minX)] = centralSymbol;
            }
            matrixMap[p.y() + abs(minY) + 1][p.x() + abs(minX)] = cardColor;
            matrixMap[p.y() + abs(minY)][p.x() + abs(minX) + 1] = cardColor;
            matrixMap[p.y() + abs(minY) - 1][p.x() + abs(minX)] = cardColor;
            matrixMap[p.y() + abs(minY)][p.x() + abs(minX) - 1] = cardColor;
        }

        //Also add background of possible card and available cells.
        if(isWithAvailablePosition){

            int availablePositionCount = 1;

            for(Point p : gameField.availablePositions()) {
                matrixMap[p.y() + abs(minY) + 1][p.x() + abs(minX)] = Symbol_String.YELLOW_SQUARE_SYMBOL;
                matrixMap[p.y() + abs(minY) + 1][p.x() + abs(minX)+ 1] = Symbol_String.YELLOW_SQUARE_SYMBOL;
                matrixMap[p.y() + abs(minY) ][p.x() + abs(minX) + 1] = Symbol_String.YELLOW_SQUARE_SYMBOL;
                matrixMap[p.y() + abs(minY) + 1][p.x() + abs(minX) -1] = Symbol_String.YELLOW_SQUARE_SYMBOL;
                matrixMap[p.y() + abs(minY) - 1][p.x() + abs(minX) + 1] = Symbol_String.YELLOW_SQUARE_SYMBOL;
                matrixMap[p.y() + abs(minY) ][p.x() + abs(minX) -1] = Symbol_String.YELLOW_SQUARE_SYMBOL;
                matrixMap[p.y() + abs(minY) - 1][p.x() + abs(minX)] = Symbol_String.YELLOW_SQUARE_SYMBOL;
                matrixMap[p.y() + abs(minY) - 1][p.x() + abs(minX) -1] = Symbol_String.YELLOW_SQUARE_SYMBOL;

                List<String> numberAsEmojis = Symbol_String.FromIntToEmoji(availablePositionCount);

                if(availablePositionCount < 10){
                    matrixMap[p.y() + abs(minY)][p.x() + abs(minX)] = numberAsEmojis.getFirst() ;
                }
                else if(availablePositionCount < 100){
                    matrixMap[p.y() + abs(minY)][p.x() + abs(minX) -1] = numberAsEmojis.getFirst();
                    matrixMap[p.y() + abs(minY)][p.x() + abs(minX) ] = numberAsEmojis.get(1);
                }
                else if(availablePositionCount < 1000){
                    matrixMap[p.y() + abs(minY)][p.x() + abs(minX) -1] = numberAsEmojis.getFirst();
                    matrixMap[p.y() + abs(minY)][p.x() + abs(minX) ] = numberAsEmojis.get(1);
                    matrixMap[p.y() + abs(minY)][p.x() + abs(minX) +1] = numberAsEmojis.get(2);
                }
                // we assumed it is impossible to have more than 1000 choice to position a card in a game.

                availablePositionCount++;
            }
        }


        //4 Populate maps with the correct symbol.
        for (Point p : gameField.placedAngles().keySet()) {
            String symbol = Symbol_String.WHITE_SQUARE_SYMBOL;
            if (gameField.placedAngles().get(p).topSymbol() == Symbol.ANIMAL) {
                symbol = Symbol_String.ANIMAL_SYMBOL;
            } else if (gameField.placedAngles().get(p).topSymbol() == Symbol.PLANT) {
                symbol = Symbol_String.PLANT_SYMBOL;
            } else if (gameField.placedAngles().get(p).topSymbol() == Symbol.INSECT) {
                symbol = Symbol_String.INSECT_SYMBOL;
            } else if (gameField.placedAngles().get(p).topSymbol() == Symbol.FUNGI) {
                symbol = Symbol_String.FUNGI_SYMBOL;
            } else if (gameField.placedAngles().get(p).topSymbol() == Symbol.INKWELL) {
                symbol = Symbol_String.INKWELL_SYMBOL;
            } else if (gameField.placedAngles().get(p).topSymbol() == Symbol.MANUSCRIPT) {
                symbol = Symbol_String.MANUSCRIPT_SYMBOL;
            } else if (gameField.placedAngles().get(p).topSymbol() == Symbol.QUILL) {
                symbol = Symbol_String.QUILL_SYMBOL;
            } else if (gameField.placedAngles().get(p).topSymbol() == Symbol.BLANK) {
                symbol = Symbol_String.WHITE_SQUARE_SYMBOL;
            } else if (gameField.placedAngles().get(p).topSymbol() == Symbol.HIDDEN) {
                Point TopPositionCard = gameField.placedAngles().get(p).topCardPosition();
                if (gameField.placedCards().get(TopPositionCard).card().color() == CardColor.WHITE) {
                    symbol = Symbol_String.BROWN_SQUARE_SYMBOL;
                } else if (gameField.placedCards().get(TopPositionCard).card().color() == CardColor.SKYBLUE) {
                    symbol = Symbol_String.BLUE_SQUARE_SYMBOL;
                } else if (gameField.placedCards().get(TopPositionCard).card().color() == CardColor.GREEN) {
                    symbol = Symbol_String.GREEN_SQUARE_SYMBOL;
                } else if (gameField.placedCards().get(TopPositionCard).card().color() == CardColor.RED) {
                    symbol = Symbol_String.RED_SQUARE_SYMBOL;
                } else if (gameField.placedCards().get(TopPositionCard).card().color() == CardColor.PURPLE) {
                    symbol = Symbol_String.PURPLE_SQUARE_SYMBOL;
                }
            }
            matrixMap[p.y() + abs(minY)][p.x() + abs(minX)] = symbol;
        }

        //5 we add the player token in the middle of the map
        String playerSymbol = Symbol_String.BLUE_CIRCLE_SYMBOL; //we need to initialize it to not get strange error.
        if (player.color() == PlayerColor.BLUE) {
            playerSymbol = Symbol_String.BLUE_CIRCLE_SYMBOL;
        } else if (player.color() == PlayerColor.RED) {
            playerSymbol = Symbol_String.RED_CIRCLE_SYMBOL;
        } else if (player.color() == PlayerColor.GREEN) {
            playerSymbol = Symbol_String.GREEN_CIRCLE_SYMBOL;
        } else if (player.color() == PlayerColor.YELLOW) {
            playerSymbol = Symbol_String.YELLOW_CIRCLE_SYMBOL;
        }
        matrixMap[abs(minY)][abs(minX)] = playerSymbol;

        //6 Display the contents of the matrixMap
        for (int i = rows - 1; i >= 0; i--) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrixMap[i][j]);
            }
            System.out.println(); // Move to the next row
        }

        //7 We print out what's underneath the starting symbol
        Point origin = new Point(0, 0);
        CardOrientation initialCardOrientation = gameField.placedCards().get(origin).orientation();
        System.out.println("list of the symbol in the middle of the starting card: " + gameField.placedCards().get(origin).card().getSide(initialCardOrientation).centerSymbols());
    }

    public static void showPoints(OpponentInfo player) {
        System.out.println("Player " + player.nickname() + " has " + player.score() + " points");
    }

    public static void showHand(ControlledPlayerInfo player) {
        System.out.println("Your hand: ");
        String[][] matrixHand = new String[3][23]; // 20 columns for 3 cards +3 cells for tabs

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 23; j++) {
                matrixHand[i][j] = "\t\t"; //I add the space between the card.
            }
        }

        for (int i = 0; i < 3; i++) {
            String[][] currentCard = new String[3][5];
            try {

                currentCard = UtilityClassCardSketcher.sketchCard(player.cardsInHand().get(i).front());
            } catch (IndexOutOfBoundsException e) {
                currentCard = UtilityClassCardSketcher.sketchEmptyCard();
            }
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 5; k++) {
                    matrixHand[j][k + 6 * i] = currentCard[j][k];
                }
            }
        }

        String[][] currentCard = new String[3][5];
        currentCard = UtilityClassCardSketcher.sketchObjectiveCard(player.secretObjective());
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 5; k++) {
                matrixHand[j][k + 18] = currentCard[j][k];
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 23; j++) {
                System.out.print(matrixHand[i][j]);
            }
            System.out.println();
        }

    }
    public  static void show2Objectives(ObjectiveInfo objectiveCard1, ObjectiveInfo objectiveCard2){
        String[][] objectives = new String[3][11];
        //I add the space between the card.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                objectives[i][j] = "\t\t";
            }
        }

        String[][] Card1 = new String[3][5];
        String[][] Card2 = new String[3][5];

        Card1 = UtilityClassCardSketcher.sketchObjectiveCard(objectiveCard1);
        Card2 = UtilityClassCardSketcher.sketchObjectiveCard(objectiveCard2);

        for(int j = 0; j < 3; j++) {
            for (int k = 0; k < 5; k++) {
                objectives[j][k] = Card1[j][k];
                objectives[j][k + 6] = Card2[j][k];
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(objectives[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n\n");

    }


    public static void showCardsOnTable(ObjectiveInfo objectiveCard1, ObjectiveInfo objectiveCard2, DrawableCardsInfo drawable) {
        System.out.println("Cards on the table: ");
        CardSideInfo goldDeck = drawable.drawableCards().get(DrawChoice.DECK_GOLD);
        CardSideInfo resourceDeck = drawable.drawableCards().get(DrawChoice.DECK_RESOURCE);
        CardSideInfo resourceCard1 = drawable.drawableCards().get(DrawChoice.RESOURCE_CARD_1);
        CardSideInfo resourceCard2 = drawable.drawableCards().get(DrawChoice.RESOURCE_CARD_2);
        CardSideInfo goldenCard1 = drawable.drawableCards().get(DrawChoice.GOLD_CARD_1);
        CardSideInfo goldenCard2 = drawable.drawableCards().get(DrawChoice.GOLD_CARD_2);

        String[][] decks = new String[3][11]; // 11 columns for 2 cards +1 cells for tabs
        String[][] drawableCards1 = new String[3][11]; // 1st row of card that are face up on the table to draw from
        String[][] drawableCards2 = new String[3][11];



        //I add the space between the card.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                decks[i][j] = "\t\t";
                drawableCards1[i][j] = "\t\t";
                drawableCards2[i][j] = "\t\t";
            }
        }

        String[][] Card1 = new String[3][5];
        String[][] Card2 = new String[3][5];

        if (resourceDeck == null) {
            Card1 = UtilityClassCardSketcher.sketchEmptyCard();
        } else {
            Card1 = UtilityClassCardSketcher.sketchResourceDeck(resourceDeck.color());
        }

        if (goldDeck == null) {
            Card2 = UtilityClassCardSketcher.sketchEmptyCard();
        } else {
            Card2 = UtilityClassCardSketcher.sketchGoldenDeck(goldDeck.color());
        }

        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 5; k++) {
                decks[j][k] = Card1[j][k];
                decks[j][k + 6] = Card2[j][k];
            }
        }

        if (resourceCard1 == null) {
            Card1 = UtilityClassCardSketcher.sketchEmptyCard();
        } else {
            Card1 = UtilityClassCardSketcher.sketchCard(resourceCard1);
        }

        if (goldenCard1 == null) {
            Card2 = UtilityClassCardSketcher.sketchEmptyCard();
        } else {
            Card2 = UtilityClassCardSketcher.sketchCard(goldenCard1);
        }

        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 5; k++) {
                drawableCards1[j][k] = Card1[j][k];
                drawableCards1[j][k + 6] = Card2[j][k];
            }
        }

        if (resourceCard2 == null) {
            Card1 = UtilityClassCardSketcher.sketchEmptyCard();
        } else {
            Card1 = UtilityClassCardSketcher.sketchCard(resourceCard2);
        }

        if (goldenCard2 == null) {
            Card2 = UtilityClassCardSketcher.sketchEmptyCard();
        } else {
            Card2 = UtilityClassCardSketcher.sketchCard(goldenCard2);
        }

        for(int j = 0; j < 3; j++) {
            for(int k = 0; k < 5; k++) {
                drawableCards2[j][k] = Card1[j][k];
                drawableCards2[j][k + 6] = Card2[j][k];
            }
        }

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(decks[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n\n");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(drawableCards1[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n\n");

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(drawableCards2[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n\n");
        show2Objectives(objectiveCard1, objectiveCard2);
    }


    public static void showInitialCard(CardInfo card){
        String[][] Front = new String[5][3];
        String[][] Back = new String[5][3];
        Front=UtilityClassCardSketcher.sketchResourceCard(card.front()); // I initially sketch it as a resource card and then I will add the center symbol
        Back=UtilityClassCardSketcher.sketchResourceCard(card.back());
        // Now to the front we need also to add the (at max 3) center symbols.
        int i=0;
        for(Symbol s: card.front().centerSymbols()){
            if(i==0){
                Front[1][2]=Symbol_String.FromSymbolToString(s);
            }
            else if(i==1){
                Front[0][2]=Symbol_String.FromSymbolToString(s);
            }
            else if(i==2){
                Front[2][2]=Symbol_String.FromSymbolToString(s);
            }
            else{
                System.out.println("We have an error, it seems like there is an initial card with more than 3 center Symbols");
            }
            i++;
        }
        System.out.println("Front of the objective card: ");
        for (int k = 0; k < 3; k++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(Front[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n");


        System.out.println("Back of the objective card: ");
        for (int k = 0; k < 3; k++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(Back[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n");
    }


}