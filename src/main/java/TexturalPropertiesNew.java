import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TexturalPropertiesNew {

    private GTDMNew gtdm;
    private ArrayList<Double> s;
    private ArrayList<Double> p;
    private ArrayList<Double> sPrevious;
    private ArrayList<Double> pPrevious;
    private ArrayList<Double> sOrigin;
    private ArrayList<Double> pOrigin;
    private Double n2;
    private Double Coarness;
    private Double Contrast;
    private Double Busyness;
    private Double Complexity;
    private Double Strength;
    private String Color;
    private Double Epsilon = 0.00001;

    private Double psiNumeratorBusyness;
    private Double psiNumeratorBusynessOrigin;


    public TexturalPropertiesNew(GTDMNew gtdm)  {
        this.gtdm = gtdm;
        this.s = new ArrayList<>( gtdm.getS());
        this.p = new ArrayList<>( gtdm.getP());
        this.n2 = gtdm.getN2();
        this.Color = gtdm.getInputDataMatrix().getColor();

        //computeCoarnessContrastBusynessComplexityStrength();
        //psiNumeratorBusyness = 0.0;
        //computeCoarnessContrastBusynessComplexityStrength();
        testSpeedFirst();
    }

    /**
     * gtdm jest nowo wyliczone, tpPRevoius sa propsy poprzedniego gtdm wiec mozna z niego wyciagnac poprzednie gtdm
     * @param gtdm
     * @param tpPrevious
     */
    public TexturalPropertiesNew(GTDMNew gtdm,TexturalPropertiesNew tpPrevious , Boolean column)  {
        this.gtdm = new GTDMNew(gtdm);
        this.s = new ArrayList<>( gtdm.getS());
        this.p = new ArrayList<>( gtdm.getP());
        this.n2 = gtdm.getN2();
        this.Color = gtdm.getInputDataMatrix().getColor();

        //computeCoarnessContrastBusynessComplexityStrength();
        this.pPrevious = new ArrayList<>( tpPrevious.p);
        this.sPrevious = new ArrayList<>( tpPrevious.s);
        this.pOrigin = new ArrayList<>( tpPrevious.gtdm.getOriginP());
        this.sOrigin = new ArrayList<>( tpPrevious.gtdm.getOriginS());
        if (column) {
            psiNumeratorBusyness = tpPrevious.psiNumeratorBusyness;
            psiNumeratorBusynessOrigin = tpPrevious.psiNumeratorBusynessOrigin;
            testSpeedColumnCalculations();
        } else {
            psiNumeratorBusyness = tpPrevious.psiNumeratorBusynessOrigin;
            testSpeedRowCalculations();
        }
    }

    public TexturalPropertiesNew(Double coarness, Double contrast, Double busyness, Double complexity, Double strength, String color)  {
        this.Coarness = coarness;
        this.Contrast = contrast;
        this.Busyness = busyness;
        this.Complexity = complexity;
        this.Strength = strength;
        this.Color = color;
    }
    public void saveToCsv(String part){
        gtdm.saveToCSV(part);
    }


//    public Double computeCoarness() {
//        Double fcos = 0.001;
//        for (int i = 0 ; i< s.size() ; i++) {
//            //fcos += ss.getValue() * p.get(ss.getKey());
//            fcos += s.get(i) * p.get(i);
//        }
//        return Math.pow(fcos, -1);
//    }
//
//    public Double computeContrast() {
//        Double Ng = new Double(s.size());
//
//        Map<Double, Double> p1 = new HashMap<>(p);
//
//        Double i = 0.0;
//        Double j = 0.0;
//        Double firstPart = 0.0;
//        for (Map.Entry<Double, Double> pp : p.entrySet()) {
//            for (Map.Entry<Double, Double> pp1 : p1.entrySet()) {
//                i = pp.getKey();
//                j = pp1.getKey();
//                firstPart += pp.getValue() * pp1.getValue() * Math.pow((i - j), 2);
//            }
//        }
//        firstPart = 1 / Ng / (Ng - 1) * firstPart;
//
//        Double secondPart = 0.0;
//        for (Map.Entry<Double, Double> ss : s.entrySet()) {
//            secondPart += ss.getValue();
//        }
//        return firstPart * secondPart * 1 / n2;
//    }
//
//    public Double computeBusyness() {
//        Double licznik = 0.0;
//        for (Map.Entry<Double, Double> ss : s.entrySet()) {
//            licznik += ss.getValue() * p.get(ss.getKey());
//        }
//        Double i = 0.0;
//        Double j = 0.0;
//        Double mianownik = 0.0;
//        Map<Double, Double> p1 = new HashMap<>(p);
//        for (Map.Entry<Double, Double> pp : p.entrySet()) {
//            for (Map.Entry<Double, Double> pp1 : p1.entrySet()) {
//                i = pp.getKey();
//                j = pp1.getKey();
//                mianownik += (i * pp.getValue()) - (j * pp1.getValue());
//            }
//        }
//        return licznik / mianownik;
//    }
//
//    public Double computeComplexity() {
//        Double i = 0.0;
//        Double j = 0.0;
//        Double part = 0.0;
//        Double part2 = 0.0;
//        Double suma = 0.0;
//        Map<Double, Double> p1 = new HashMap<>(p);
//        for (Map.Entry<Double, Double> pp : p.entrySet()) {
//            for (Map.Entry<Double, Double> pp1 : p1.entrySet()) {
//                i = pp.getKey();
//                j = pp1.getKey();
//                part = Math.abs(i - j);
//                part /= n2 * (pp.getValue() + pp1.getValue());
//                part2 = pp.getValue() * s.get(pp.getKey());
//                part2 -= pp1.getValue() * s.get(pp1.getKey());
//                part *= part2;
//                suma += part;
//            }
//        }
//        return suma;
//    }
//
//    public Double computeStrength() {
//        Double i = 0.0;
//        Double j = 0.0;
//        Double part = 0.0;
//        Double part2 = 0.0;
//        Double suma = 0.0;
//        Map<Double, Double> p1 = new HashMap<>(p);
//        for (Map.Entry<Double, Double> pp : p.entrySet()) {
//            for (Map.Entry<Double, Double> pp1 : p1.entrySet()) {
//                i = pp.getKey();
//                j = pp1.getKey();
//                part = Math.pow((i - j), 2);
//                part *= (pp.getValue() + pp1.getValue());
//                suma += part;
//            }
//        }
//        Double fcos = 0.001;
//        for (Map.Entry<Double, Double> ss : s.entrySet()) {
//            fcos += ss.getValue();
//        }
//        return suma / fcos;
//    }

    public void testSpeedColumnCalculations() {
        Double Ng = 0.0;
        for (int i = 0 ; i < p.size() ;i++){
            if (p.get(i)!=0.0){
                Ng ++;
            }
        }
        ArrayList<Double> p1 = new ArrayList<>(p);
        Double partContrast = 0.0;
        Double denominatorBusyness = 0.0;
        Double partComplexity = 0.0;
        Complexity = 0.0;
        Strength = 0.0;
        Double partStrength = 0.0;

        gtdm.getChangedPixels().stream().forEach(i-> {
            psiNumeratorBusyness -= sPrevious.get(i.intValue())*pPrevious.get(i.intValue());
            psiNumeratorBusyness += s.get(i.intValue())*p.get(i.intValue());
        });


        for (int i = 0; i< p.size(); i++){
            if (p.get(i)==0.0)
                continue;
//            psiNumeratorBusyness += s.get(i)* p.get(i);
            for (int j = 0; j< p1.size(); j++){
                if (p1.get(j)==0.0)
                    continue;
                denominatorBusyness += i * p.get(i);
                denominatorBusyness -= j * p1.get(j);
                partContrast += p.get(i) * p1.get(j) * Math.pow((i - j), 2);
                partComplexity = Math.abs(i - j) / (n2 * (p.get(i) + p1.get(j)));
                partComplexity *= (p.get(i) * s.get(i)) + (p1.get(j) * s.get(j));
                Complexity += partComplexity;
                partStrength = Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
                Strength += partStrength;
            }
        }

        partContrast = 1 / Ng / (Ng - 1) * partContrast;
        Double siSum = s
                .stream()
                .mapToDouble(s -> s)
                .sum();

        Coarness = Math.pow(Epsilon + psiNumeratorBusyness, -1);
        Contrast = partContrast * siSum * 1 / n2;
        Busyness = psiNumeratorBusyness / denominatorBusyness;
        Strength /= siSum;

    }

    public void testSpeedRowCalculations() {
        Double Ng = 0.0;
        for (int i = 0 ; i < p.size() ;i++){
            if (p.get(i)!=0.0){
                Ng ++;
            }
        }
        ArrayList<Double> p1 = new ArrayList<>(p);
        Double partContrast = 0.0;
        Double denominatorBusyness = 0.0;
        Double partComplexity = 0.0;
        Complexity = 0.0;
        Strength = 0.0;
        Double partStrength = 0.0;

        gtdm.getChangedPixels().stream().forEach(i-> {
            psiNumeratorBusyness -=  sOrigin.get(i.intValue())*pOrigin.get(i.intValue());
            psiNumeratorBusyness += s.get(i.intValue())*p.get(i.intValue());
        });


        for (int i = 0; i< p.size(); i++){
            if (p.get(i)==0.0)
                continue;
            //psiNumeratorBusyness += s.get(i)* p.get(i);
            for (int j = 0; j< p1.size(); j++){
                if (p1.get(j)==0.0)
                    continue;
                denominatorBusyness += i * p.get(i);
                denominatorBusyness -= j * p1.get(j);
                partContrast += p.get(i) * p1.get(j) * Math.pow((i - j), 2);
                partComplexity = Math.abs(i - j) / (n2 * (p.get(i) + p1.get(j)));
                partComplexity *= (p.get(i) * s.get(i)) + (p1.get(j) * s.get(j));
                Complexity += partComplexity;
                partStrength = Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
                Strength += partStrength;
            }
        }

        partContrast = 1 / Ng / (Ng - 1) * partContrast;
        Double siSum = s
                .stream()
                .mapToDouble(s -> s)
                .sum();

        Coarness = Math.pow(Epsilon + psiNumeratorBusyness, -1);
        System.out.println(Coarness);
        Contrast = partContrast * siSum * 1 / n2;
        Busyness = psiNumeratorBusyness / denominatorBusyness;
        Strength /= siSum;

        psiNumeratorBusynessOrigin = psiNumeratorBusyness;
    }





    public void testSpeedFirst() {
        Double Ng = 0.0;
        for (int i = 0 ; i < p.size() ;i++){
            if (p.get(i)!=0.0){
                Ng ++;
            }
        }
        ArrayList<Double> p1 = new ArrayList<>(p);
        psiNumeratorBusyness = 0.0;

        for (int i = 0; i<s.size(); i++){
            psiNumeratorBusyness += s.get(i)* p.get(i);
        }

        Double partContrast = 0.0;
        Double denominatorBusyness = 0.0;
        Double partComplexity = 0.0;
        Complexity = 0.0;
        Strength = 0.0;
        Double partStrength = 0.0;


        for (int i = 0; i< p.size(); i++){
            if (p.get(i)==0.0)
                continue;
            for (int j = 0; j< p1.size(); j++){
                if (p1.get(j)==0.0)
                    continue;
                denominatorBusyness += i * p.get(i);
                denominatorBusyness -= j * p1.get(j);
                partContrast += p.get(i) * p1.get(j) * Math.pow((i - j), 2);
                partComplexity = Math.abs(i - j) / (n2 * (p.get(i) + p1.get(j)));
                partComplexity *= (p.get(i) * s.get(i)) + (p1.get(j) * s.get(j));
                Complexity += partComplexity;
                partStrength = Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
                Strength += partStrength;
            }
        }

        partContrast = 1 / Ng / (Ng - 1) * partContrast;
        Double siSum = s
                .stream()
                .mapToDouble(s -> s)
                .sum();

        Coarness = Math.pow(Epsilon + psiNumeratorBusyness, -1);
        Contrast = partContrast * siSum * 1 / n2;
        Busyness = psiNumeratorBusyness / denominatorBusyness;
        Strength /= siSum;

        psiNumeratorBusynessOrigin = psiNumeratorBusyness;
    }



    public void computeCoarnessContrastBusynessComplexityStrength() {
        Double Ng = 0.0;
        for (int i = 0 ; i < p.size() ;i++){
            if (p.get(i)!=0.0){
                Ng ++;
            }
        }
        ArrayList<Double> p1 = new ArrayList<>(p);
        Double psiNumeratorBusyness = 0.0;

        for (int i = 0; i<s.size(); i++){
            psiNumeratorBusyness += s.get(i)* p.get(i);
        }

        Double partContrast = 0.0;
        Double denominatorBusyness = 0.0;
        Double partComplexity = 0.0;
        Complexity = 0.0;
        Strength = 0.0;
        Double partStrength = 0.0;


        for (int i = 0; i< p.size(); i++){
            if (p.get(i)==0.0)
                continue;
            for (int j = 0; j< p1.size(); j++){
                if (p1.get(j)==0.0)
                    continue;
                denominatorBusyness += i * p.get(i);
                denominatorBusyness -= j * p1.get(j);
                partContrast += p.get(i) * p1.get(j) * Math.pow((i - j), 2);
                partComplexity = Math.abs(i - j) / (n2 * (p.get(i) + p1.get(j)));
                partComplexity *= (p.get(i) * s.get(i)) + (p1.get(j) * s.get(j));
                Complexity += partComplexity;
                partStrength = Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
                Strength += partStrength;
            }
        }

        partContrast = 1 / Ng / (Ng - 1) * partContrast;
        Double siSum = s
                .stream()
                .mapToDouble(s -> s)
                .sum();

        Coarness = Math.pow(Epsilon + psiNumeratorBusyness, -1);
        //System.out.println(Coarness);
        Contrast = partContrast * siSum * 1 / n2;
        Busyness = psiNumeratorBusyness / denominatorBusyness;
        Strength /= siSum;
    }
    @Override
    public String toString(){
        return "\n\nCOLOR:  " + Color +
        "\nCoarness:  " + Coarness +
        "\nContrast:  " + Contrast +
        "\nBusyness:  " + Busyness +
        "\nComplexity:  " + Complexity +
        "\nStrength:  " + Strength;
    }

    public Double getCoarness() {
        return Coarness;
    }

    public Double getContrast() {
        return Contrast;
    }

    public Double getBusyness() {
        return Busyness;
    }

    public Double getComplexity() {
        return Complexity;
    }

    public Double getStrength() {
        return Strength;
    }

    public String getColor(){
        return Color;
    }

    public Map<String,Double> getProps(){
        Map map = new HashMap<String,Double>();
        map.put(Constans.COARNESS,Coarness);
        map.put(Constans.CONTRAST,Contrast);
        map.put(Constans.BUSYNESS,Math.abs(Busyness));
        map.put(Constans.COMPLEXITY,Complexity);
        map.put(Constans.STRENGTH,Strength);
        return  map;
    }
}
