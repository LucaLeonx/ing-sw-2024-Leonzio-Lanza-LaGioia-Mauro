package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.map.GameField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.floor;

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

    /**
     * We have 2 cases here:
     * 1) We have a map that contains 3 symbol and we need to check how many times these symbols are repeated
     * 2) We have 1 symbol and we need to check how many times this symbol is repeated 3 times.
     * @param symbolOccurrences
     * @return
     */

    public static RewardFunction createCountGroupSymbolsFunction(Map<Symbol, Integer> symbolOccurrences ){
            return new RewardFunction() {
                @Override

                public int getPoints(GameField field) {
                    List<Integer> numberOfGroups = new ArrayList<>();

                    return symbolOccurrences.keySet().stream()
                            .mapToInt(symbol -> field.getCounter(symbol) / symbolOccurrences.get(symbol))
                            .min()
                            .orElse(0);
                }
                    // return floor(field.getCounter(symbolOccurrences.keySet().stream().findFirst())/3); //stream().limit(1).collect(collector)
            };
        }
    }



}
