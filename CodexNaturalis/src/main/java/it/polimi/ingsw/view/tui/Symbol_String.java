package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.card.Symbol;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to get the right emoji to print in the code.
 */
public class Symbol_String {
    public static final String FUNGI_SYMBOL= "\uD83C\uDF44";
    public static final String ANIMAL_SYMBOL= "\uD83D\uDC3A";
    public static final String INSECT_SYMBOL= "\uD83E\uDD8B";
    public static final String PLANT_SYMBOL= "\uD83C\uDF3F";
    public static final String MANUSCRIPT_SYMBOL= "\uD83D\uDCDD";
    public static final String QUILL_SYMBOL= "\uD83E\uDEB6";
    public static final String INKWELL_SYMBOL= "\uD83E\uDED9";
    public static final String WHITE_SQUARE_SYMBOL= "\u26AA";
    public static final String RED_SQUARE_SYMBOL= "\uD83D\uDFE5";
    public static final String GREEN_SQUARE_SYMBOL= "\uD83D\uDFE9";
    public static final String BLUE_SQUARE_SYMBOL= "\uD83D\uDFE6";
    public static final String PURPLE_SQUARE_SYMBOL= "\uD83D\uDFEA";
    public static final String BROWN_SQUARE_SYMBOL= "\uD83D\uDFEB";
    public static final String BLACK_SQUARE_SYMBOL= "\u26AB";
    public static final String YELLOW_SQUARE_SYMBOL = "\uD83D\uDFE8";
    public static final String BLUE_CIRCLE_SYMBOL= "\uD83D\uDD35";
    public static final String GREEN_CIRCLE_SYMBOL= "\uD83D\uDFE2";
    public static final String YELLOW_CIRCLE_SYMBOL= "\uD83D\uDFE1";
    public static final String RED_CIRCLE_SYMBOL= "\uD83D\uDD34";
    public static final String COVERED_ANGLES_SYMBOL = "\u25F0 ";
    public static final String FOR_EACH_SYMBOL ="\u2755";

    /**
     *  A method to convert an int into a string in such a way that  always takes up 2 char. eg int 7 will be converted into " 7"
      */

    public static String FromIntToString(int n){
        String temp = String.valueOf(n);
        String result;
        if(n<10){
            result=" " + temp;
        }
        else {
            result=temp;
        }
        return result;
    }


    /**
     *
     * @param symbol the symbol in an enum we want to convert into an emoji
     * @return the char emoji corresponding to that enum-symbol
     */
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
            default:
                //probably there is a hidden symbol but in this situation I do nothing.
        }
        return CharSymbol;
    }

}
