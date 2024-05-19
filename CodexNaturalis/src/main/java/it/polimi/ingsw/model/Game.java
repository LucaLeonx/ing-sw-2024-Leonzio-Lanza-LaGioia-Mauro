package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.servercontroller.operationexceptions.ElementNotFoundException;
import it.polimi.ingsw.controller.servercontroller.operationexceptions.InvalidParameterException;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.RejectedExecutionException;

public class Game {

    private final List<Player> players;
    private final Deck resourceCardDeck;
    private final Deck goldCardDeck;
    private final Map<DrawChoice, Card> visibleCards;
    private final List<ObjectiveCard> commonObjectiveCards;
    private final Map<String, PlayerSetup> playerSetups;
    private Player currentPlayer;

    private boolean isLastTurn;
    private boolean isGameEnded;

    public Game(Deck goldCardDeck,
                Deck resourceCardDeck,
                List<Player> players,
                Player currentPlayer,
                Map<DrawChoice, Card> visibleCards,
                List<ObjectiveCard> commonObjectiveCards,
                Map<String, PlayerSetup> playerSetups,
                boolean isLastTurn,
                boolean isGameEnded) {
        this.goldCardDeck = goldCardDeck;
        this.resourceCardDeck = resourceCardDeck;
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.visibleCards = visibleCards;
        this.commonObjectiveCards = commonObjectiveCards;
        this.playerSetups = playerSetups;
        this.isLastTurn = isLastTurn;
        this.isGameEnded = isGameEnded;
    }


    public Game(List<String> playerNames){
        // Create decks and random pickers

        Deck initialCardsDeck;
        RandomPicker<ObjectiveCard> objectivePicker;
        RandomPicker<PlayerColor> colorPicker;

        try {
            resourceCardDeck = new Deck(CardFactory.getResourceCards());
            goldCardDeck = new Deck(CardFactory.getGoldCards());

            initialCardsDeck = new Deck(CardFactory.getInitialCards());
            objectivePicker = new RandomPicker<>(CardFactory.getObjectiveCards());
            colorPicker = new RandomPicker<>(List.of(PlayerColor.values()));
        } catch (FileNotFoundException e){
            throw new RejectedExecutionException("Impossible to create game: cards missing");
        }

        // Create players

        players = new ArrayList<>();
        playerSetups = new HashMap<>();

        for(String playerName : playerNames){

            Player newPlayer = new Player(playerName, colorPicker.extractRandomElement().get());
            // Generate hand cards
            newPlayer.addCard(resourceCardDeck.draw());
            newPlayer.addCard(resourceCardDeck.draw());
            newPlayer.addCard(goldCardDeck.draw());
            players.add(newPlayer);

            // Create setup
            playerSetups.put(playerName, new PlayerSetup(objectivePicker.extractRandomElement().get(),
                    objectivePicker.extractRandomElement().get(),
                    initialCardsDeck.draw()));
        }

        // Create common objectives
        commonObjectiveCards = new ArrayList<>();
        commonObjectiveCards.add(objectivePicker.extractRandomElement().get());
        commonObjectiveCards.add(objectivePicker.extractRandomElement().get());

        // Generate visible cards
        visibleCards = new HashMap<>();
        visibleCards.put(DrawChoice.RESOURCE_CARD_1, resourceCardDeck.draw());
        visibleCards.put(DrawChoice.RESOURCE_CARD_2, resourceCardDeck.draw());
        visibleCards.put(DrawChoice.GOLD_CARD_1, goldCardDeck.draw());
        visibleCards.put(DrawChoice.GOLD_CARD_2, goldCardDeck.draw());

        // Setup variables to start
        currentPlayer = players.getFirst();
        isLastTurn = false;
        isGameEnded = false;

    }

    public List<Player> getPlayers(){
        return players;
    }

    public Player getPlayer(String nickname) throws NoSuchElementException {
        Player playerInformation = players.stream()
                .filter((player) -> player.getNickname().equals(nickname))
                .findFirst()
                .orElseThrow(() -> new ElementNotFoundException("Player " + nickname + " not found"));

        return playerInformation;
    }

    public synchronized String getCurrentPlayerNickname() {
        return currentPlayer.getNickname();
    }

    public synchronized boolean isLastTurn(){
        return isLastTurn;
    }

    public synchronized boolean isEnded() {
        return isGameEnded;
    }

    public synchronized PlayerSetup getPlayerSetup(String playerName){
        return playerSetups.get(playerName);
    }

    public Deck getResourceCardDeck(){
        return resourceCardDeck;
    }

