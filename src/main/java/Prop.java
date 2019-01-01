import java.util.ArrayList;
import java.util.Map;

/**
 * @author Kamil Sowa
 * @version 1.0
 * Klasa do przechowywaia nazwy teksury jej wartości i pozycji w algorytmie usuwania skrajnych wartości
 *
 */
public class Prop {
    /**
     * Nazwa cechy struktury
     */
    String name;
    /**
     * Wartość cechy
     */
    Double value;
    /**
     * Pozycja cechy
     */
    int pos;

    /**
     * Akcesor wartości cechy
     * @return wartośćcechy
     */
    public Double getValue() {
        return value;
    }
    /**
     * UStawienie nowe wartości cechy
     * @param value wartość cechy
     */
    public void setValue(Double value) {
        this.value = value;
    }

    /**
     * Konstruktor
     * @param name nazwa cechy struktury
     * @param value wartość cechy
     * @param pos pozycja cechy
     */

    public Prop(String name, Double value, int pos) {
        this.name = name;
        this.value = value;
        this.pos = pos;
    }
}
