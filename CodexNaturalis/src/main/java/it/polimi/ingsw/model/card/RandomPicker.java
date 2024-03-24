package it.polimi.ingsw.model.card;

import java.util.*;

public class RandomPicker<T>{
    /**
     * This class stores a set of elements, allowing to extract
     * them in random order, without reinsertion
     */
    private final Iterator<T> elements;

    /**
     * @param elements The collection of elements stored to be stored at start
     * @param rng The source of randomness used for random extractions.
     *            This parameter is useful for testing purposes
     */
    public RandomPicker(Collection<T> elements, Random rng){
        List<T> temporaryList = new ArrayList<>(elements);
        Collections.shuffle(temporaryList, rng);
        this.elements = temporaryList.iterator();
    }

    /**
     * RandomPicker constructor with default randomness source
     * @param elements The collection of elements stored to be stored at start
     */
    public RandomPicker(Collection<T> elements){
        this(elements, new Random());
    }

    /**
     * Specifies if the RandomPicker is empty
     * @return True if and only if the RandomPicker is empty
     */
    public boolean isEmpty(){
        return !elements.hasNext();
    }

    /**
     *
     * @return
     */
    public Optional<T> extractRandomElement(){
        Optional<T> extractedElement = Optional.empty();

        if(!isEmpty()){
            extractedElement = Optional.of(elements.next());
        }

        return extractedElement;
    }
}