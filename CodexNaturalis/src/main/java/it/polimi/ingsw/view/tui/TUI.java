package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.CardInfo;
import it.polimi.ingsw.controller.clientcontroller.ControlledPlayerInfo;
import it.polimi.ingsw.controller.clientcontroller.ObjectiveInfo;
import it.polimi.ingsw.controller.clientcontroller.OpponentInfo;
import it.polimi.ingsw.model.DrawChoice;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

import static java.lang.Math.abs;


public class TUI {
    static String fungiSymbol= "\uD83C\uDF44";
    static String animalSymbol= "\uD83D\uDC3A";
    static String insectSymbol= "\uD83E\uDD8B";
    static String plantSymbol= "\uD83C\uDF3F";
    static String manuscriptSymbol= "\uD83D\uDCDD";
    static String quillSymbol= "\uD83E\uDEB6";
    static String inkwellSymbol= "\uD83E\uDED9";
    static String whiteSquareSymbol= "\u2B1C";
    static String redSquareSymbol= "\uD83D\uDFE5";
    static String greenSquareSymbol= "\uD83D\uDFE9";
    static String blueSquareSymbol= "\uD83D\uDFE6";
    static String purpleSquareSymbol= "\uD83D\uDFEA";
    static String brownSquareSymbol= "\uD83D\uDFEB";
    static String blackSquareSymbol= "\u2B1B";
    static String yellowSquareSymbol = "\uD83D\uDFE8";
    static String blueCircleSymbol= "\uD83D\uDD35";
    static String greenCircleSymbol= "\uD83D\uDFE2";
    static String yellowCircleSymbol= "\uD83D\uDFE1";
    static String redCircleSymbol= "\uD83D\uDD34";
    static String onePointSymbol= "1️⃣";
    static String twoPointsSymbol= "2️⃣";

    static String threePointsSymbol= "3️⃣";

    static String fivePointsSymbol = "5️⃣";
    static String coveredAnglesSymbol = "\u25F0";
    static String forEachSymbol ="\u2755";

// TODO:  Fix DrawCards On table based on the new structure in ClientController. (When we introduce a new class in client.Controller containing things regarding table)
// TODO:  implement TUI in case there is no deck or some drawable cards / Cards in hands are missing. (When we decide how to say there is some missing cards)
    //TODO:  testing. (When we fix infoTraslator)
    //TODO: SketchGoldenCard method (When we decide if it is possible to have a list of symbol as recquirements or if it is better to have a big switch statement similar to what we have for ObjectiveCards)
    //TODO: Evaluate whether or not certain attribute like the name are needed in Objective card.
    //TODO: change also DrawMap with new structure (When we are sure this infoTraslator works properly)


    public void drawMap(Player player) {

        System.out.flush();

        // 1 find the max and min row and column in order to create a matrix of the right dimension
        int minX = 0; // x of the leftmost cell
        int maxX = 0; // x of the rightmost cell
        int minY = 0; // y of the nethermost cell
        int maxY = 0; // y of the topmost  cell

        //
        for (Point p : player.getField().getCards().keySet()) {
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

        // we need to consider maxX+1 because the rightmost card also needs to have a right edge, same is true for minX, minY, maxY
        maxX++;
        minX--;
        maxY++;
        minY--;
        //the +1 is for the central cell (0,0)
        int rows = maxY - minY +1;
        int columns = maxX - minX +1;


        String[][] matrixMap = new String[rows][columns];

        //2 Populate the array with black square
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrixMap[i][j] = blackSquareSymbol;
            }
        }


        //3 Populate maps with the correct card color and add central symbol if the orientation of the card is back
        for(Point p: player.getField().getCards().keySet())
        {
            String centralSymbol=animalSymbol; //We need this to keep track of what we need to put in the central cell of the card in case the card is plaued on the back.
            String cardColor=animalSymbol;
            if(player.getField().getCards().get(p).getCardColor() == CardColor.SKYBLUE)
            {
                cardColor=blueSquareSymbol;
                centralSymbol=animalSymbol;
            }
            else if(player.getField().getCards().get(p).getCardColor() == CardColor.RED)
            {
                cardColor=redSquareSymbol;
                centralSymbol=fungiSymbol;
            }
            else if(player.getField().getCards().get(p).getCardColor() == CardColor.GREEN)
            {
                cardColor=greenSquareSymbol;
                centralSymbol=plantSymbol;
            }
            else if(player.getField().getCards().get(p).getCardColor() == CardColor.PURPLE)
            {
                cardColor=purpleSquareSymbol;
                centralSymbol=insectSymbol;
            }
            else if(player.getField().getCards().get(p).getCardColor() == CardColor.WHITE)
            {
                cardColor=brownSquareSymbol; // initial card
            }
            if(player.getField().getCardCells().get(p).visibleCardSide().equals(player.getField().getCards().get(p).getSide(CardOrientation.FRONT))) {
                matrixMap[p.y() + abs(minY)][p.x() + abs(minX)] = cardColor;
            }
            else{
                matrixMap[p.y() + abs(minY)][p.x() + abs(minX)] = centralSymbol;
            }

            matrixMap[p.y()+abs(minY)+1][p.x()+abs(minX)]=cardColor;
            matrixMap[p.y()+abs(minY)][p.x()+abs(minX)+1]=cardColor;
            matrixMap[p.y()+abs(minY)-1][p.x()+abs(minX)]=cardColor;
            matrixMap[p.y()+abs(minY)][p.x()+abs(minX)-1]=cardColor;
        }


