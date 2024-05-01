package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.CardOrientation;
import it.polimi.ingsw.model.card.Symbol;
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

}