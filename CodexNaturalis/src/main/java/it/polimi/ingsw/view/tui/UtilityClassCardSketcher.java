package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.controller.clientcontroller.CardType;
import it.polimi.ingsw.dataobject.CardSideInfo;
import it.polimi.ingsw.dataobject.ObjectiveInfo;
import it.polimi.ingsw.dataobject.RewardType;
import it.polimi.ingsw.model.card.AnglePosition;
import it.polimi.ingsw.model.card.CardColor;
import it.polimi.ingsw.model.card.Symbol;

public final class UtilityClassCardSketcher {
    
    private UtilityClassCardSketcher (){}; 
    public static String[][] sketchCard(CardSideInfo card){
        if(card.Type()== CardType.RESOURCE) {
            return sketchResourceCard(card);
        }
        else if(card.Type()==CardType.GOLD) {
            return sketchGoldenCard(card);
        }
        else {
            System.out.println("Error, card not supported by this method");
            return null;
        }
    }

    public static String[][] sketchEmptyCard() {
        String[][] cardSketched = new String[3][5];
        for(int i=0; i<3; i++){
            for(int j=0; j<5; j++){
                cardSketched[i][j]=Symbol_String.BLACK_SQUARE_SYMBOL;
            }
        }
        return cardSketched;
    }

