package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.card.Symbol;

public record AngleCell(Point position, Point topCardPosition, Point bottomCardPosition, Symbol topSymbol, Symbol bottomSymbol) {
    public AngleCell(Point position, Point topCardPosition, Symbol topSymbol){
        this(position, topCardPosition, topCardPosition, topSymbol, topSymbol);
    }

    public AngleCell withNewTopCard(Point newTopCardPosition, Symbol newTopSymbol){
        return new AngleCell(this.position, newTopCardPosition, this.topCardPosition, newTopSymbol, this.topSymbol);
    }
}
