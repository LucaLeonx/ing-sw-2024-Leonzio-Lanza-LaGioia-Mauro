package it.polimi.ingsw.model.card;

import java.util.*;

public class CardSide {

    private final RequirementFunction playingRequirements;
    private final RewardFunction playingReward;
    private final Set<Symbol> centerSymbol;
    private final Map<AnglePosition, Symbol> angles;

    /**
     * Class constructor
     * @param centerSymbol The symbols displayed in the centre of the card
     * @param angles A map between each angle and the corresponding symbol on top of it
     * @param playingRequirements A function to determine whether a card can be played on this side on the map
     * @param playingReward A function that returns the number of points awarded when playing the card on this side on the map
     */
    public CardSide(Set<Symbol> centerSymbol, Map<AnglePosition,Symbol> angles, RequirementFunction playingRequirements, RewardFunction playingReward){
        this.centerSymbol = new HashSet<Symbol>(centerSymbol);
        this.angles = new HashMap<AnglePosition,Symbol>(angles);
        this.playingRequirements = playingRequirements;
        this.playingReward = playingReward;
    }

    /**
     * @return A Set with the values of the center symbols
     */
    public Set<Symbol> getCenterSymbols(){
        return centerSymbol;
    }

    /**
     * Return the Symbol displayed on the supplied angle
     * @param position The angle to check
     * @return The symbol inside the angle given as input
     */
    public Symbol getSymbolFromAngle(AnglePosition position){
        return angles.get(position);
    }

    /**
     * @return A new List containing all the symbols on the given CardSide,
     * both in the center and on the angles.
     */
    public List<Symbol> getDisplayedSymbols(){
        List<Symbol> displayedSymbols = new ArrayList<>(getCenterSymbols());

        for(AnglePosition position : AnglePosition.values()){
            displayedSymbols.add(getSymbolFromAngle(position));
        }

        return displayedSymbols;
    }

    /**
     *
     * @return An anonymous RequirementFunction that
     * evaluates whether it is possible to play the card on the current side
     * on the map
     */
    public RequirementFunction getPlayingRequirements() {
        return this.playingRequirements;
    }

    /**
     *
     * @return An anonymous RewardFunction that returns the number of points
     * earned when playing the card on the current side, depending on the other cards
     * on the map
     */
    public RewardFunction getPlayingReward() {
        return this.playingReward;
    }



}
