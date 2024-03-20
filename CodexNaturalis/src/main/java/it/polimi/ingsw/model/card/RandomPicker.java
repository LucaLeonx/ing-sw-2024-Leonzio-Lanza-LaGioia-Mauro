import java.util.Collections
import java.util.List
import java.util.Optional
import java.util.Random

public class RandomPicker<T>{

    private final Iterator<T> elements;

    // You can choose the seed for debugging purposes
    public RandomPicker(Collection<T> elements, RandomGenerator rng){
        List<T> temporaryList = List.copyOf(elements);
        Collections.shuffle(temporaryList, rng);
        this.elements = temporaryList.iterator();
    }

    public RandomPicker(Collection<T> elements){
        RandomPicker(elements, new Random());
    }

    public boolean isEmpty(){
        return !elements.hasNext();
    }

    public Optional<T> extractRandomElement(){
        Optional<T> extractedElement = Optional.of(null);

        if(!isEmpty()){
            extractedElement = Optional.of(elements.next());
        }

        return extractedElement;
    }
}