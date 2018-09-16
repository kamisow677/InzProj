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
    public static void averageMatrix(TexturalPropertiesNew t1, TexturalPropertiesNew t2, TexturalPropertiesNew t3){
        System.out.println("\n\nCoarness:  " +  (t1.getCoarness()+t2.getCoarness()+t3.getCoarness())/3);
        System.out.println("Contrast:  " +  (t1.getContrast()+t2.getContrast()+t3.getContrast())/3);
        System.out.println("Busyness:  " +  (t1.getBusyness()+t2.getBusyness()+t3.getBusyness())/3);
        System.out.println("Complexity:  " +  (t1.getComplexity()+t2.getComplexity()+t3.getComplexity())/3);
        System.out.println("Strength:  " +  (t1.getStrength()+t2.getStrength()+t3.getStrength())/3);
    }

}
