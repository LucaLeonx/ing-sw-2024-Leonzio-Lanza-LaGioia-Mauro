package it.polimi.ingsw.model.card;

import java.util.*;

public class RandomPicker<T>{

    private final Iterator<T> elements;

    // You can choose the seed for debugging purposes
    public RandomPicker(Collection<T> elements, Random rng){
        List<T> temporaryList = new ArrayList<>(elements);
        Collections.shuffle(temporaryList, rng);
        this.elements = temporaryList.iterator();
    }

    public RandomPicker(Collection<T> elements){
        this(elements, new Random());
    }

    public boolean isEmpty(){
        return !elements.hasNext();
    }

    public Optional<T> extractRandomElement(){
        Optional<T> extractedElement = Optional.empty();

        if(!isEmpty()){
            extractedElement = Optional.of(elements.next());
        }

        return extractedElement;
    }
}