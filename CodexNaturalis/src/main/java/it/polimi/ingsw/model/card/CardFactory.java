package it.polimi.ingsw.model.card;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import static it.polimi.ingsw.model.card.GameFunctionFactory.*;

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

    public static List<Card> getInitialCards() throws FileNotFoundException {
        FileReader reader= new FileReader("src/main/java/it/polimi/ingsw/model/card/JsonFiles/Initial.json");
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Card[].class,new InitialCardAdapter());
        Gson gson = builder.create();

        Card[] card = gson.fromJson(reader,Card[].class);
        return  Arrays.asList(card);
    }

    public static List<Card> getResourceCards() throws FileNotFoundException{
        FileReader reader= new FileReader("src/main/java/it/polimi/ingsw/model/card/JsonFiles/Resource.json");
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Card[].class,new ResourceCardAdapter());
        Gson gson = builder.create();

        Card[] card = gson.fromJson(reader,Card[].class);
        return  Arrays.asList(card);
    }

    public static List<Card> getGoldCards() throws FileNotFoundException{
        FileReader reader= new FileReader("src/main/java/it/polimi/ingsw/model/card/JsonFiles/Gold.json");
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(Card[].class,new GoldCardAdapter());
        Gson gson = builder.create();

        Card[] card = gson.fromJson(reader,Card[].class);
        return  Arrays.asList(card);
    }

    private static List<Card> getCardList(){
        return List.of();
    }


}
class ResourceCardAdapter extends TypeAdapter<Card[]>{
    public  Card[] read (JsonReader reader) throws IOException {
        Card[] resourcesCard = new Card[40];
        int i = 0;
        String typeName = null;

        reader.beginArray();

        while (reader.hasNext()) {
            int id = 0,reward = 0;
            CardColor col = null;
            CardSide front = null;
            CardSide back = new CardSide(
                    new HashSet<Symbol>(),
                    Map.of(AnglePosition.UP_LEFT, Symbol.BLANK,
                            AnglePosition.UP_RIGHT, Symbol.BLANK,
                            AnglePosition.DOWN_LEFT, Symbol.BLANK,
                            AnglePosition.DOWN_RIGHT, Symbol.BLANK),
                    createRequiredSymbolsFunction(new HashMap<Symbol,Integer>()),
                    createPointsRewardFunction(0)
            );

            reader.beginObject();
            JsonToken token = reader.peek();
            if(token.equals(JsonToken.NAME)){
                typeName = reader.nextName();
            }
            if(typeName.equals("id")){
                id = reader.nextInt();
                typeName = reader.nextName();
            }
            if(typeName.equals("color")){
                col = CardColor.valueOf(reader.nextString());
                typeName = reader.nextName();
            }
            if(typeName.equals("front")){
                Map<AnglePosition,Symbol> angle = new HashMap<AnglePosition,Symbol>();
                reader.beginObject();

                reader.nextName(); //RewardFunction
                reward = reader.nextInt();

                reader.nextName(); //angle
                reader.beginObject();
                token = reader.peek();
                while(!token.equals(JsonToken.END_OBJECT)){
                    angle.put(AnglePosition.valueOf(reader.nextName()),Symbol.valueOf(reader.nextString()));
                    token = reader.peek();
                }
                reader.endObject();
                //Create the front object
                front = new CardSide(
                        new HashSet<Symbol>(),
                        angle,
                        createRequiredSymbolsFunction(new HashMap<Symbol,Integer>()),
                        createPointsRewardFunction(reward)
                );

                reader.endObject();
                //typeName = reader.nextName();
            }

            resourcesCard[i] = new Card(id,col,front,back);
            i++;
            if(token.equals(JsonToken.END_OBJECT)){
                reader.endObject();
            }
        }
        reader.endArray();
        return resourcesCard;
    }

