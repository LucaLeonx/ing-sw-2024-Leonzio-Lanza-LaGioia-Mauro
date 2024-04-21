package it.polimi.ingsw.test.model.card;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardFactory;
import it.polimi.ingsw.model.card.RandomPicker;

import java.io.FileNotFoundException;
import java.util.Optional;

public class CardFactoryTest {
    public static void main(String[] args) throws FileNotFoundException {

        RandomPicker<Card> initalCardPicker = CardFactory.getInitialcards();
        Optional<Card> Optcarta;
        Card carta;

        while(!initalCardPicker.isEmpty()){
            Optcarta = initalCardPicker.extractRandomElement();
            if(Optcarta.isPresent()) {
                carta = Optcarta.get();
                System.out.println("id :"+ carta.getId());

            }
        }

        return;
    }

}