    public static String[][] sketchBackground(CardColor color) {
        String[][] cardSketched = new String[3][5];
        String background=Symbol_String.BLACK_SQUARE_SYMBOL;
        switch (color) {
            case CardColor.RED:
                background = Symbol_String.RED_SQUARE_SYMBOL;
                break;
            case CardColor.GREEN:
                background = Symbol_String.GREEN_SQUARE_SYMBOL;
                break;
            case CardColor.PURPLE:
                background = Symbol_String.PURPLE_SQUARE_SYMBOL;
                break;
            case CardColor.SKYBLUE:
                background = Symbol_String.BLUE_SQUARE_SYMBOL;
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

    public static String[][] sketchResourceCard(CardSideInfo card) {
        String[][] cardSketched = new String[3][5];
        cardSketched=sketchBackground(card.color());
        for(int i=0; i<4; i++) {
            String symbol=Symbol_String.BLACK_SQUARE_SYMBOL;
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
            symbol=FromSymbolToString(card.angleSymbols().get(angle));
            // if it is hidden I don't want to change emoji from the background color.
            if(card.angleSymbols().get(angle) != Symbol.HIDDEN) {
                cardSketched[y][x] = symbol;
            }
        }
        if(card.reward() == RewardType.ONE_POINT) {
            cardSketched[0][2]=Symbol_String.ONE_SYMBOL;
        }

        return cardSketched;
    }



    public static String[][] sketchGoldenCard(CardSideInfo card) {
        String[][] cardSketched = new String[3][5];
        cardSketched=sketchResourceCard(card); // Golden card doesn't differ from Golden Card In terms of angles
        for(int i=0; i<card.requiredSymbols().size(); i++){
            if(i==0) {
                cardSketched[2][1] = FromSymbolToString(card.requiredSymbols().get(i));
            }
            else if(i==1) {
                cardSketched[2][2] = FromSymbolToString(card.requiredSymbols().get(i));
            }
            else if(i==2) {
                cardSketched[2][3] = FromSymbolToString(card.requiredSymbols().get(i));
            }
            else if(i==3) {
                cardSketched[1][2] = FromSymbolToString(card.requiredSymbols().get(i));
            }
            else if(i==4) {
                cardSketched[1][1] = FromSymbolToString(card.requiredSymbols().get(i));
            }
        }


        switch (card.reward()) {
            case RewardType.ONE_POINT:
                cardSketched[0][2]=Symbol_String.ONE_SYMBOL;
                break;
            case RewardType.THREE_POINTS:
                cardSketched[0][2]=Symbol_String.THREE_SYMBOL;
                break;
            case RewardType.FIVE_POINTS:
                cardSketched[0][2]=Symbol_String.FIVE_SYMBOL;
                break;
            case RewardType.POINT_PER_QUILL:
                cardSketched[0][1]=Symbol_String.ONE_SYMBOL;
                cardSketched[0][2]=Symbol_String.FOR_EACH_SYMBOL;
                cardSketched[0][3]=Symbol_String.QUILL_SYMBOL;
                break;
            case RewardType.POINT_PER_INKWELL:
                cardSketched[0][1]=Symbol_String.ONE_SYMBOL;
                cardSketched[0][2]=Symbol_String.FOR_EACH_SYMBOL;
                cardSketched[0][3]=Symbol_String.INKWELL_SYMBOL;
                break;
            case RewardType.POINT_PER_MANUSCRIPT:
                cardSketched[0][1]=Symbol_String.ONE_SYMBOL;
                cardSketched[0][2]=Symbol_String.FOR_EACH_SYMBOL;
                cardSketched[0][3]=Symbol_String.MANUSCRIPT_SYMBOL;
                break;
            case RewardType.POINTS_PER_COVERED_ANGLE:
                cardSketched[0][1]=Symbol_String.TWO_SYMBOL;
                cardSketched[0][2]=Symbol_String.FOR_EACH_SYMBOL;
                cardSketched[0][3]=Symbol_String.COVERED_ANGLES_SYMBOL;
                break;
        }

        return cardSketched;
    }

    public static String FromSymbolToString(Symbol symbol){
        String CharSymbol=Symbol_String.BLACK_SQUARE_SYMBOL;
        switch (symbol){
            case Symbol.ANIMAL:
                CharSymbol=Symbol_String.ANIMAL_SYMBOL;
                break;
            case Symbol.FUNGI:
                CharSymbol=Symbol_String.FUNGI_SYMBOL;
                break;
            case Symbol.PLANT:
                CharSymbol=Symbol_String.PLANT_SYMBOL;
                break;
            case Symbol.INSECT:
                CharSymbol=Symbol_String.INSECT_SYMBOL;
                break;
            case Symbol.QUILL:
                CharSymbol=Symbol_String.QUILL_SYMBOL;
                break;
            case Symbol.MANUSCRIPT:
                CharSymbol=Symbol_String.MANUSCRIPT_SYMBOL;
                break;
            case Symbol.INKWELL:
                CharSymbol=Symbol_String.INKWELL_SYMBOL;
                break;
            case Symbol.BLANK:
                CharSymbol=Symbol_String.WHITE_SQUARE_SYMBOL;
                break;
        }
        return CharSymbol;
    }




    public static String[][] sketchObjectiveCard(ObjectiveInfo card){
        String[][] cardSketched = new String[3][5];
        // I set every cell to black (background) so I will change only the cells that I need to change. I can't use the
        // set background function since black is not on the "normal color card" but since every time the color
        // chosen for the objective card for the background is always black it is sufficient to implement a nested for as it follows:

        cardSketched=sketchEmptyCard(); //since the bakground of an objective card is black I can use this method to draw objective background.
        switch (card.id()) {
            case 87:
                cardSketched[0][4]=Symbol_String.TWO_SYMBOL;
                cardSketched[0][3]=Symbol_String.RED_SQUARE_SYMBOL;
                cardSketched[1][2]=Symbol_String.RED_SQUARE_SYMBOL;
                cardSketched[2][1]=Symbol_String.RED_SQUARE_SYMBOL;
                break;
            case 88:
                cardSketched[0][4]=Symbol_String.TWO_SYMBOL;
                cardSketched[0][1]=Symbol_String.GREEN_SQUARE_SYMBOL;
                cardSketched[1][2]=Symbol_String.GREEN_SQUARE_SYMBOL;
                cardSketched[2][3]=Symbol_String.GREEN_SQUARE_SYMBOL;
                break;
            case 89:
                cardSketched[0][4]=Symbol_String.TWO_SYMBOL;
                cardSketched[0][3]=Symbol_String.BLUE_SQUARE_SYMBOL;
                cardSketched[1][2]=Symbol_String.BLUE_SQUARE_SYMBOL;
                cardSketched[2][1]=Symbol_String.BLUE_SQUARE_SYMBOL;
                break;
            case 90:
                cardSketched[0][4]=Symbol_String.TWO_SYMBOL;
                cardSketched[0][1]=Symbol_String.PURPLE_SQUARE_SYMBOL;
                cardSketched[1][2]=Symbol_String.PURPLE_SQUARE_SYMBOL;
                cardSketched[2][3]=Symbol_String.PURPLE_SQUARE_SYMBOL;
                break;
            case 91:
                cardSketched[0][4]=Symbol_String.THREE_SYMBOL;
                cardSketched[0][2]=Symbol_String.RED_SQUARE_SYMBOL;
                cardSketched[1][2]=Symbol_String.RED_SQUARE_SYMBOL;
                cardSketched[2][3]=Symbol_String.GREEN_SQUARE_SYMBOL;
                break;
            case 92:
                cardSketched[0][4]=Symbol_String.THREE_SYMBOL;
                cardSketched[0][2]=Symbol_String.GREEN_SQUARE_SYMBOL;
                cardSketched[1][2]=Symbol_String.GREEN_SQUARE_SYMBOL;
                cardSketched[2][1]=Symbol_String.PURPLE_SQUARE_SYMBOL;
                break;
            case 93:
                cardSketched[0][4]=Symbol_String.THREE_SYMBOL;
                cardSketched[1][2]=Symbol_String.BLUE_SQUARE_SYMBOL;
                cardSketched[2][2]=Symbol_String.BLUE_SQUARE_SYMBOL;
                cardSketched[0][3]=Symbol_String.RED_SQUARE_SYMBOL;
                break;
            case 94:
                cardSketched[0][4]=Symbol_String.THREE_SYMBOL;
                cardSketched[1][2]=Symbol_String.PURPLE_SQUARE_SYMBOL;
                cardSketched[2][2]=Symbol_String.PURPLE_SQUARE_SYMBOL;
                cardSketched[0][1]=Symbol_String.BLUE_SQUARE_SYMBOL;
                break;
            case 95:
                cardSketched[0][4]=Symbol_String.TWO_SYMBOL;
                cardSketched[1][1]=Symbol_String.FUNGI_SYMBOL;
                cardSketched[1][2]=Symbol_String.FUNGI_SYMBOL;
                cardSketched[1][3]=Symbol_String.FUNGI_SYMBOL;
                break;
            case 96:
                cardSketched[0][4]=Symbol_String.TWO_SYMBOL;
                cardSketched[1][1]=Symbol_String.PLANT_SYMBOL;
                cardSketched[1][2]=Symbol_String.PLANT_SYMBOL;
                cardSketched[1][3]=Symbol_String.PLANT_SYMBOL;
                break;
            case 97:
                cardSketched[0][4]=Symbol_String.TWO_SYMBOL;
                cardSketched[1][1]=Symbol_String.ANIMAL_SYMBOL;
                cardSketched[1][2]=Symbol_String.ANIMAL_SYMBOL;
                cardSketched[1][3]=Symbol_String.ANIMAL_SYMBOL;
                break;
            case 98:
                cardSketched[0][4]=Symbol_String.TWO_SYMBOL;
                cardSketched[1][1]=Symbol_String.INSECT_SYMBOL;
                cardSketched[1][2]=Symbol_String.INSECT_SYMBOL;
                cardSketched[1][3]=Symbol_String.INSECT_SYMBOL;
                break;
            case 99:
                cardSketched[0][4]=Symbol_String.THREE_SYMBOL;
                cardSketched[1][1]=Symbol_String.QUILL_SYMBOL;
                cardSketched[1][2]=Symbol_String.INKWELL_SYMBOL;
                cardSketched[1][3]=Symbol_String.MANUSCRIPT_SYMBOL;
                break;
            case 100:
                cardSketched[0][4]=Symbol_String.TWO_SYMBOL;
                cardSketched[1][1]=Symbol_String.MANUSCRIPT_SYMBOL;
                cardSketched[1][2]=Symbol_String.MANUSCRIPT_SYMBOL;
                break;
            case 101:
                cardSketched[0][4]=Symbol_String.TWO_SYMBOL;
                cardSketched[1][1]=Symbol_String.INKWELL_SYMBOL;
                cardSketched[1][2]=Symbol_String.INKWELL_SYMBOL;
                break;
            case 102:
                cardSketched[0][4]=Symbol_String.TWO_SYMBOL;
                cardSketched[1][1]=Symbol_String.QUILL_SYMBOL;
                cardSketched[1][2]=Symbol_String.QUILL_SYMBOL;
                break;
            default:
                System.out.println("Error, idCard out of range");
                break;
        }
        return cardSketched;
    }

    public static String[][] sketchGoldenDeck(CardColor colorDeck){
        String[][] goldenDeck = new String[3][5];
        goldenDeck=sketchBackground(colorDeck);
        goldenDeck=addBlankAngles(goldenDeck);
        goldenDeck=addCentralSymbol(goldenDeck, colorDeck);
        goldenDeck[0][2]=Symbol_String.YELLOW_SQUARE_SYMBOL;
        goldenDeck[1][1]=Symbol_String.YELLOW_SQUARE_SYMBOL;
        goldenDeck[1][3]=Symbol_String.YELLOW_SQUARE_SYMBOL;
        goldenDeck[2][2]=Symbol_String.YELLOW_SQUARE_SYMBOL;
        return goldenDeck;
    }

    public static String[][] sketchResourceDeck(CardColor colorDeck){
        String[][] resourceDeck = new String[3][5];
        resourceDeck=sketchBackground(colorDeck);
        resourceDeck=addBlankAngles(resourceDeck);
        resourceDeck=addCentralSymbol(resourceDeck, colorDeck);
        return resourceDeck;
    }

    public static String[][] addBlankAngles(String[][] sketchedCard) {
        sketchedCard[0][0]=Symbol_String.WHITE_SQUARE_SYMBOL;
        sketchedCard[0][4]=Symbol_String.WHITE_SQUARE_SYMBOL;
        sketchedCard[2][0]=Symbol_String.WHITE_SQUARE_SYMBOL;
        sketchedCard[2][4]=Symbol_String.WHITE_SQUARE_SYMBOL;
        return sketchedCard;
    }

    public static String[][] addCentralSymbol(String[][] sketchedCard,CardColor color){
        String symbol = Symbol_String.BLACK_SQUARE_SYMBOL;
        switch (color){
            case CardColor.SKYBLUE:
                symbol=Symbol_String.ANIMAL_SYMBOL;
                break;
            case CardColor.RED:
                symbol=Symbol_String.FUNGI_SYMBOL;
                break;
            case CardColor.GREEN:
                symbol=Symbol_String.PLANT_SYMBOL;
                break;
            case CardColor.PURPLE:
                symbol=Symbol_String.INSECT_SYMBOL;
                break;
            default:
                System.out.println("Invalid card as input");
        }
        sketchedCard[1][2]=symbol;
        return sketchedCard;
    }

}
