import java.util.ArrayList;
import java.util.HashMap;

public class Transformer {
    public static ArrayList<Double>  mapToArrayList(HashMap<Double,Double> inputMap){
        ArrayList<Double> arrayList= new ArrayList<>(256);
        for (int i = 0 ; i<256 ; i++){
            Double value = inputMap.get(i);
            if (value != null)
                arrayList.add(i, value);
        }
        return arrayList;
    }
    public static void averageProperties(TexturalProperties t1, TexturalProperties t2, TexturalProperties t3){
        System.out.println("\n\nAVERAGE MATRIX");
        System.out.println("Coarness:  " +  (t1.getCoarness()+t2.getCoarness()+t3.getCoarness())/3);
        System.out.println("Contrast:  " +  (t1.getContrast()+t2.getContrast()+t3.getContrast())/3);
        System.out.println("Busyness:  " +  (t1.getBusyness()+t2.getBusyness()+t3.getBusyness())/3);
        System.out.println("Complexity:  " +  (t1.getComplexity()+t2.getComplexity()+t3.getComplexity())/3);
        System.out.println("Strength:  " +  (t1.getStrength()+t2.getStrength()+t3.getStrength())/3);
    }
    public static TexturalProperties averageProperties(ArrayList<TexturalProperties> arr, String color ){
      //  System.out.println("\n\nSUM MATRIX "+ color);
        Double coarness = (arr.stream().mapToDouble(asd -> asd.getCoarness()).sum())/arr.size();
        Double contrast = (arr.stream().mapToDouble(asd -> asd.getContrast()).sum())/arr.size();
        Double busyness = (arr.stream().mapToDouble(asd -> asd.getBusyness()).sum())/arr.size();
        Double complexity =( arr.stream().mapToDouble(asd -> asd.getComplexity()).sum())/arr.size();
        Double strength = (arr.stream().mapToDouble(asd -> asd.getStrength()).sum())/arr.size();
//        System.out.println("Coarness:  " +  arr.stream().mapToDouble(asd -> asd.getCoarness()).sum());
//        System.out.println("Contrast:  " +  arr.stream().mapToDouble(asd -> asd.getContrast()).sum());
//        System.out.println("Busyness:  " +  arr.stream().mapToDouble(asd -> asd.getBusyness()).sum());
//        System.out.println("Complexity:  " +  arr.stream().mapToDouble(asd -> asd.getComplexity()).sum());
//        System.out.println("Strength:  " +  arr.stream().mapToDouble(asd -> asd.getStrength()).sum());
        return new TexturalProperties(coarness,contrast,busyness,complexity,strength, color);
    }

}
