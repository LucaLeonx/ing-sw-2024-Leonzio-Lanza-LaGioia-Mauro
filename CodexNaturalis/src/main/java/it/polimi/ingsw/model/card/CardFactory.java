package it.polimi.ingsw.model.card;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;

import static it.polimi.ingsw.model.card.GameFunctionFactory.createPointsRewardFunction;
import static it.polimi.ingsw.model.card.GameFunctionFactory.createRequiredSymbolsFunction;

public abstract class CardFactory {

    /*
            return new CardSide(new HashSet<>().add(centerSymbol), Map.of(
                AnglePosition.UP_LEFT, Symbol.BLANK,
                AnglePosition.UP_RIGHT, Symbol.BLANK,
                AnglePosition.DOWN_LEFT, Symbol.BLANK,
                AnglePosition.DOWN_RIGHT, Symbol.BLANK
        ));
     */

//

    public static RandomPicker<Card> getInitialcard() throws FileNotFoundException {
        FileReader reader= new FileReader("\\JSONfiles\\Initial.json");
        Gson gson = new Gson();

        Card[] cards = gson.fromJson(reader,Card[].class);

        return new RandomPicker<Card>(Arrays.asList(cards));
    }

    //private static CardSide createBackSide(Symbol centerSymbol){}

    private static List<Card> getCardList(){
        return List.of();
    }

}

abstract class RequirementFuncDeserializer implements JsonDeserializer<RequirementFunction>{
   public RequirementFunction deserialize() throws JsonParseException {
       HashMap<Symbol,Integer> emptyMap = new HashMap<>();
       return createRequiredSymbolsFunction(emptyMap);
   }
}
abstract class RewardFuncDeserializer implements JsonDeserializer<RewardFunction>{
    public RewardFunction deserialize() throws JsonParseException{
        return createPointsRewardFunction(0);
    }
}
