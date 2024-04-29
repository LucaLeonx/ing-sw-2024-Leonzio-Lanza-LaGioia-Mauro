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
    static String plantSymbol= "\uD83C\uDF3F ";
    static String manuscriptSymbol= "\uD83D\uDCDD";
    static String quillSymbol= "\uD83E\uDEB6";
    static String inkwellSymbol= "\uD83E\uDED9";
    static String whiteSquareSymbol= "\u2B1C";
    static String redSquareSymbol= "\uD83D\uDFE5";
    static String greenSquareSymbol= "\uD83D\uDFE9";
    static String blueSquareSymbol= "\uD83D\uDFE6";
    static String purpleSquareSymbol= "\uD83D\uDFEA";
    static String goldSquareSymbol= "\uD83D\uDFE8";
    static String blackSquareSymbol= "\u2B1B";
    static String blueCircleSymbol= "\uD83D\uDD35";
    static String greenCircleSymbol= "\uD83D\uDFE2";
    static String yellowCircleSymbol= "\uD83D\uDFE1";
    static String redCircleSymbol= "\uD83D\uDD34";

    public void drawMap(Player player) {
        /*debugging lines to make sure the unicode is well written
        System.out.println(fungiSymbol + animalSymbol + insectSymbol + plantSymbol + manuscriptSymbol + quillSymbol + inkwellSymbol);
        System.out.println(whiteSquareSymbol +  redSquareSymbol + greenSquareSymbol + blueSquareSymbol + purpleSquareSymbol + goldSquareSymbol + blackSquareSymbol);
        System.out.println(blueCircleSymbol + greenCircleSymbol + yellowCircleSymbol + redCircleSymbol); */

        System.out.flush();

        // 1 find the max and min row and column in order to create a matrix of the right dimension
        int minX=0;
        int maxX=0;
        int minY=0;
        int maxY=0;

        // we used angles because in angles there will be the edge of the map, not in cards.
        for(Point p: player.getField().getAnglesSymbols().keySet())
        {
            if(p.x()<minX){
                minX=p.x();
            }
            if(p.x()>maxX){
                maxX=p.x();
            }
            if(p.y()<minY){
                minY=p.y();
            }
            if(p.y()>maxY){
                maxY=p.y();
            }
        }

        int rows=maxX-minX;
        int columns=maxY-minY;

        String[][] matrixMap = new String[rows][columns];

        //2 Populate the array with black square
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrixMap[i][j] = blackSquareSymbol;
            }
        }

        //3 Populate maps with the correct card color
        for(Point p: player.getField().getCards().keySet())
        {
            String cardColor=goldSquareSymbol;
            if(player.getField().getCards().get(p).getCardColor() == CardColor.SKYBLUE)
            {
                cardColor=blueSquareSymbol;
            }
            else if(player.getField().getCards().get(p).getCardColor() == CardColor.RED)
            {
                cardColor=redSquareSymbol;
            }
            else if(player.getField().getCards().get(p).getCardColor() == CardColor.GREEN)
            {
                cardColor=greenSquareSymbol;
            }
            else if(player.getField().getCards().get(p).getCardColor() == CardColor.PURPLE)
            {
                cardColor=purpleSquareSymbol;
            }
            else if(player.getField().getCards().get(p).getCardColor() == CardColor.WHITE)
            {
                cardColor=goldSquareSymbol; // initial card
            }
            matrixMap[p.x()+abs(minX)][p.y()+abs(minY)]=cardColor;
            //we color in an anticlockwise starting from (+1,0) not considering the angle.
            matrixMap[p.x()+abs(minX)+1][p.y()+abs(minY)]=cardColor;
            matrixMap[p.x()+abs(minX)][p.y()+abs(minY)+1]=cardColor;;
            matrixMap[p.x()+abs(minX)-1][p.y()+abs(minY)]=cardColor;
            matrixMap[p.x()+abs(minX)][p.y()+abs(minY)-1]=cardColor;
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
                    symbol=goldSquareSymbol;
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
            matrixMap[p.x()+abs(minX)][p.y()+abs(minY)]=symbol;
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
        matrixMap[abs(minX)][abs(minY)]=playerSymbol;

        //6 Display the contents of the matrixMap
        for (int i = 0; i < rows; i++) {
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