        //4 Populate maps with the correct symbol.
        for(Point p: player.getField().getAnglesSymbols().keySet())
        {
            String symbol=whiteSquareSymbol;
            if(player.getField().getAnglesSymbols().get(p) == Symbol.ANIMAL)
            {
                symbol=animalSymbol;
            }
            else if(player.getField().getAnglesSymbols().get(p) == Symbol.PLANT)
            {
                symbol=plantSymbol;
            }
            else if(player.getField().getAnglesSymbols().get(p) == Symbol.INSECT)
            {
                symbol=insectSymbol;
            }
            else if(player.getField().getAnglesSymbols().get(p) == Symbol.FUNGI)
            {
                symbol=fungiSymbol;
            }
            else if(player.getField().getAnglesSymbols().get(p) == Symbol.INKWELL)
            {
                symbol=inkwellSymbol;
            }
            else if(player.getField().getAnglesSymbols().get(p) == Symbol.MANUSCRIPT)
            {
                symbol=manuscriptSymbol;
            }
            else if(player.getField().getAnglesSymbols().get(p) == Symbol.QUILL)
            {
                symbol=quillSymbol;
            }
            else if(player.getField().getAnglesSymbols().get(p) == Symbol.BLANK)
            {
                symbol=whiteSquareSymbol;
            }
            else if(player.getField().getAnglesSymbols().get(p) == Symbol.HIDDEN)
            {
                Point TopPositionCard=player.getField().getAngleCells().get(p).topCardPosition();
                if(player.getField().getCards().get(TopPositionCard).getCardColor()==CardColor.WHITE)
                {
                    symbol=brownSquareSymbol;
                }
                else if(player.getField().getCards().get(TopPositionCard).getCardColor()==CardColor.SKYBLUE)
                {
                    symbol=blueSquareSymbol;
                }
                else if(player.getField().getCards().get(TopPositionCard).getCardColor()==CardColor.GREEN)
                {
                    symbol=greenSquareSymbol;
                }
                else if(player.getField().getCards().get(TopPositionCard).getCardColor()==CardColor.RED)
                {
                    symbol=redSquareSymbol;
                }
                else if(player.getField().getCards().get(TopPositionCard).getCardColor()==CardColor.PURPLE)
                {
                    symbol=purpleSquareSymbol;
                }
            }
            matrixMap[p.y()+abs(minY)][p.x()+abs(minX)]=symbol;
        }

        //5 we add the player token in the middle of the map
        String playerSymbol=blueCircleSymbol; //we need to initialize it to not get strange error.
        if(player.getColor() == PlayerColor.BLUE)
        {
            playerSymbol=blueCircleSymbol;
        }
        else if(player.getColor() == PlayerColor.RED)
        {
            playerSymbol=redCircleSymbol;
        }
        else if(player.getColor() == PlayerColor.GREEN)
        {
            playerSymbol=greenCircleSymbol;
        }
        else if(player.getColor() == PlayerColor.YELLOW)
        {
            playerSymbol=yellowCircleSymbol;
        }
        matrixMap[abs(minY)][abs(minX)]=playerSymbol;

        //6 Display the contents of the matrixMap
        for (int i = rows-1; i >= 0; i--){
            for (int j = 0; j < columns; j++) {
                System.out.print(matrixMap[i][j]);
            }
            System.out.println(); // Move to the next row
        }

