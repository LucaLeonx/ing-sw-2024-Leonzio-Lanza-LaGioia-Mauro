package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.map.GameField;

import java.util.Map;

public abstract class GameFunctionFactory {

    public static RewardFunction createPointsRewardFunction(int points) {
        return new RewardFunction() {
            @Override
            public int getPoints(GameField field) {
                return points;
            }
        };
    }

    public static RewardFunction createCountSymbolsFunction(Symbol symbol){
        return new RewardFunction() {
            @Override
            public int getPoints(GameField field) {
                return field.getCounter(symbol);
            }
        };
    }

    public static RewardFunction


    public static RewardFunction createCountSymbolsFunction(Symbol symbol){
        return new RewardFunction() {
            @Override
            public int getPoints(GameField field) {
                return field.getCounter(symbol);
            }
        };
    }



}
