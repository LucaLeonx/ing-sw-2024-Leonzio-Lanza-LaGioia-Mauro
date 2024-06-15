package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.dataobject.*;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.PlayerColor;

import java.util.List;

import static java.lang.Math.abs;

/**
 * a class used for drawing or sketching the various card in the terminal using emoji and our model.
 */

public class TUIMethods {

    /**
     * Draw the entire map of a player in the terminal
     * @param playerColor : color of the token of the player
     * @param gameField : game field we want to draw
     * @param isWithAvailablePosition : parameter used to say if the map should have placeholders where the player could put his cards
     */
    public static void drawMap(PlayerColor playerColor, GameFieldInfo gameField, boolean isWithAvailablePosition) {
        System.out.println("map: ");
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

                matrixMap[p.y() + abs(minY)][p.x() + abs(minX)] = Symbol_String.FromIntToString(availablePositionCount);
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
        if (playerColor == PlayerColor.BLUE) {
            playerSymbol = Symbol_String.BLUE_CIRCLE_SYMBOL;
        } else if (playerColor == PlayerColor.RED) {
            playerSymbol = Symbol_String.RED_CIRCLE_SYMBOL;
        } else if (playerColor == PlayerColor.GREEN) {
            playerSymbol = Symbol_String.GREEN_CIRCLE_SYMBOL;
        } else if (playerColor == PlayerColor.YELLOW) {
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

        // if is withAvaiablePosition=TRUE it might also be useful for playing player to have the list of all the symbol he has on the map
        if(isWithAvailablePosition) {
            System.out.println("");
            System.out.println("Your count of visible symbols on the map: ");
            System.out.println(Symbol_String.FromSymbolToString(Symbol.FUNGI) + ":" + gameField.symbolCounterMap().get(Symbol.FUNGI));
            System.out.println(Symbol_String.FromSymbolToString(Symbol.ANIMAL) + ":" + gameField.symbolCounterMap().get(Symbol.ANIMAL));
            System.out.println(Symbol_String.FromSymbolToString(Symbol.PLANT) + ":" + gameField.symbolCounterMap().get(Symbol.PLANT));
            System.out.println(Symbol_String.FromSymbolToString(Symbol.INSECT) + ":" + gameField.symbolCounterMap().get(Symbol.INSECT));
            System.out.println(Symbol_String.FromSymbolToString(Symbol.INKWELL) + ":" + gameField.symbolCounterMap().get(Symbol.INKWELL));
            System.out.println(Symbol_String.FromSymbolToString(Symbol.QUILL) + ":" + gameField.symbolCounterMap().get(Symbol.QUILL));
            System.out.println(Symbol_String.FromSymbolToString(Symbol.MANUSCRIPT) + ":" + gameField.symbolCounterMap().get(Symbol.MANUSCRIPT));
        }
        System.out.println("\n"); // just some spacing.
    }


    /**
     *
     * @param player that we need to print the hand in the terminal
     */
    public static void showHand(ControlledPlayerInfo player) {
        System.out.println("Your hand: ");
        String[][] matrixHand = new String[3][23]; // 20 columns for 3 cards +3 cells for tabs

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 23; j++) {
                matrixHand[i][j] = "      "; //I add the space between the card.
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
        if(player.secretObjective() != null) {
            currentCard = UtilityClassCardSketcher.sketchObjectiveCard(player.secretObjective());
        }
        else{
            currentCard = UtilityClassCardSketcher.sketchEmptyCard();
        }
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

        System.out.println("\n"); // just some spacing.

    }

    /**
     * Helper method to show either the 2 common objectives cards in the table or at the beginning of the game
     * or the 2 secret objective to chose from
     * @param objectiveCard1
     * @param objectiveCard2
     */
    public  static void show2Objectives(ObjectiveInfo objectiveCard1, ObjectiveInfo objectiveCard2){
        String[][] objectives = new String[3][11];
        //I add the space between the card.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                objectives[i][j] = "      ";
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
        System.out.println("\n"); // just some spacing.

    }

    /**
     * to sketch drawable cards and the 2 common objective cards
     * @param objectiveCard1
     * @param objectiveCard2
     * @param drawable
     */
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
                decks[i][j] = "      ";
                drawableCards1[i][j] = "      ";
                drawableCards2[i][j] = "      ";
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

        System.out.println("ResourceDeck    GoldDeck");
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(decks[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n\n");

        System.out.println("ResourceCard1   GoldCard1");
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(drawableCards1[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n\n");

        System.out.println("ResourceCard2   GoldCard2");
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(drawableCards2[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n\n");

        System.out.println("Objective1      Objective2");
        show2Objectives(objectiveCard1, objectiveCard2);
    }

    /**
     *
     * @param card initial card to show to the player in order for him/her to chose if he/she wants the front one or the back side of the card
     */
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
        System.out.println("Front of the initial card: ");
        for (int k = 0; k < 3; k++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(Front[k][j]);
            }
            System.out.println();
        }
        System.out.print("\n");


        System.out.println("Back of the objective card: ");
        for (int k = 0; k < 3; k++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(Back[k][j]);
            }
            System.out.println();
        }
        System.out.println("\n"); // just some spacing.
    }

    /**
     * print a decorated message
     * @param message string to print
     * @param borderColor
     * @param textColor
     */
    public static void printStylishMessage(String message, String borderColor, String textColor) {
        // Define ASCII art characters for styling
        final String TOP_LEFT = borderColor + "╔";
        final String TOP_RIGHT = borderColor + "╗";
        final String BOTTOM_LEFT = borderColor +"╚";
        final String BOTTOM_RIGHT =borderColor + "╝";
        final String HORIZONTAL_LINE =borderColor + "═";
        final String VERTICAL_LINE = borderColor + "║";

        String ANSI_BOLD = "\u001B[1m";
        // ANSI escape code to reset text formatting
        String ANSI_RESET = "\u001B[0m";

        // Determine message width based on length
        int messageLength = message.length();
        int boxWidth = messageLength + 4; // Additional padding for borders

        // Print top border with specified border color
        System.out.print(TOP_LEFT);
        for (int i = 0; i < boxWidth - 2; i++) {
            System.out.print(HORIZONTAL_LINE);
        }
        System.out.println(TOP_RIGHT);

        // Print message with specified text color
        System.out.println(VERTICAL_LINE + textColor + " " + ANSI_BOLD + message + ANSI_RESET + " " + VERTICAL_LINE);

        // Print bottom border with specified border color
        System.out.print(BOTTOM_LEFT);
        for (int i = 0; i < boxWidth - 2; i++) {
            System.out.print(HORIZONTAL_LINE);
        }
        System.out.println(BOTTOM_RIGHT);

        // Reset colors after printing
        System.out.print("\u001B[0m");
    }

    /**
     * makes an ascii art with a fungi and a butterfly
     */
    public static void printMushroom(){
        System.out.println("\u001B[31m"+ "                                __.....__ ");
        System.out.println("\u001B[31m"+"                             .'\"         \"`." );
        System.out.println("\u001B[31m"+"                           .'               `.\t\t\t\t"+ "\u001B[35m" + "(_\\|/_)");
        System.out.println("\u001B[31m"+"                          .                   . \t\t\t "+ "\u001B[35m" + "(/|\\) ");
        System.out.println("\u001B[31m"+"                         .       __...__       .");
        System.out.println("\u001B[31m"+"                        . _.--\"\"\"       \"\"\"--._ .");
        System.out.println("\u001B[31m"+"                        :\"                     \";");
        System.out.println("\u001B[31m"+"                         `-.__    :   :    __.-'");
        System.out.println("\u001B[31m"+"                             \"\"\"-:   :-\"\"\"   ");
        System.out.println("\u001B[31m"+"                                J     L");
        System.out.println("\u001B[31m"+"                                :     :  ");
        System.out.println("\u001B[31m"+"                                J       L");
        System.out.println("\u001B[31m"+"                                :       :");
        System.out.println("\u001B[31m"+"                               `._____.'");
        System.out.println("\u001B[32m"+"_______\\|/_____________\\|/_________________________\\|/_________________\\|/__________");
        // Reset colors after printing
        System.out.print("\u001B[0m");
    }

    /**
     * makes a wolf ascii art
     */

    public static void printWolf(){
        System.out.println("\u001B[34m"+ "                                           ,     ,");
        System.out.println("\u001B[34m"+"                                           |\\---/|" );
        System.out.println("\u001B[34m"+"                                          /  , , |\t\t\t"+ "\u001B[35m" + "(_\\|/_)");
        System.out.println("\u001B[34m"+"                                     __.-'|  / \\ /\t\t\t "+ "\u001B[35m" + "(/|\\) ");
        System.out.println("\u001B[34m"+"                            __ ___.-'        ._O|");
        System.out.println("\u001B[34m"+"                         .-'  '        :      _/");
        System.out.println("\u001B[34m"+"                        / ,    .        .     |");
        System.out.println("\u001B[34m"+"                       :  ;    :        :   _/");
        System.out.println("\u001B[34m"+"                       |  |   .'     __:   /");
        System.out.println("\u001B[34m"+"                       |  :   /'----'| \\  |");
        System.out.println("\u001B[34m"+"                       \\  |\\  |      | /| |");
        System.out.println("\u001B[34m"+"                        '.'| /       || \\ |");
        System.out.println("\u001B[32m"+"_____________\\|/________"+"\u001B[34m"+"| /|.'"+"\u001B[32m"+"_______"+ "\u001B[34m"+ "'.l \\\\_" + "\u001B[32m"+"_______\\|/______________________\\|/____");
        System.out.println("\u001B[32m"+"__\\|/___________________"+ "\u001B[34m"+"|| ||" +"\u001B[32m"+"_____________"+"\u001B[34m"+ "'-'" + "\u001B[32m"+"_______\\|/______________________\\|/____");
        System.out.println("\u001B[32m"+"________________\\|/_____"+ "\u001B[34m" +"'-''-'" + "\u001B[32m"+"_______\\|/_____________\\|/_________________________\\|/");
        //System.out.println("\u001B[32m"+"_______\\|/_____________\\|/_________________________\\|/_________________\\|/__________");
        // Reset colors after printing
        System.out.print("\u001B[0m");
    }
}
