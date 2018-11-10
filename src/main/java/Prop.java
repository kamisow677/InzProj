import java.util.ArrayList;
import java.util.Map;

public class Prop {
    String name;
    Double value;
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

    public Prop(String name, Double value, int pos) {
        this.name = name;
        this.value = value;
        this.pos = pos;
    }
    public  Prop(Map<String,Double> m, int pos){
        this.pos = pos;

    }
}
