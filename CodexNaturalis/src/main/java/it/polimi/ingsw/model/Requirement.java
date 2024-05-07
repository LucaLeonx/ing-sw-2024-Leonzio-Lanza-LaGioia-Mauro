package it.polimi.ingsw.model;

import it.polimi.ingsw.model.card.GameFunctionFactory;
import it.polimi.ingsw.model.card.RequirementFunction;
import it.polimi.ingsw.model.card.Symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Requirement {

    private final List<Symbol> requiredSymbols;
    private final RequirementFunction requirementFunction;

    public Requirement(Map<Symbol, Integer> symbolCount) {

        requiredSymbols = new ArrayList<>();
        for(Map.Entry<Symbol, Integer> entry : symbolCount.entrySet()){
            for(int i = 0; i < entry.getValue(); i++){
                requiredSymbols.add(entry.getKey());
            }
        }

        this.requirementFunction = GameFunctionFactory.createRequiredSymbolsFunction(symbolCount);
    }

    public List<Symbol> getRequiredSymbols() {
        return requiredSymbols;
    }

    public RequirementFunction getRequirementCalculator(){
        return requirementFunction;
    }


}
