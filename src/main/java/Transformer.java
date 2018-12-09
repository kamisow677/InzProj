import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author Kamil Sowa
 * @version 1.0
 * Klasa ta służy do fabrykacji specyficznego zestawu cech tekstur zbudowanego na podstawie innego zestawu w opraciu o klasy TexturalProperties
 *
 */
public class Transformer {

    /**
     * Ta metoda tworzy nowy obiekt klasy TexturalProperties, gdzie cechy tekstury są średnią przekazanych w argumentach wartości
     * @param arr lista z obiektami TexturalProperties
     * @param color kolor obrazu, dla którego tworzone są cechy
     * @return nowy obiekt klasy TexturalProperties
     */
    public static TexturalProperties averageProperties(ArrayList<TexturalProperties> arr, String color ){
        Double coarness = (arr.stream().mapToDouble(asd -> asd.getCoarness()).sum())/arr.size();
        Double contrast = (arr.stream().mapToDouble(asd -> asd.getContrast()).sum())/arr.size();
        Double busyness = (arr.stream().mapToDouble(asd -> asd.getBusyness()).sum())/arr.size();
        Double complexity =( arr.stream().mapToDouble(asd -> asd.getComplexity()).sum())/arr.size();
        Double strength = (arr.stream().mapToDouble(asd -> asd.getStrength()).sum())/arr.size();
        return new TexturalProperties(coarness,contrast,busyness,complexity,strength, color);
    }


}
