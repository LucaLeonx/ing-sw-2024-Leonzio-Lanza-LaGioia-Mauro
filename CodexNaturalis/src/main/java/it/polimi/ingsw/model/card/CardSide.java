package it.polimi.ingsw.model.card;

import java.util.*;

public class CardSide {

    private final RequirementFunction  playingRequirements;
    private final RewardFunction  playingReward;
    private final Set<Symbol> centerSymbol;
    private final Map<AnglePosition, Symbol> angles;

    /**
     * Class constructor
     * @param centerSymbol - symbols displayed in the centre of the card
     * @param angles -
     */
    public CardSide(Set<Symbol> centerSymbol, Map<AnglePosition,Symbol> angles, RequirementFunction playingRequirements, RewardFunction playingReward){
        this.centerSymbol = new HashSet<Symbol>(centerSymbol);
        this.angles = new HashMap<AnglePosition,Symbol>(angles);
        this.playingRequirements = playingRequirements;
        this.playingReward = playingReward;
    }


    public RequirementFunction getPlayingRequirements() {
        return this.playingRequirements;
    }

    public RewardFunction getPlayingReward() {
        return this.playingReward;
    }

    /**
     *
     *
     * @return A Set with the values of the center symbols
     */
    public Set<Symbol> getCenterSymbols(){
        return centerSymbol;
    }


    /**
     *
     * @param position - is one of the 4 possible angles that a cardSide could have, its type is defined by AnglePosition enum
     * @return The symbol inside the angle given in input
     */
    public Symbol getSymbolFromAngle(AnglePosition position){
        return angles.get(position);
    }

    /**
     *
     * @return All the symbols displayed in the card, both the center and the angles ones,
     * note that the returned Set is a copy of the class attribute, NOT the same, so
     * '==' statement will return false (as opposed to equals() method)
     */
    public Set<Symbol> getDisplayedSymbols(){
        Set<Symbol> displayedSymbols = new HashSet<>(getCenterSymbols());

        for(AnglePosition position : AnglePosition.values()){
            displayedSymbols.add(getSymbolFromAngle(position));
        }

        return displayedSymbols;
    }



}
