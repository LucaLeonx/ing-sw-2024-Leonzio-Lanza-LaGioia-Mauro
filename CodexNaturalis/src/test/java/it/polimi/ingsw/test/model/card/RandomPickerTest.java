package it.polimi.ingsw.test.model.card;

import it.polimi.ingsw.model.card.RandomPicker;
import junit.framework.*;

import java.util.*;
import java.lang.Integer;

public class RandomPickerTest extends TestCase {

    private RandomPicker<Integer> pickerA;
    private RandomPicker<Integer> pickerB;
    private RandomPicker<Integer> pickerEmpty;
    private List<Integer> initialList;

    public void setUp(){
        initialList = Arrays.asList(2,4,6,8,10);
        long seed = 1000;

        pickerA = new RandomPicker<>(Arrays.asList(1,2,3,4,5), new Random(seed));
        System.out.print(pickerA.getElements());
        pickerB = new RandomPicker<>(initialList);
        pickerEmpty = new RandomPicker<>(new ArrayList<>());
    }

    public void testGetters(){

        assertFalse(pickerA.isEmpty());

        Integer randomValue1 = pickerA.extractRandomElement().orElse(-1);
        Integer randomValue2 = pickerA.extractRandomElement().orElse(-1);

        // This test is reproducible because seed is fixed
        assertEquals(3, randomValue1.intValue());
        assertEquals(1, randomValue2.intValue());
        assertFalse(pickerA.isEmpty());
    }

    public void testEmptyPicker(){
        Optional<Integer> emptyVal = pickerEmpty.extractRandomElement();
        assertTrue(pickerEmpty.isEmpty());
        assertTrue(emptyVal.isEmpty());
    }

    public void testFullExtraction(){

        List<Integer> returnedValues = new ArrayList<>();

        while(!pickerB.isEmpty()){
            returnedValues.add(pickerB.extractRandomElement().orElse(-1));
        }

        Collections.sort(returnedValues);

        assertEquals(initialList, returnedValues);
        assertTrue(pickerB.isEmpty());
    }

}