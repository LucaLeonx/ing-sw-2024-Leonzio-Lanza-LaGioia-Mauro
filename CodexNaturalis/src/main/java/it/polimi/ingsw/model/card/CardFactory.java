package it.polimi.ingsw.model.card;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.*;

import static it.polimi.ingsw.model.card.GameFunctionFactory.createPointsRewardFunction;

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

    private static RandomPicker<Card> getInitialcard() throws FileNotFoundException {
        //scrivere CustomDeserialization per RequiredFunction e RewardFunction
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

class RequirementFuncDeserializer implements JsonDeserializer<RequirementFunction>{ @Override
   public RequirementFunction deserialize() throws JsonParseException {
        return createRequiredFunction(true);
   }

    @Override
    public RequirementFunction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }
}
class RewardFuncDeserializer implements JsonDeserializer<RewardFunction>{
    public RewardFunction deserialize() throws JsonParseException{
        return createPointsRewardFunction(0);
    }
}
