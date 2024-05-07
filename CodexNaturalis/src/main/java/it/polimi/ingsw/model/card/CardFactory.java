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
import it.polimi.ingsw.dataobject.RewardType;
import it.polimi.ingsw.model.Requirement;
import it.polimi.ingsw.model.Reward;

import static it.polimi.ingsw.dataobject.RewardType.*;
import static it.polimi.ingsw.model.card.GameFunctionFactory.*;

public abstract class CardFactory {

    private List<Card> InitialCards;
    private List<Card> ResourceCards;
    private List<Card> GoldCards;
    private List<ObjectiveCard> ObjectiveCards;
    private void setupInitial() throws FileNotFoundException{

    }

    private void setupResource() throws FileNotFoundException{

    }

    private void setupGold() throws FileNotFoundException{

    }

    private void setupObjective() throws FileNotFoundException{

    }

    protected static RewardType getRewardEnum(String type, int points){
        return switch(type){
            case "DEFAULT" -> switch (points){
                case 0 -> NONE;
                case 1 -> ONE_POINT;
                case 3 -> THREE_POINTS;
                case 5 -> FIVE_POINTS;
                default -> throw new IllegalStateException("Unexpected value: " + points);
            };
            case "QUILL" -> POINT_PER_QUILL;
            case "INKWELL" -> POINT_PER_INKWELL;
            case "MANUSCRIPT" -> POINT_PER_MANUSCRIPT;
            case "COVEREDANGLES" -> POINTS_PER_COVERED_ANGLE;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

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

    public static List<ObjectiveCard> getObjectiveCards() throws FileNotFoundException{
        FileReader reader= new FileReader("src/main/java/it/polimi/ingsw/model/card/JsonFiles/Objective.json");
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(ObjectiveCard[].class,new ObjectiveCardAdapter());
        Gson gson = builder.create();

        ObjectiveCard[] card = gson.fromJson(reader,ObjectiveCard[].class);
        return  Arrays.asList(card);
    }
}
class ResourceCardAdapter extends TypeAdapter<Card[]>{
    @Override
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
                    new Requirement(new HashMap<Symbol,Integer>()),
                    new Reward(NONE, createPointsRewardFunction(0))
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
                        new Requirement(new HashMap<Symbol,Integer>()),
                        new Reward((reward == 0) ? NONE : CardFactory.getRewardEnum("DEFAULT", reward), createPointsRewardFunction(reward))
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
    @Override
    public void write(JsonWriter write, Card[] card) throws IOException {

    }
}
class InitialCardAdapter extends TypeAdapter<Card[]>{
    @Override
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
                        new Requirement(new HashMap<Symbol,Integer>()),
                        new Reward(NONE, createPointsRewardFunction(0))
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
                        new Requirement(new HashMap<Symbol,Integer>()),
                        new Reward(NONE, createPointsRewardFunction(0))
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

    @Override
    public void write(JsonWriter write, Card[] card) throws IOException {

    }
}

class GoldCardAdapter extends TypeAdapter<Card[]>{
    @Override
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
                    new Requirement(new HashMap<>()),
                    new Reward(NONE, createPointsRewardFunction(0))
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
                int pointsToAward = 0;
                if(tipoReward.equals("COVEREDANGLES")){
                    reader.nextName();
                    reader.nextInt();
                    reward = createCoveredAnglesFunction(id);
                } else if (tipoReward.equals("DEFAULT")) {
                    reader.nextName();
                    pointsToAward = reader.nextInt();
                    reward = createPointsRewardFunction(pointsToAward);
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
                        new Requirement(requiredSymbols),
                        new Reward(CardFactory.getRewardEnum(tipoReward, pointsToAward), reward));

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
    @Override
    public void write(JsonWriter write, Card[] card) throws IOException {

    }
}

class ObjectiveCardAdapter extends TypeAdapter<ObjectiveCard[]>{

    @Override
    public ObjectiveCard[] read(JsonReader reader) throws IOException {
        ObjectiveCard[] array = new ObjectiveCard[16];
        int i=0;
        String typeName = null;
        reader.beginArray();

        while (reader.hasNext()) {
            int id = 0;
            RewardFunction reward = null;
            Map<Symbol, Integer> symbolsGroup = new HashMap<Symbol, Integer>();

            reader.beginObject();
            JsonToken token = reader.peek();
            if(token.equals(JsonToken.NAME)){
                typeName = reader.nextName();
            }
            if(typeName.equals("id")){
                id = reader.nextInt();
                typeName = reader.nextName();
            }
            if(typeName.equals("type")){
                String rewardType = reader.nextString();
                if(rewardType.equals("DIAG")){
                    reader.nextName();
                    reader.beginArray();
                    reward = createDiagonalPatternMatchFunction(reader.nextInt()==1,CardColor.valueOf(reader.nextString()));
                    reader.endArray();
                }
                if(rewardType.equals("BLOCK")){
                    CardColor colorblock = null ,colorangle = null;
                    reader.nextName();
                    String angle = reader.nextString();
                    switch (angle){
                        case "UP_LEFT":
                            colorblock = CardColor.valueOf("RED");
                            colorangle = CardColor.valueOf("GREEN");
                            break;
                        case "UP_RIGHT":
                            colorblock = CardColor.valueOf("GREEN");
                            colorangle = CardColor.valueOf("PURPLE");
                            break;
                        case "DOWN_LEFT":
                            colorblock = CardColor.valueOf("SKYBLUE");
                            colorangle = CardColor.valueOf("RED");
                            break;
                        case "DOWN_RIGHT":
                            colorblock = CardColor.valueOf("PURPLE");
                            colorangle = CardColor.valueOf("SKYBLUE");
                            break;
                    }
                    reward = createBlockPatternMatchFunction(AnglePosition.valueOf(angle),colorblock,colorangle);
                }
                if(rewardType.equals("VECTOR")){
                    reader.nextName();
                    int points = reader.nextInt();
                    reader.nextName();
                    reader.beginArray();
                    while(!reader.peek().equals(JsonToken.END_ARRAY)){
                        Symbol reqSymb = Symbol.valueOf(reader.nextString());
                        if(symbolsGroup.containsKey(reqSymb)){
                            symbolsGroup.replace(reqSymb,symbolsGroup.get(reqSymb)+1);
                        }
                        else{
                            symbolsGroup.put(reqSymb,1);
                        }
                    }
                    reader.endArray();
                    reward = createCountGroupSymbolsFunction(symbolsGroup,points);
                }
                token = reader.peek();
            }
            array[i] = new ObjectiveCard(id,reward);
            i++;
            if(token.equals(JsonToken.END_OBJECT)){
                reader.endObject();
            }
        }

        reader.endArray();
        return array;
    }

    @Override
    public void write(JsonWriter write, ObjectiveCard[] objCard) throws IOException {

    }
}

