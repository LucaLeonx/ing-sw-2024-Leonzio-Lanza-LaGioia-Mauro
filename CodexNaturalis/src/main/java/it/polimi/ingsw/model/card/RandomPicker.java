package it.polimi.ingsw.model.card;

import java.util.*;

public class RandomPicker<T>{
    /**
     * This class stores a set of elements, allowing to extract
     * them in random order, without reinsertion
     */
    private final Stack<T> elements;

    /**
     * @param elements The collection of elements stored to be stored at start.
     *                 Keep in mind that null objects can be added to and
     *                 extracted from the RandomPicker.
     * @param rng The source of randomness used for random extractions.
     *            This parameter is useful for testing purposes
     */
    public RandomPicker(Collection<T> elements, Random rng) {
        this.elements = new Stack<>();
        this.elements.addAll(elements);
        Collections.shuffle(this.elements, rng);
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
        return elements.empty();
    }

    /**
     *
     * @return
     */
    public Optional<T> extractRandomElement(){
        Optional<T> extractedElement;

        try {
            extractedElement = Optional.of(elements.pop());
        } catch (EmptyStackException e){
            extractedElement = Optional.empty();
        }

        return extractedElement;
    }

    public List<T> getElements(){
        return List.copyOf(elements);
    }
}