    public Deck getGoldenCardDeck(){
        return goldCardDeck;
    }

    public List<ObjectiveCard> getCommonObjectiveCards() {
        return new ArrayList<>(commonObjectiveCards);
    }

    public synchronized Map<DrawChoice, Card> getVisibleCards(){
        return new HashMap<>(visibleCards);
    }

    public synchronized void changeCurrentPlayer(){
        if(isLastPlayerOfRound()) {
            if(isLastTurn){
                endGame();
            } else {
                isLastTurn =
                        (getResourceCardDeck().isEmpty() && getGoldenCardDeck().isEmpty())
                                || getPlayers().stream().anyMatch((participant) -> participant.getScore() >= 20);
            }
        }
        updateCurrentPlayer();
    }

    public void endGame() {
        isGameEnded = true;
        for(Player player : players){
            calculateFinalReward(player);
        }
    }

    public synchronized boolean allPlayersHaveSetup(){
        return getPlayers().stream().allMatch(Player::hasSecretObjective);
    }

    public synchronized boolean canPlayerPlay(String playerName){
        return !getPlayer(playerName).getField().getAvailablePositions().isEmpty();
    }

    public synchronized void registerPlayerSetup(String playerName, int objectiveCardId, CardOrientation initialCardSide){
        Player player = getPlayer(playerName);
        PlayerSetup setup = getPlayerSetup(playerName);

        ObjectiveCard chosenObjective;

        if(player.hasSecretObjective()){
            throw new InvalidParameterException("Setup already chosen");
        }

        if(objectiveCardId == setup.objective1().getId()){
           chosenObjective = setup.objective1();
        } else if(objectiveCardId == setup.objective2().getId()){
            chosenObjective = setup.objective2();
        } else {
            throw new ElementNotFoundException("Cannot choose objective card with ID " + objectiveCardId);
        }

        player.setSecretObjective(chosenObjective);
        player.getField().placeCard(setup.initialCard(), initialCardSide, new Point(0,0));
    }

    public synchronized void makePlayerPlaceCard(String playerName, int cardId, Point position, CardOrientation orientation)  {
        Player player = getPlayer(playerName);
        Card removedCard = null;
        GameField field = player.getField();
        removedCard = player.removeCard(cardId);
        field.placeCard(removedCard, orientation, position);
        player.incrementScore(removedCard.getSide(orientation).getRewardPoints(field));
    }

    public synchronized void makePlayerDraw(String playerName, DrawChoice drawChoice){

        Player player = getPlayer(playerName);

        Deck selectedDeck = switch(drawChoice){
            case DECK_RESOURCE, RESOURCE_CARD_1, RESOURCE_CARD_2 -> getResourceCardDeck();
            case DECK_GOLD, GOLD_CARD_1, GOLD_CARD_2 -> getGoldenCardDeck();
        };

        switch(drawChoice){
            case DECK_RESOURCE, DECK_GOLD: {
                if(selectedDeck.isEmpty()){
                    throw new ElementNotFoundException("Cannot draw from empty " + drawChoice );
                }
                player.addCard(selectedDeck.draw());
                break;
            }
            case RESOURCE_CARD_1, RESOURCE_CARD_2, GOLD_CARD_1, GOLD_CARD_2:

                if(!visibleCards.containsKey(drawChoice)){
                    throw new ElementNotFoundException("Cannot draw non-existent card from " + drawChoice + " position");
                }

                player.addCard(visibleCards.get(drawChoice));
                visibleCards.remove(drawChoice);
                if(!selectedDeck.isEmpty()){
                    visibleCards.put(drawChoice, selectedDeck.draw());
                }
                break;
        }
    }

    public synchronized void skipTurn(){
        updateCurrentPlayer();
    }

    public synchronized List<Player> getLeaderBoard(){
            List<Player> leaderboard = getPlayers();
            leaderboard.sort((p1, p2) -> (p1.getScore() > p2.getScore()) ? 1 : -1);
            return leaderboard;
    }

    private synchronized boolean isLastPlayerOfRound(){
        return currentPlayer.getNickname().equals(players.getLast().getNickname());
    }

    private synchronized void calculateFinalReward(Player player){
        player.incrementScore(player.getSecretObjective().getRewardFunction().getPoints(player.getField()));
        for(ObjectiveCard objective : commonObjectiveCards){
            player.incrementScore(objective.getRewardFunction().getPoints(player.getField()));
        }
    }

    private void updateCurrentPlayer() {
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
    }
}
