package it.polimi.ingsw.model.card;

import java.util.*;

public class CardSide {

    private final Set<Symbol> centerSymbol;
    private final   Map<AnglePosition,Symbol> angles;

    public CardSide(Set<Symbol> centerSymbol, Map<AnglePosition,Symbol> angles){
        this.centerSymbol = centerSymbol;
        this.angles = angles;

    }

    public Set<Symbol> getCenterSymbols(){ return centerSymbol;}

    public Symbol getSymbolFromAngle(AnglePosition pos){ return angles.get(pos); }

}
