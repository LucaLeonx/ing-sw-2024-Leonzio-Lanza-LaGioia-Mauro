package it.polimi.ingsw.view.tui;

import it.polimi.ingsw.model.card.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Symbol_String {
    public static final String FUNGI_SYMBOL= "\uD83C\uDF44";
    public static final String ANIMAL_SYMBOL= "\uD83D\uDC3A";
    public static final String INSECT_SYMBOL= "\uD83E\uDD8B";
    public static final String PLANT_SYMBOL= "\uD83C\uDF3F";
    public static final String MANUSCRIPT_SYMBOL= "\uD83D\uDCDD";
    public static final String QUILL_SYMBOL= "\uD83E\uDEB6";
    public static final String INKWELL_SYMBOL= "\uD83E\uDED9";
    public static final String WHITE_SQUARE_SYMBOL= "\u2B1C";
    public static final String RED_SQUARE_SYMBOL= "\uD83D\uDFE5";
    public static final String GREEN_SQUARE_SYMBOL= "\uD83D\uDFE9";
    public static final String BLUE_SQUARE_SYMBOL= "\uD83D\uDFE6";
    public static final String PURPLE_SQUARE_SYMBOL= "\uD83D\uDFEA";
    public static final String BROWN_SQUARE_SYMBOL= "\uD83D\uDFEB";
    public static final String BLACK_SQUARE_SYMBOL= "\u2B1B";
    public static final String YELLOW_SQUARE_SYMBOL = "\uD83D\uDFE8";
    public static final String BLUE_CIRCLE_SYMBOL= "\uD83D\uDD35";
    public static final String GREEN_CIRCLE_SYMBOL= "\uD83D\uDFE2";
    public static final String YELLOW_CIRCLE_SYMBOL= "\uD83D\uDFE1";
    public static final String RED_CIRCLE_SYMBOL= "\uD83D\uDD34";
    public static final String ZERO_SYMBOL = "0️⃣";
    public static final String ONE_SYMBOL = "1️⃣";
    public static final String TWO_SYMBOL = "2️⃣";
    public static final String THREE_SYMBOL = "3️⃣";
    public static final String FOUR_SYMBOL = "4️⃣";
    public static final String FIVE_SYMBOL = "5️⃣";
    public static final String SIX_SYMBOL = "6️⃣";
    public static final String SEVEN_SYMBOL = "7️⃣";
    public static final String EIGHT_SYMBOL = "8️⃣";
    public static final String NINE_SYMBOL = "9️⃣";

    public static final String COVERED_ANGLES_SYMBOL = "\u25F0 ";
    public static final String FOR_EACH_SYMBOL ="\u2755";

    public static List<String> FromIntToEmoji(int n){
        String temp = String.valueOf(n);
        List<String> result= new ArrayList<>();
        for(int i=0; i<temp.length(); i++){
            switch (temp.charAt(i)){
                case '0':
                    result.add(ZERO_SYMBOL);
                    break;
                case '1':
                    result.add(ONE_SYMBOL);
                    break;
                case '2':
                    result.add(TWO_SYMBOL);
                    break;
                case '3':
                    result.add(THREE_SYMBOL);
                    break;
                case '4':
                    result.add(FOUR_SYMBOL);
                    break;
                case '5':
                    result.add(FIVE_SYMBOL);
                    break;
                case '6':
                    result.add(SIX_SYMBOL);
                    break;
                case '7':
                    result.add(SEVEN_SYMBOL);
                    break;
                case '8':
                    result.add(EIGHT_SYMBOL);
                    break;
                case '9':
                    result.add(NINE_SYMBOL);
                    break;
                default:
                    System.out.println("Error in FromIntToEmoji. We are sorry");
            }
        }
        return result;
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

}