    public void write(JsonWriter write, Card[] card) throws IOException {

    }
}
class InitialCardAdapter extends TypeAdapter<Card[]>{
    public  Card[] read (JsonReader reader) throws IOException{
        Card[] array = new Card[6];
        int i=0;
        String typeName = null;
        reader.beginArray();

        while (reader.hasNext()){
            int id = 0;
            CardColor col = null;
            CardSide front = null, back = null;

            reader.beginObject();
            JsonToken token = reader.peek();
            if(token.equals(JsonToken.NAME)){
                typeName = reader.nextName();
            }
            if(typeName.equals("id")){
                id = reader.nextInt();
                typeName = reader.nextName();
            }
            if(typeName.equals("color")){
                col = CardColor.valueOf(reader.nextString());
                typeName = reader.nextName();
            }
            if(typeName.equals("front")){
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
                //Create the front object
                front = new CardSide(
                        centerSymbols,
                        angle,
                        createRequiredSymbolsFunction(new HashMap<Symbol,Integer>()),
                        createPointsRewardFunction(0)
                );

                reader.endObject();
                typeName = reader.nextName();

            }
            if(typeName.equals("back")){
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
                //Create the back object
                back = new CardSide(
                        new HashSet<Symbol>(),
                        angle,
                        createRequiredSymbolsFunction(new HashMap<Symbol,Integer>()),
                        createPointsRewardFunction(0)
                );
                reader.endObject();
                token = reader.peek();
            }
            array[i] = new Card(id,col,front,back);
            i++;
            if(token.equals(JsonToken.END_OBJECT)){
                reader.endObject();
            }
        }
        reader.endArray();
        return array ;
    }

    public void write(JsonWriter write, Card[] card) throws IOException {

    }
}

class GoldCardAdapter extends TypeAdapter<Card[]>{
    public  Card[] read (JsonReader reader) throws IOException{
        Card[] array = new Card[40];
        int i=0;
        String typeName = null;
        reader.beginArray();


        while (reader.hasNext()) {
            int id = 0;
            RewardFunction reward;
            Map<Symbol,Integer> requiredSymbols = new HashMap<Symbol,Integer>();
            CardColor col = null;
            CardSide front = null;
            CardSide back = new CardSide(
                    new HashSet<Symbol>(),
                    Map.of(AnglePosition.UP_LEFT, Symbol.BLANK,
                            AnglePosition.UP_RIGHT, Symbol.BLANK,
                            AnglePosition.DOWN_LEFT, Symbol.BLANK,
                            AnglePosition.DOWN_RIGHT, Symbol.BLANK),
                    createRequiredSymbolsFunction(new HashMap<Symbol, Integer>()),
                    createPointsRewardFunction(0)
            );

            reader.beginObject();
            JsonToken token = reader.peek();
            if(token.equals(JsonToken.NAME)){
                typeName = reader.nextName();
            }
            if(typeName.equals("id")){
                id = reader.nextInt();
                typeName = reader.nextName();
            }
            if(typeName.equals("color")){
                col = CardColor.valueOf(reader.nextString());
                typeName = reader.nextName();
            }
            if(typeName.equals("front")){
                Map<AnglePosition,Symbol> angle = new HashMap<AnglePosition,Symbol>();
                reader.beginObject();

                reader.nextName(); //RewardFunction
                reader.beginObject();
                reader.nextName();//Rewardtype
                String tipoReward = reader.nextString();
                if(tipoReward.equals("COVEREDANGLES")){
                    reader.nextName();
                    reader.nextInt();
                    reward = createCoveredAnglesFunction(id);
                } else if (tipoReward.equals("DEFAULT")) {
                    reader.nextName();
                    reward = createPointsRewardFunction(reader.nextInt());
                } else{
                    reward = createCountSymbolsFunction(Symbol.valueOf(tipoReward));
                    reader.nextName();
                    reader.nextInt();
                }
                reader.endObject(); // End Rewardfunction

                reader.nextName(); //RequiredFunction

                reader.beginArray();
                token = reader.peek();
                while(!token.equals(JsonToken.END_ARRAY)){
                    Symbol reqSymb = Symbol.valueOf(reader.nextString());
                    if(requiredSymbols.containsKey(reqSymb)){
                        requiredSymbols.replace(reqSymb,requiredSymbols.get(reqSymb)+1);
                    }
                    else{
                        requiredSymbols.put(reqSymb,1);
                    }

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

                front = new CardSide(
                        new HashSet<Symbol>(),
                        angle,
                        createRequiredSymbolsFunction(requiredSymbols),
                        reward
                );

                reader.endObject();
                //typeName = reader.nextName();
            }
            array[i] = new Card(id,col,front,back);
            i++;
            if(token.equals(JsonToken.END_OBJECT)){
                reader.endObject();
            }

        }
        reader.endArray();
        return array ;
    }

    public void write(JsonWriter write, Card[] card) throws IOException {

    }
}

