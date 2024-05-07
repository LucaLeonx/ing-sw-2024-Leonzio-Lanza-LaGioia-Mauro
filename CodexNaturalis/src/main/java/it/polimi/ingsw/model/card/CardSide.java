package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.Requirement;
import it.polimi.ingsw.model.Reward;
import it.polimi.ingsw.model.map.GameField;

import java.util.*;

public class CardSide {

    private final Requirement playingRequirements;
    private final Reward playingReward;
    private final Set<Symbol> centerSymbol;
    private final Map<AnglePosition, Symbol> angles;

    /**
     * Class constructor
     * @param centerSymbol The symbols displayed in the centre of the card
     * @param angles A map between each angle and the corresponding symbol on top of it
     * @param playingRequirements A function to determine whether a card can be played on this side on the map
     * @param playingReward A function that returns the number of points awarded when playing the card on this side on the map
     */
    public CardSide(Set<Symbol> centerSymbol, Map<AnglePosition,Symbol> angles, Requirement playingRequirements, Reward playingReward){
        this.centerSymbol = new HashSet<Symbol>(centerSymbol);
        this.angles = new HashMap<AnglePosition,Symbol>(angles);
        this.playingRequirements = playingRequirements;
        this.playingReward = playingReward;
    }

    /**
     * @return A Set with the values of the center symbols
     */
    public Set<Symbol> getCenterSymbols(){
        return Set.copyOf(centerSymbol);
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
    public Requirement getPlayingRequirements() {
        return this.playingRequirements;
    }

    /**
     *
     * @return An anonymous RewardFunction that returns the number of points
     * earned when playing the card on the current side, depending on the other cards
     * on the map
     */
    public Reward getPlayingReward() {
        return this.playingReward;
    }

    public int getRewardPoints(GameField field){
        return playingReward.rewardCalculator().getPoints(field);
    }

    public boolean isPlayable(GameField field){
        return playingRequirements.getRequirementCalculator().isSatisfied(field);
    }
}
