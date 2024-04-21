package it.polimi.ingsw.model.card;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
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
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Card.class,new InitialCardAdapter());
        Gson gson = builder.create();

        Card[] cards = gson.fromJson(reader,Card[].class);

        return new RandomPicker<Card>(Arrays.asList(cards));
    }

    //private static CardSide createBackSide(Symbol centerSymbol){}

    private static List<Card> getCardList(){
        return List.of();
    }


}

class InitialCardAdapter extends TypeAdapter<Card>{
    public Card read(JsonReader reader) throws IOException{
        reader.beginObject();
        int id = 0;
        CardColor col = null;
        CardSide front = null, back = null;
        String type = null;
        int i = 0;

        while(reader.hasNext()){
            JsonToken token = reader.peek();
            i++;
            if(token.equals(JsonToken.NAME)){
                type = reader.nextName();
            }
            if(type.equals("id")){
                id = reader.nextInt();
                System.out.println(i +" id:" + id);
                token = reader.peek();
            }
            if(type.equals("color")){
                String s = reader.nextString();
                col = CardColor.valueOf(s);
                System.out.println(i+" color:" + s);
                token = reader.peek();
            }
            if(type.equals("front")){
                Set<Symbol> centerSymbols = new HashSet<Symbol>();
                Map<AnglePosition,Symbol> angle = new HashMap<AnglePosition,Symbol>();
                reader.beginObject();
                reader.nextName(); //centerSymbol

                reader.beginArray();
                token = reader.peek();
                while(!token.equals(JsonToken.END_ARRAY)){
                    centerSymbols.add(Symbol.valueOf(reader.nextString()));
                    token = reader.peek();
                }
                reader.endArray();

                reader.nextName(); //angle
                reader.beginObject();
                token = reader.peek();
                while(!token.equals(JsonToken.END_OBJECT)){
                    angle.put(AnglePosition.valueOf(reader.nextName()),Symbol.valueOf(reader.nextString()));
                    token = reader.peek();
                }
                reader.endObject();

                reader.endObject();
                token = reader.peek();

            }
            if(type.equals("back")){
                Map<AnglePosition,Symbol> angle = new HashMap<AnglePosition,Symbol>();
                reader.beginObject();

                reader.nextName(); //angle
                reader.beginObject();
                token = reader.peek();
                while(!token.equals(JsonToken.END_OBJECT)){
                    angle.put(AnglePosition.valueOf(reader.nextName()),Symbol.valueOf(reader.nextString()));
                    token = reader.peek();
                }
                reader.endObject();

                reader.endObject();
                token = reader.peek();
            }
            if(token.equals(JsonToken.END_OBJECT)){
                reader.endObject();
            }
        }
        return new Card(id,col,front,back);
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


