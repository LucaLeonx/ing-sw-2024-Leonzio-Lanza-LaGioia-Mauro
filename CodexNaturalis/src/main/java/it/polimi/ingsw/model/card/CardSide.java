package it.polimi.ingsw.model.card;

import java.util.*;

public class CardSide {

    private final Set<Symbol> centerSymbol;
    private final Map<AnglePosition,Symbol> angles;

    public CardSide(Set<Symbol> centerSymbol, Map<AnglePosition,Symbol> angles){
        this.centerSymbol = centerSymbol;
        this.angles = angles;
    }

    public Set<Symbol> getCenterSymbols(){
        return centerSymbol;
    }

    public Symbol getSymbolFromAngle(AnglePosition position){
        return angles.get(position);
    }

    public Set<Symbol> getDisplayedSymbols(){
        Set<Symbol> displayedSymbols = new HashSet<>(getDisplayedSymbols());

        for(AnglePosition position : AnglePosition.values()){
            displayedSymbols.add(getSymbolFromAngle(position));
        }

        return displayedSymbols;
    }



}
