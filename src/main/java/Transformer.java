import net.imglib2.img.array.ArrayRandomAccess;

import java.util.ArrayList;
import java.util.HashMap;

public class Transformer {
    public static ArrayList<Double>  mapToArrayList(HashMap<Double,Double> inputMap){
        ArrayList<Double> arrayList= new ArrayList<>(255);
        for (int i = 0 ; i<255 ; i++){
            Double value = inputMap.get(i);
            if (value != null)
                arrayList.add(i, value);
        }
        return arrayList;
    }
}
