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
     * nazwa cechy struktury
     */
    String name;
    /**
     * wartość cechy
     */
    Double value;
    /**
     * pozycja cechy
     */
    int pos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
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
    public  Prop(Map<String,Double> m, int pos){
        this.pos = pos;

    }
}
