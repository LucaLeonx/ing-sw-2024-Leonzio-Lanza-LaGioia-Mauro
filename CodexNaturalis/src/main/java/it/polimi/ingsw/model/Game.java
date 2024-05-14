package it.polimi.ingsw.model;

// import it.polimi.ingsw.controller.servercontroller.InternalGameObserver;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.map.GameField;
import it.polimi.ingsw.model.map.Point;
import it.polimi.ingsw.model.player.InvalidCardException;
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
    // private final List<InternalGameObserver> gameObservers = Collections.synchronizedList(new LinkedList<>());

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
                .findFirst().orElseThrow(NoSuchElementException::new);

        return playerInformation;
    }

    public synchronized String getCurrentPlayerNickname() {
        return currentPlayer.getNickname();
    }

    public synchronized boolean isLastTurn(){
        return isLastTurn;
    }

    public boolean isEnded() {
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
                isGameEnded = true;
                for(Player player : players){
                    calculateFinalReward(player);
                }
                // Notify game ending
            } else {
                isLastTurn =
                        (getResourceCardDeck().isEmpty() && getGoldenCardDeck().isEmpty())
                                || getPlayers().stream().anyMatch((participant) -> participant.getScore() >= 20);
            }
        }
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
    }

    public synchronized void registerPlayerSetup(String playerName, int objectiveCardId, CardOrientation initialCardSide){
        Player player = getPlayer(playerName);
        PlayerSetup setup = getPlayerSetup(playerName);
        ObjectiveCard chosenObjective = (objectiveCardId == setup.objective1().getId()) ? setup.objective1() : setup.objective2();
        player.setSecretObjective(chosenObjective);
        player.getField().placeCard(setup.initialCard(), initialCardSide, new Point(0,0));
    }

    public synchronized void makePlayerPlaceCard(Player player, int cardId, Point position, CardOrientation orientation)  {
        Card removedCard = null;
        GameField field = player.getField();

        try {
            removedCard = player.removeCard(cardId);
        } catch (InvalidCardException e) {
            throw new InvalidOperationException(e.getMessage());
        }


        field.placeCard(removedCard, orientation, position);
        player.incrementScore(removedCard.getSide(orientation).getRewardPoints(field));
    }

    public synchronized void makePlayerDraw(Player player, DrawChoice drawChoice){
        Deck selectedDeck = switch(drawChoice){
            case DECK_RESOURCE, RESOURCE_CARD_1, RESOURCE_CARD_2 -> getResourceCardDeck();
            case DECK_GOLD, GOLD_CARD_1, GOLD_CARD_2 -> getGoldenCardDeck();
        };

        switch(drawChoice){
            case DECK_RESOURCE, DECK_GOLD: {
                player.addCard(selectedDeck.draw());
                break;
            }
            case RESOURCE_CARD_1, RESOURCE_CARD_2, GOLD_CARD_1, GOLD_CARD_2:
                player.addCard(visibleCards.get(drawChoice));
                visibleCards.remove(drawChoice);
                visibleCards.put(drawChoice, selectedDeck.draw());
                break;
        }
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

    @Deprecated
    public synchronized void setVisibleCard(DrawChoice choice, Card card){
        visibleCards.put(choice, card);
    }

}
