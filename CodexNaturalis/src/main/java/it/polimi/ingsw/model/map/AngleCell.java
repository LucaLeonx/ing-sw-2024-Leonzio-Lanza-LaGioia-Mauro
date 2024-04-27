package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.card.Symbol;

public record AngleCell(Point topCardPosition, Point bottomCardPosition, Symbol topSymbol, Symbol bottomSymbol) {
    public AngleCell(Point topCardPosition, Symbol topSymbol){
        this(topCardPosition, topCardPosition, topSymbol, topSymbol);
    }

    public AngleCell withNewTopCard(Point newTopCardPosition, Symbol newTopSymbol){
        return new AngleCell(newTopCardPosition, this.topCardPosition, newTopSymbol, this.topSymbol);
    }

    public boolean isCovered(){
        return !topCardPosition.equals(bottomCardPosition());
    }
}
