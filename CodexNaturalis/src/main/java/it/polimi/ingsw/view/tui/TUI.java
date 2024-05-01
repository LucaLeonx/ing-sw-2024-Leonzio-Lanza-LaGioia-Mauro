package it.polimi.ingsw.view.tui;

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

    public void showPoints(Player player) {
        System.out.println("Player " + player.getNickname() + " has "+ player.getScore() + " points");
    }
    public void showHand(Player player)
    {
        String[][] matrixHand = new String[3][23]; // 20 columns for 3 cards +2 cells for tabs


    }
    public void showCardsOnTable()
    {
        String[][] decks = new String[3][11]; // 11 columns for 2 cards +1 cells for tabs
        String[][] drawable1 = new String[3][11]; // 1st row of card that are face up on the table to draw from
        String[][] drawable2 = new String[3][11];
        String[][] objectives = new String[3][11];
    }

    public String[][] sketchCard(Card card){
        if(card.getId()<=40) {
            return sketchResourceCard(card);
        }
        else if(card.getId()>40 && card.getId()<=80) {
            return sketchGoldenCard(card);
        }
        else if(card.getId()>=87 && card.getId()<=102){
            return  sketchObjectiveCard(card);
        }


    }


    public String[][] sketchResourceCard(Card card) {
        String[][] cardDrawn = new String[3][5];
        String background;
        switch (card.getCardColor()) {
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
                cardDrawn[i][j]=blackSquareSymbol;
            }
        }

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
            switch (card.getSide(CardOrientation.FRONT).getSymbolFromAngle(angle)){
                case Symbol.ANIMAL:
                    symbol=animalSymbol;
                case Symbol.FUNGI:
                    symbol=fungiSymbol;
                case Symbol.PLANT:
                    symbol=plantSymbol;
                case Symbol.INSECT:
                    symbol=insectSymbol;
                case Symbol.QUILL:
                    symbol=quillSymbol;
                case Symbol.MANUSCRIPT:
                    symbol=manuscriptSymbol;
                case Symbol.INKWELL:
                    symbol=inkwellSymbol;
                case Symbol.BLANK:
                    symbol=whiteSquareSymbol;
            }
            // if it is hidden I don't want to change emoji from the background color.
            if(card.getSide(CardOrientation.FRONT).getSymbolFromAngle(angle) != Symbol.HIDDEN) {
                cardDrawn[y][x] = symbol;
            }
        }
        return cardDrawn;
    }

    public String[][] sketchGoldenCard(Card card) {


    }


    public String[][] sketchObjectiveCard(Card card){
        String[][] cardDrawn = new String[3][5];
        // I set every cell to black (background) so I will change only the cells that I need to change
        for(int i=0; i<3; i++){
            for(int j=0; j<5; j++){
                cardDrawn[i][j]=blackSquareSymbol;
            }
        }
        switch (card.getId()) {
            case 87:
                cardDrawn[0][4]=twoPointsSymbol;
                cardDrawn[0][3]=redSquareSymbol;
                cardDrawn[1][2]=redSquareSymbol;
                cardDrawn[2][1]=redSquareSymbol;
                break;
            case 88:
                cardDrawn[0][4]=twoPointsSymbol;
                cardDrawn[0][1]=greenSquareSymbol;
                cardDrawn[1][2]=greenSquareSymbol;
                cardDrawn[2][3]=greenSquareSymbol;
                break;
            case 89:
                cardDrawn[0][4]=twoPointsSymbol;
                cardDrawn[0][3]=blueSquareSymbol;
                cardDrawn[1][2]=blueSquareSymbol;
                cardDrawn[2][1]=blueSquareSymbol;
                break;
            case 90:
                cardDrawn[0][4]=twoPointsSymbol;
                cardDrawn[0][1]=purpleSquareSymbol;
                cardDrawn[1][2]=purpleSquareSymbol;
                cardDrawn[2][3]=purpleSquareSymbol;
                break;
            case 91:
                cardDrawn[0][4]=threePointsSymbol;
                cardDrawn[0][2]=redSquareSymbol;
                cardDrawn[1][2]=redSquareSymbol;
                cardDrawn[2][3]=greenSquareSymbol;
                break;
            case 92:
                cardDrawn[0][4]=threePointsSymbol;
                cardDrawn[0][2]=greenSquareSymbol;
                cardDrawn[1][2]=greenSquareSymbol;
                cardDrawn[2][1]=purpleSquareSymbol;
                break;
            case 93:
                cardDrawn[0][4]=threePointsSymbol;
                cardDrawn[1][2]=blueSquareSymbol;
                cardDrawn[2][2]=blueSquareSymbol;
                cardDrawn[0][3]=purpleSquareSymbol;
                break;
            case 94:
                cardDrawn[0][4]=threePointsSymbol;
                cardDrawn[1][2]=purpleSquareSymbol;
                cardDrawn[2][2]=purpleSquareSymbol;
                cardDrawn[0][1]=blueSquareSymbol;
                break;
            case 95:
                cardDrawn[0][4]=twoPointsSymbol;
                cardDrawn[1][1]=fungiSymbol;
                cardDrawn[1][2]=fungiSymbol;
                cardDrawn[1][3]=fungiSymbol;
                break;
            case 96:
                cardDrawn[0][4]=twoPointsSymbol;
                cardDrawn[1][1]=plantSymbol;
                cardDrawn[1][2]=plantSymbol;
                cardDrawn[1][3]=plantSymbol;
                break;
            case 97:
                cardDrawn[0][4]=twoPointsSymbol;
                cardDrawn[1][1]=animalSymbol;
                cardDrawn[1][2]=animalSymbol;
                cardDrawn[1][3]=animalSymbol;
                break;
            case 98:
                cardDrawn[0][4]=twoPointsSymbol;
                cardDrawn[1][1]=insectSymbol;
                cardDrawn[1][2]=insectSymbol;
                cardDrawn[1][3]=insectSymbol;
                break;
            case 99:
                cardDrawn[0][4]=threePointsSymbol;
                cardDrawn[1][1]=quillSymbol;
                cardDrawn[1][2]=inkwellSymbol;
                cardDrawn[1][3]=manuscriptSymbol;
                break;
            case 100:
                cardDrawn[0][4]=twoPointsSymbol;
                cardDrawn[1][1]=manuscriptSymbol;
                cardDrawn[1][2]=manuscriptSymbol;
                break;
            case 101:
                cardDrawn[0][4]=twoPointsSymbol;
                cardDrawn[1][1]=inkwellSymbol;
                cardDrawn[1][2]=inkwellSymbol;
                break;
            case 102:
                cardDrawn[0][4]=twoPointsSymbol;
                cardDrawn[1][1]=quillSymbol;
                cardDrawn[1][2]=quillSymbol;
                break;
            default:
                System.out.println("Error, idCard out of range");
                break;
        }
        return cardDrawn;
    }

    //public String[][] drawCard( card) {

    //}


}