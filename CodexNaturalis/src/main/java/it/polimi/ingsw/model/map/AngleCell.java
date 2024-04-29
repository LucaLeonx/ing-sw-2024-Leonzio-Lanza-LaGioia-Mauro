package it.polimi.ingsw.model.map;

import it.polimi.ingsw.model.card.Symbol;

/**
 * Class used by the Gamefiled to memorize the symbols in a specific point, because they can be overlapped
 * @param topCardPosition
 * @param bottomCardPosition
 * @param topSymbol
 * @param bottomSymbol
 */
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
