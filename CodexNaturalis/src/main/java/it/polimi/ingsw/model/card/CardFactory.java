package it.polimi.ingsw.model.card;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

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

    public static RandomPicker<Card> getInitialcards() throws FileNotFoundException {
        FileReader reader= new FileReader("src/main/java/it/polimi/ingsw/model/card/JsonFiles/Initial.json");
        Gson gson = new Gson();

        Card[] cards = gson.fromJson(reader,Card[].class);

        return new RandomPicker<Card>(Arrays.asList(cards));
    }

    //private static CardSide createBackSide(Symbol centerSymbol){}

    private static List<Card> getCardList(){
        return List.of();
    }


}

class CardAdapter extends TypeAdapter<Card>{
    public Card read(JsonReader reader) throws IOException {



    }

    public void write(JsonWriter write, Card card) throws IOException {

    }
}

/*
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
*/


