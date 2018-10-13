import java.util.*;

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
    private Double Epsilon = 0.00000001;

    private Double psiNumeratorBusyness;
    private Double psiNumeratorBusynessOrigin;

    private Double partContrast;
    private Double partContrastOrigin;

    private Double partStrength;
    private Double partStrengthOrigin;

    private Double partComplexity;
    private Double partComplexityOrigin;

    public TexturalPropertiesNew(GTDMNew gtdm)  {
        this.gtdm = gtdm;
        this.s = new ArrayList<>( gtdm.getS());
        this.p = new ArrayList<>( gtdm.getP());
        this.n2 = gtdm.getN2();
        this.Color = gtdm.getInputDataMatrix().getColor();

        //computeCoarnessContrastBusynessComplexityStrength();
        //psiNumeratorBusyness = 0.0;
//        computeCoarnessContrastBusynessComplexityStrength();
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
            partContrast = tpPrevious.partContrast;
            partContrastOrigin = tpPrevious.partContrastOrigin;
            partStrength = tpPrevious.partStrength;
            partStrengthOrigin = tpPrevious.partStrengthOrigin;
            partComplexity = tpPrevious.partComplexity;
            partComplexityOrigin = tpPrevious.partComplexityOrigin;
            testSpeedColumnCalculations();
        } else {
            psiNumeratorBusyness = tpPrevious.psiNumeratorBusynessOrigin;
            partContrast = tpPrevious.partContrastOrigin;
            partStrength = tpPrevious.partStrengthOrigin;
            partComplexity = tpPrevious.partComplexityOrigin;
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
        //Double partContrast = 0.0;
        Double denominatorBusyness = 0.0;
//        Double partComplexity = 0.0;
        Complexity = 0.0;
        Strength = 0.0;
        //Double partStrength = 0.0;

        Iterator<Double> iterator = gtdm.getChangedPixels().iterator();
        while(iterator.hasNext()) {
            int i = iterator.next().intValue();
            if (pPrevious.get(i)==0.0)
                continue;
            psiNumeratorBusyness -= sPrevious.get(i)*pPrevious.get(i);
            for (int j = 0; j< p1.size(); j++) {
                if (pPrevious.get(j)==0.0)
                    continue;
                partContrast -= pPrevious.get(i) * pPrevious.get(j) * Math.pow((i - j), 2);
                partStrength -= Math.pow((i - j), 2) * (pPrevious.get(i) + pPrevious.get(j));
                partComplexity -= (Math.abs(i - j) / (n2 * (pPrevious.get(i) + pPrevious.get(j)))) * ((pPrevious.get(i) * sPrevious.get(i)) + (pPrevious.get(j) * sPrevious.get(j)));
                Constans.a++;
            }
        }
        iterator = gtdm.getChangedPixels().iterator();
        while(iterator.hasNext()) {
            int i = iterator.next().intValue();
            if (p.get(i)==0.0)
                continue;
            psiNumeratorBusyness += s.get(i)*p.get(i);
            for (int j = 0; j< p1.size(); j++) {
                if (p.get(j)==0.0)
                    continue;
                partContrast += p.get(i) * p1.get(j) * Math.pow((i - j), 2);
                partStrength += Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
                partComplexity += (Math.abs(i - j) / (n2 * (p.get(i) + p.get(j)))) * ((p.get(i) * s.get(i)) + (p.get(j) * s.get(j)));
                Constans.a++;
            }
        }

//        Iterator<Double> iterator = gtdm.getChangedPixels().iterator();
//        while(iterator.hasNext()) {
//            int i = iterator.next().intValue();
//            if (pPrevious.get(i)==0.0 && p.get(i)==0.0) {
//                continue;
//            }
//            else if (pPrevious.get(i)==0.0){
//                psiNumeratorBusyness += s.get(i)*p.get(i);
//            }
//            else if (p.get(i)==0.0){
//                psiNumeratorBusyness -= sPrevious.get(i)*pPrevious.get(i);
//            }else{
//                psiNumeratorBusyness -= sPrevious.get(i)*pPrevious.get(i);
//                psiNumeratorBusyness += s.get(i)*p.get(i);
//            }
//            for (int j = 0; j< p1.size(); j++) {
//                if (pPrevious.get(j)==0.0 && p.get(j)==0.0) {
//                    continue;
//                } else if (pPrevious.get(j)==0.0){
//                    if (p.get(i)==0.0)
//                        continue;
//                    partContrast += p.get(i) * p1.get(j) * Math.pow((i - j), 2);
//                    partStrength += Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
//                    partComplexity += (Math.abs(i - j) / (n2 * (p.get(i) + p.get(j)))) * ((p.get(i) * s.get(i)) + (p.get(j) * s.get(j)));
//                }
//                else if (p.get(j)==0.0){
//                    if (pPrevious.get(i)==0.0)
//                        continue;
//                    partContrast -= pPrevious.get(i) * pPrevious.get(j) * Math.pow((i - j), 2);
//                    partStrength -= Math.pow((i - j), 2) * (pPrevious.get(i) + pPrevious.get(j));
//                    partComplexity -= (Math.abs(i - j) / (n2 * (pPrevious.get(i) + pPrevious.get(j)))) * ((pPrevious.get(i) * sPrevious.get(i)) + (pPrevious.get(j) * sPrevious.get(j)));
//                } else  {
//                    if (p.get(i)==0.0) {
//                        partContrast -= pPrevious.get(i) * pPrevious.get(j) * Math.pow((i - j), 2);
//                        partStrength -= Math.pow((i - j), 2) * (pPrevious.get(i) + pPrevious.get(j));
//                        partComplexity -= (Math.abs(i - j) / (n2 * (pPrevious.get(i) + pPrevious.get(j)))) * ((pPrevious.get(i) * sPrevious.get(i)) + (pPrevious.get(j) * sPrevious.get(j)));
//                    } else if (pPrevious.get(i)==0.0) {
//                        partContrast += p.get(i) * p1.get(j) * Math.pow((i - j), 2);
//                        partStrength += Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
//                        partComplexity += (Math.abs(i - j) / (n2 * (p.get(i) + p.get(j)))) * ((p.get(i) * s.get(i)) + (p.get(j) * s.get(j)));
//                    } else {
//                        partContrast -= pPrevious.get(i) * pPrevious.get(j) * Math.pow((i - j), 2);
//                        partContrast += p.get(i) * p1.get(j) * Math.pow((i - j), 2);
//                        partStrength -= Math.pow((i - j), 2) * (pPrevious.get(i) + pPrevious.get(j));
//                        partStrength += Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
//                        partComplexity -= (Math.abs(i - j) / (n2 * (pPrevious.get(i) + pPrevious.get(j)))) * ((pPrevious.get(i) * sPrevious.get(i)) + (pPrevious.get(j) * sPrevious.get(j)));
//                        partComplexity += (Math.abs(i - j) / (n2 * (p.get(i) + p.get(j)))) * ((p.get(i) * s.get(i)) + (p.get(j) * s.get(j)));
//                    }
//                }
//                Constans.a++;
//            }
//        }




        List rest = new ArrayList();
        for (int j = 0; j< p1.size(); j++) {
            if (!gtdm.getChangedPixels().contains(new Double(j))){
                rest.add(new Double(j));
            }
        }

        for (Object k : rest) {
            int j = ((Double) k).intValue();
            if (pPrevious.get(j)==0.0)
                continue;
            iterator = gtdm.getChangedPixels().iterator();
            while (iterator.hasNext()) {
                int  i = iterator.next().intValue();
                if (pPrevious.get(i)==0.0)
                    continue;
                partContrast -= pPrevious.get(i) * pPrevious.get(j) * Math.pow((j - i), 2);
                partStrength -= Math.pow((i - j), 2) * (pPrevious.get(i) + pPrevious.get(j));
                partComplexity -= (Math.abs(i - j) / (n2 * (pPrevious.get(i) + pPrevious.get(j)))) * ((pPrevious.get(i) * sPrevious.get(i)) + (pPrevious.get(j) * sPrevious.get(j)));
                Constans.a++;
            }

        }
        for (Object k : rest) {
            int j = ((Double) k).intValue();
            if (p.get(j)==0.0)
                continue;
            iterator = gtdm.getChangedPixels().iterator();
            while (iterator.hasNext()) {
                int  i = iterator.next().intValue();
                if (p.get(i)==0.0)
                    continue;
                partContrast += p.get(i) * p1.get(j) * Math.pow((j - i), 2);
                partStrength += Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
                partComplexity += (Math.abs(i - j) / (n2 * (p.get(i) + p.get(j)))) * ((p.get(i) * s.get(i)) + (p.get(j) * s.get(j)));
                Constans.a++;
            }
        }

//        for (Object k : rest) {
//            int j = ((Double) k).intValue();
//            if (pPrevious.get(j)==0.0 && p.get(j)==0.0) {
//                continue;
//            }
//            iterator = gtdm.getChangedPixels().iterator();
//            while (iterator.hasNext()) {
//                int  i = iterator.next().intValue();
//                if (pPrevious.get(i)==0.0 && p.get(i)==0.0) {
//                    continue;
//                } else if (p.get(i)==0.0){
//                    if (pPrevious.get(j)==0.0)
//                        continue;
//                    partContrast -= pPrevious.get(i) * pPrevious.get(j) * Math.pow((j - i), 2);
//                    partStrength -= Math.pow((i - j), 2) * (pPrevious.get(i) + pPrevious.get(j));
//                    partComplexity -= (Math.abs(i - j) / (n2 * (pPrevious.get(i) + pPrevious.get(j)))) * ((pPrevious.get(i) * sPrevious.get(i)) + (pPrevious.get(j) * sPrevious.get(j)));
//                } else if (pPrevious.get(i)==0.0){
//                    if (p.get(j)==0.0)
//                        continue;
//                    partContrast += p.get(i) * p1.get(j) * Math.pow((j - i), 2);
//                    partStrength += Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
//                    partComplexity += (Math.abs(i - j) / (n2 * (p.get(i) + p.get(j)))) * ((p.get(i) * s.get(i)) + (p.get(j) * s.get(j)));
//                } else {
//                    if (p.get(j)==0.0) {
//                        partContrast -= pPrevious.get(i) * pPrevious.get(j) * Math.pow((j - i), 2);
//                        partStrength -= Math.pow((i - j), 2) * (pPrevious.get(i) + pPrevious.get(j));
//                        partComplexity -= (Math.abs(i - j) / (n2 * (pPrevious.get(i) + pPrevious.get(j)))) * ((pPrevious.get(i) * sPrevious.get(i)) + (pPrevious.get(j) * sPrevious.get(j)));
//                    }else if (pPrevious.get(j)==0.0) {
//                        partContrast += p.get(i) * p1.get(j) * Math.pow((j - i), 2);
//                        partStrength += Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
//                        partComplexity += (Math.abs(i - j) / (n2 * (p.get(i) + p.get(j)))) * ((p.get(i) * s.get(i)) + (p.get(j) * s.get(j)));
//                    } else {
//                        partContrast -= pPrevious.get(i) * pPrevious.get(j) * Math.pow((j - i), 2);
//                        partStrength -= Math.pow((i - j), 2) * (pPrevious.get(i) + pPrevious.get(j));
//                        partComplexity -= (Math.abs(i - j) / (n2 * (pPrevious.get(i) + pPrevious.get(j)))) * ((pPrevious.get(i) * sPrevious.get(i)) + (pPrevious.get(j) * sPrevious.get(j)));
//                        partContrast += p.get(i) * p1.get(j) * Math.pow((j - i), 2);
//                        partStrength += Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
//                        partComplexity += (Math.abs(i - j) / (n2 * (p.get(i) + p.get(j)))) * ((p.get(i) * s.get(i)) + (p.get(j) * s.get(j)));
//                    }
//                }
//
//                Constans.a++;
//            }
//
//        }






//        for (int i = 0; i< p.size(); i++){
//            if (p.get(i)==0.0)
//                continue;
////            psiNumeratorBusyness += s.get(i)* p.get(i);
//            for (int j = 0; j< p1.size(); j++){
//                if (p1.get(j)==0.0)
//                    continue;
//                denominatorBusyness += i * p.get(i);
//                denominatorBusyness -= j * p1.get(j);
//                //partContrast += p.get(i) * p1.get(j) * Math.pow((i - j), 2);
////                partComplexity = Math.abs(i - j) / (n2 * (p.get(i) + p1.get(j)));
////                partComplexity *= (p.get(i) * s.get(i)) + (p1.get(j) * s.get(j));
////                Complexity += partComplexity;
//               // partStrength += Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
//
//            }
//        }

        Complexity = partComplexity;
        Contrast = 1 / Ng / (Ng - 1) * partContrast;
        Double siSum = s
                .stream()
                .mapToDouble(s -> s)
                .sum();

        Coarness = Math.pow(Epsilon + psiNumeratorBusyness, -1);
        Contrast = Contrast * siSum * 1 / n2;
        Busyness = psiNumeratorBusyness / denominatorBusyness;
        Strength = partStrength /(Epsilon +siSum);

    }


    /**
     *
     *
     */
    public void testSpeedRowCalculations() {
        Double Ng = 0.0;
        for (int i = 0 ; i < p.size() ;i++){
            if (p.get(i)!=0.0){
                Ng ++;
            }
        }
        ArrayList<Double> p1 = new ArrayList<>(p);
        //Double partContrast = 0.0;
        Double denominatorBusyness = 0.0;
//        Double partComplexity = 0.0;
        Complexity = 0.0;
        Strength = 0.0;
       // Double partStrength = 0.0;



        Iterator<Double> iterator = gtdm.getChangedPixels().iterator();
        while(iterator.hasNext()) {
            int i = iterator.next().intValue();
            if (pOrigin.get(i)==0.0)
                continue;
            psiNumeratorBusyness -= pOrigin.get(i)*sOrigin.get(i);
            for (int j = 0; j< p1.size(); j++) {
                if (pOrigin.get(j)==0.0)
                    continue;
                partContrast -= pOrigin.get(i) * pOrigin.get(j) * Math.pow((i - j), 2);
                partStrength -= Math.pow((i - j), 2) * (pOrigin.get(i) + pOrigin.get(j));
                partComplexity -= (Math.abs(i - j) / (n2 * (pOrigin.get(i) + pOrigin.get(j)))) * ((pOrigin.get(i) * sOrigin.get(i)) + (pOrigin.get(j) * sOrigin.get(j)));
                Constans.a++;
            }
        }
        iterator = gtdm.getChangedPixels().iterator();
        while(iterator.hasNext()) {
            int i = iterator.next().intValue();
            if (p.get(i)==0.0)
                continue;
            psiNumeratorBusyness += s.get(i)*p.get(i);
            for (int j = 0; j< p1.size(); j++) {
                if (p.get(j)==0.0)
                    continue;
                partContrast += p.get(i) * p.get(j) * Math.pow((i - j), 2);
                partStrength += Math.pow((i - j), 2) * (p.get(i) + p.get(j));
                partComplexity += (Math.abs(i - j) / (n2 * (p.get(i) + p1.get(j)))) * ((p.get(i) * s.get(i)) + (p1.get(j) * s.get(j)));
                Constans.a++;
            }
        }

        List rest = new ArrayList();
        for (int j = 0; j< p1.size(); j++) {
            if (!gtdm.getChangedPixels().contains(new Double(j))){
                rest.add(new Double(j));
            }
        }

        for (Object k : rest) {
            int j = ((Double) k).intValue();
            if (pOrigin.get(j)==0.0)
                continue;
            iterator = gtdm.getChangedPixels().iterator();
            while (iterator.hasNext()) {
                int  i = iterator.next().intValue();
                if (pOrigin.get(i)==0.0)
                    continue;
                partContrast -= pOrigin.get(i) * pOrigin.get(j) * Math.pow((j - i), 2);
                partStrength -= Math.pow((i - j), 2) * (pOrigin.get(i) + pOrigin.get(j));
                partComplexity -= (Math.abs(i - j) / (n2 * (pOrigin.get(i) + pOrigin.get(j)))) * ((pOrigin.get(i) * sOrigin.get(i)) + (pOrigin.get(j) * sOrigin.get(j)));
                Constans.a++;
            }

        }
        for (Object k : rest) {
            int j = ((Double) k).intValue();
            if (p.get(j)==0.0)
                continue;
            iterator = gtdm.getChangedPixels().iterator();
            while (iterator.hasNext()) {
                int  i = iterator.next().intValue();
                if (p.get(i)==0.0)
                    continue;
                partContrast += p.get(i) * p.get(j) * Math.pow((j - i), 2);
                partStrength += Math.pow((i - j), 2) * (p.get(i) + p.get(j));
                partComplexity += (Math.abs(i - j) / (n2 * (p.get(i) + p1.get(j)))) * ((p.get(i) * s.get(i)) + (p1.get(j) * s.get(j)));
                Constans.a++;
            }
        }





//        gtdm.getChangedPixels().stream().forEach(i-> {
//            psiNumeratorBusyness -=  sOrigin.get(i.intValue())*pOrigin.get(i.intValue());
//            psiNumeratorBusyness += s.get(i.intValue())*p.get(i.intValue());
//        });


//        for (int i = 0; i< p.size(); i++){
//            if (p.get(i)==0.0)
//                continue;
//            //psiNumeratorBusyness += s.get(i)* p.get(i);
//            for (int j = 0; j< p1.size(); j++){
//                if (p1.get(j)==0.0)
//                    continue;
//                denominatorBusyness += i * p.get(i);
//                denominatorBusyness -= j * p1.get(j);
//                //partContrast += p.get(i) * p1.get(j) * Math.pow((i - j), 2);
////                partComplexity = Math.abs(i - j) / (n2 * (p.get(i) + p1.get(j)));
////                partComplexity *= (p.get(i) * s.get(i)) + (p1.get(j) * s.get(j));
////                Complexity += partComplexity;
//             //   partStrength += Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
//            }
//        }

        Complexity = partComplexity;
        Contrast = 1 / Ng / (Ng - 1) * partContrast;
        Double siSum = s
                .stream()
                .mapToDouble(s -> s)
                .sum();

        Coarness = Math.pow(Epsilon + psiNumeratorBusyness, -1);
        Contrast = Contrast * siSum * 1 / n2;
        Busyness = psiNumeratorBusyness / denominatorBusyness;
        Strength = partStrength / siSum;

        psiNumeratorBusynessOrigin = psiNumeratorBusyness;
        partContrastOrigin = partContrast;
        partStrengthOrigin = partStrength;
        partComplexityOrigin = partComplexity;
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

        partContrast = 0.0;
        Double denominatorBusyness = 0.0;
        partComplexity = 0.0;
        Complexity = 0.0;
        Strength = 0.0;
        partStrength = 0.0;


        for (int i = 0; i< p.size(); i++){
            if (p.get(i)==0.0)
                continue;
            for (int j = 0; j< p1.size(); j++){
                if (p1.get(j)==0.0)
                    continue;
//                denominatorBusyness += i * p.get(i);
//                denominatorBusyness -= j * p1.get(j);
                partContrast += p.get(i) * p1.get(j) * Math.pow((i - j), 2);
                partComplexity += (Math.abs(i - j) / (n2 * (p.get(i) + p1.get(j)))) * ((p.get(i) * s.get(i)) + (p1.get(j) * s.get(j)));
                partStrength += Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
                Constans.b++;
            }
        }
        Complexity = partComplexity;
        Contrast = 1 / Ng / (Ng - 1) * partContrast;
        Double siSum = s
                .stream()
                .mapToDouble(s -> s)
                .sum();


        Coarness = Math.pow(Epsilon + psiNumeratorBusyness, -1);
        Contrast = Contrast * siSum * 1 / n2;
        Busyness = psiNumeratorBusyness / denominatorBusyness;
        Strength = partStrength / siSum;

        psiNumeratorBusynessOrigin = psiNumeratorBusyness;
        partContrastOrigin = partContrast;
        partStrengthOrigin = partStrength;
        partComplexityOrigin = partComplexity;
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
        partComplexity = 0.0;
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
                partComplexity += (Math.abs(i - j) / (n2 * (p.get(i) + p1.get(j)))) * ((p.get(i) * s.get(i)) + (p1.get(j) * s.get(j)));
                partStrength += Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
            }
        }

        Complexity = partComplexity;
        Contrast = 1 / Ng / (Ng - 1) * partContrast;
        Double siSum = s
                .stream()
                .mapToDouble(s -> s)
                .sum();

        Coarness = Math.pow(Epsilon + psiNumeratorBusyness, -1);
        //System.out.println(Coarness);
        Contrast = Contrast * siSum * 1 / n2;
        Busyness = psiNumeratorBusyness / denominatorBusyness;
        Strength = partStrength / siSum;
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