        //7 We print out what's underneath the starting symbol
        Point origin = new Point(0,0);
        System.out.println("list of the symbol in the middle of the starting card: " + player.getField().getCardCells().get(origin).visibleCardSide().getCenterSymbols());
    }

    public void showPoints(OpponentInfo player) {
        System.out.println("Player " + player.nickname() + " has "+ player.score() + " points");
    }
    public void showHand(ControlledPlayerInfo player)
    {
        String[][] matrixHand = new String[3][23]; // 20 columns for 3 cards +3 cells for tabs

        for(int i=0; i<3; i++) {
            for(int j=0; j<23; j++){
                matrixHand[i][j]="\t\t"; //I add the space between the card.
            }
        }

        for(int i=0; i<3; i++) {
            String[][] currentCard = new String[3][5];
            currentCard=sketchCard(player.cards().get(i));
            for(int j=0; j<3; j++){
                for(int k=0; k<5; k++) {
                    matrixHand[j][k+6*i]=currentCard[j][k];
                }
            }
        }
        String[][] currentCard = new String[3][5];
        currentCard=sketchObjectiveCard(player.secretObjective());
        for(int j=0; j<3; j++){
            for(int k=0; k<5; k++) {
                matrixHand[j][k+17]=currentCard[j][k];
            }
        }

        for(int i=0; i<3; i++) {
            for(int j=0; j<23; j++){
                System.out.print(matrixHand[i][j]);
            }
            System.out.println();
        }

    }
    public void showCardsOnTable(CardColor colorGoldDeck, CardColor colorResourceDeck)
    {
        CardInfo resourceCard1=game.getDrawableCards().get(DrawChoice.RESOURCE_CARD_1);
        CardInfo resourceCard2=game.getDrawableCards().get(DrawChoice.RESOURCE_CARD_2);
        CardInfo goldenCard1=game.getDrawableCards().get(DrawChoice.GOLD_CARD_1);
        CardInfo goldenCard2=game.getDrawableCards().get(DrawChoice.GOLD_CARD_2);
        ObjectiveInfo objectiveCard1=game.getCommonObjectiveCards().get(0);
        ObjectiveInfo objectiveCard2=game.getCommonObjectiveCards().get(1);


        String[][] decks = new String[3][11]; // 11 columns for 2 cards +1 cells for tabs
        String[][] drawableCards1 = new String[3][11]; // 1st row of card that are face up on the table to draw from
        String[][] drawableCards2 = new String[3][11];
        String[][] objectives = new String[3][11];


        //I add the space between the card.
        for(int i=0; i<3; i++) {
            for(int j=0; j<11; j++){
                decks[i][j]="\t\t";
                drawableCards1[i][j]="\t\t";
                drawableCards2[i][j]="\t\t";
                objectives[i][j]="\t\t";
            }
        }

        String[][] Card1 = new String[3][5];
        String[][] Card2 = new String[3][5];

        Card1=sketchGoldenDeck();
        Card2=sketchResourceDeck();

        for(int j=0; j<3; j++){
            for(int k=0; k<5; k++) {
                decks[j][k]=Card1[j][k];
                decks[j][k+6]=Card1[j][k];
            }
        }

        Card1=sketchCard(resourceCard1);
        Card2=sketchCard(goldenCard1);

        for(int j=0; j<3; j++){
            for(int k=0; k<5; k++) {
                drawableCards1[j][k]=Card1[j][k];
                drawableCards1[j][k+6]=Card1[j][k];
            }
        }

        Card1=sketchCard(resourceCard2);
        Card2=sketchCard(goldenCard2);

        for(int j=0; j<3; j++){
            for(int k=0; k<5; k++) {
                drawableCards2[j][k]=Card2[j][k];
                drawableCards2[j][k+6]=Card2[j][k];
            }
        }

        Card1=sketchObjectiveCard(objectiveCard1);
        Card2=sketchObjectiveCard(objectiveCard2);

        for(int j=0; j<3; j++){
            for(int k=0; k<5; k++) {
                drawableCards2[j][k]=Card2[j][k];
                drawableCards2[j][k+6]=Card2[j][k];
            }
        }

        for(int i=0; i<3; i++) {
            for(int j=0; j<11; j++){
                System.out.print(decks[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n\n");

        for(int i=0; i<3; i++) {
            for(int j=0; j<11; j++){
                System.out.print(drawableCards1[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n\n");

        for(int i=0; i<3; i++) {
            for(int j=0; j<11; j++){
                System.out.print(drawableCards2[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n\n");

        for(int i=0; i<3; i++) {
            for(int j=0; j<11; j++){
                System.out.print(objectives[i][j]);
            }
            System.out.println();
        }
        System.out.print("\n\n");
    }

    public String[][] sketchCard(CardInfo card){
        if(card.id()<=40) {
            return sketchResourceCard(card);
        }
        else if(card.id()>40 && card.id()<=80) {
            return sketchGoldenCard(card);
        }
        else {
            System.out.println("Error, card not supported by this method");
            return null;
        }
    }

    public String[][] sketchBackground(CardColor color) {
        String[][] cardSketched = new String[3][5];
        String background=blackSquareSymbol;
        switch (color) {
            case CardColor.RED:
                background = redSquareSymbol;
                break;
            case CardColor.GREEN:
                background = greenSquareSymbol;
                break;
            case CardColor.PURPLE:
                background = purpleSquareSymbol;
                break;
            case CardColor.SKYBLUE:
                background = blueSquareSymbol;
                break;
            default:
                System.out.println("Error");
                break;
        }
        for(int i=0; i<3; i++){
            for(int j=0; j<5; j++){
                cardSketched[i][j]=background;
            }
        }
        return  cardSketched;
    }

    public String[][] sketchResourceCard(CardInfo card) {
        String[][] cardSketched = new String[3][5];
        cardSketched=sketchBackground(card.color());

        // I don't need to check the orientation since all card displayed in hand/ in play are on the front side.
        for(int i=0; i<4; i++) {
            String symbol=blackSquareSymbol;
            AnglePosition angle = AnglePosition.UP_LEFT;
            int x=0;
            int y=0;
            switch (i) {
            case 0:
                angle=AnglePosition.UP_LEFT;
                x=0;
                y=0;
                break;
            case 1:
                angle=AnglePosition.UP_RIGHT;
                x=4;
                y=0;
                break;
            case 2:
                angle=AnglePosition.DOWN_LEFT;
                x=0;
                y=2;
                break;
            case 3:
                angle=AnglePosition.DOWN_RIGHT;
                x=4;
                y=2;
                break;
            }
            switch (card.getSide(CardOrientation.FRONT).angleSymbols().get(angle)){
                case Symbol.ANIMAL:
                    symbol=animalSymbol;
                    break;
                case Symbol.FUNGI:
                    symbol=fungiSymbol;
                    break;
                case Symbol.PLANT:
                    symbol=plantSymbol;
                    break;
                case Symbol.INSECT:
                    symbol=insectSymbol;
                    break;
                case Symbol.QUILL:
                    symbol=quillSymbol;
                    break;
                case Symbol.MANUSCRIPT:
                    symbol=manuscriptSymbol;
                    break;
                case Symbol.INKWELL:
                    symbol=inkwellSymbol;
                    break;
                case Symbol.BLANK:
                    symbol=whiteSquareSymbol;
                    break;
            }
            // if it is hidden I don't want to change emoji from the background color.
            if(card.getSide(CardOrientation.FRONT).angleSymbols().get(angle) != Symbol.HIDDEN) {
                cardSketched[y][x] = symbol;
            }
        }
        return cardSketched;
    }

    public String[][] sketchGoldenCard(CardInfo card) {
        String[][] cardSketched = new String[3][5];

        return cardSketched;
    }


    public String[][] sketchObjectiveCard(ObjectiveInfo card){
        String[][] cardSketched = new String[3][5];
        // I set every cell to black (background) so I will change only the cells that I need to change. I can't use the
        // set background function since black is not on the "normal color card" but since every time the color
        // chosen for the objective card for the background is always black it is sufficient to implement a nested for as it follows:

        for(int i=0; i<3; i++){
            for(int j=0; j<5; j++){
                cardSketched[i][j]=blackSquareSymbol;
            }
        }
        switch (card.id()) {
            case 87:
                cardSketched[0][4]=twoPointsSymbol;
                cardSketched[0][3]=redSquareSymbol;
                cardSketched[1][2]=redSquareSymbol;
                cardSketched[2][1]=redSquareSymbol;
                break;
            case 88:
                cardSketched[0][4]=twoPointsSymbol;
                cardSketched[0][1]=greenSquareSymbol;
                cardSketched[1][2]=greenSquareSymbol;
                cardSketched[2][3]=greenSquareSymbol;
                break;
            case 89:
                cardSketched[0][4]=twoPointsSymbol;
                cardSketched[0][3]=blueSquareSymbol;
                cardSketched[1][2]=blueSquareSymbol;
                cardSketched[2][1]=blueSquareSymbol;
                break;
            case 90:
                cardSketched[0][4]=twoPointsSymbol;
                cardSketched[0][1]=purpleSquareSymbol;
                cardSketched[1][2]=purpleSquareSymbol;
                cardSketched[2][3]=purpleSquareSymbol;
                break;
            case 91:
                cardSketched[0][4]=threePointsSymbol;
                cardSketched[0][2]=redSquareSymbol;
                cardSketched[1][2]=redSquareSymbol;
                cardSketched[2][3]=greenSquareSymbol;
                break;
            case 92:
                cardSketched[0][4]=threePointsSymbol;
                cardSketched[0][2]=greenSquareSymbol;
                cardSketched[1][2]=greenSquareSymbol;
                cardSketched[2][1]=purpleSquareSymbol;
                break;
            case 93:
                cardSketched[0][4]=threePointsSymbol;
                cardSketched[1][2]=blueSquareSymbol;
                cardSketched[2][2]=blueSquareSymbol;
                cardSketched[0][3]=purpleSquareSymbol;
                break;
            case 94:
                cardSketched[0][4]=threePointsSymbol;
                cardSketched[1][2]=purpleSquareSymbol;
                cardSketched[2][2]=purpleSquareSymbol;
                cardSketched[0][1]=blueSquareSymbol;
                break;
            case 95:
                cardSketched[0][4]=twoPointsSymbol;
                cardSketched[1][1]=fungiSymbol;
                cardSketched[1][2]=fungiSymbol;
                cardSketched[1][3]=fungiSymbol;
                break;
            case 96:
                cardSketched[0][4]=twoPointsSymbol;
                cardSketched[1][1]=plantSymbol;
                cardSketched[1][2]=plantSymbol;
                cardSketched[1][3]=plantSymbol;
                break;
            case 97:
                cardSketched[0][4]=twoPointsSymbol;
                cardSketched[1][1]=animalSymbol;
                cardSketched[1][2]=animalSymbol;
                cardSketched[1][3]=animalSymbol;
                break;
            case 98:
                cardSketched[0][4]=twoPointsSymbol;
                cardSketched[1][1]=insectSymbol;
                cardSketched[1][2]=insectSymbol;
                cardSketched[1][3]=insectSymbol;
                break;
            case 99:
                cardSketched[0][4]=threePointsSymbol;
                cardSketched[1][1]=quillSymbol;
                cardSketched[1][2]=inkwellSymbol;
                cardSketched[1][3]=manuscriptSymbol;
                break;
            case 100:
                cardSketched[0][4]=twoPointsSymbol;
                cardSketched[1][1]=manuscriptSymbol;
                cardSketched[1][2]=manuscriptSymbol;
                break;
            case 101:
                cardSketched[0][4]=twoPointsSymbol;
                cardSketched[1][1]=inkwellSymbol;
                cardSketched[1][2]=inkwellSymbol;
                break;
            case 102:
                cardSketched[0][4]=twoPointsSymbol;
                cardSketched[1][1]=quillSymbol;
                cardSketched[1][2]=quillSymbol;
                break;
            default:
                System.out.println("Error, idCard out of range");
                break;
        }
        return cardSketched;
    }

    String[][] sketchGoldenDeck(CardColor colorDeck){
        String[][] goldenDeck = new String[3][5];
        goldenDeck=sketchBackground(colorDeck);
        goldenDeck=addBlankAngles(goldenDeck);
        goldenDeck=addCentralSymbol(goldenDeck, colorDeck);
        goldenDeck[0][2]=yellowSquareSymbol;
        goldenDeck[1][1]=yellowSquareSymbol;
        goldenDeck[1][3]=yellowSquareSymbol;
        goldenDeck[2][2]=yellowSquareSymbol;
        return goldenDeck;
    }

    String[][] sketchResourceDeck(CardColor colorDeck){
        String[][] resourceDeck = new String[3][5];
        resourceDeck=sketchBackground(colorDeck);
        resourceDeck=addBlankAngles(resourceDeck);
        resourceDeck=addCentralSymbol(resourceDeck, colorDeck);
        return resourceDeck; 
    }

    String[][] addBlankAngles(String[][] sketchedCard) {
        sketchedCard[0][0]=whiteSquareSymbol;
        sketchedCard[0][4]=whiteSquareSymbol;
        sketchedCard[2][0]=whiteSquareSymbol;
        sketchedCard[2][4]=whiteSquareSymbol;
        return sketchedCard;
    }

    String[][] addCentralSymbol(String[][] sketchedCard,CardColor color){
        String symbol = blackSquareSymbol;
        switch (color){
            case CardColor.SKYBLUE:
                symbol=animalSymbol;
                break;
            case CardColor.RED:
                symbol=fungiSymbol;
                break;
            case CardColor.GREEN:
                symbol=plantSymbol;
                break;
            case CardColor.PURPLE:
                symbol=insectSymbol;
                break;
            default:
                System.out.println("Invalid card as input");
        }
        sketchedCard[1][2]=symbol;
        return sketchedCard;
    }




}