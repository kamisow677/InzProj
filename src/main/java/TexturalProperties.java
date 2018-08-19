import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TexturalProperties {

    private GTDM toneMatrix;
    private Map<Double, Double> s;
    private Map<Double, Double> p;
    private Double n2;

    public TexturalProperties(GTDM toneMatrix) throws FileNotFoundException, UnsupportedEncodingException {
        this.toneMatrix = toneMatrix;
        this.s = toneMatrix.getS();
        this.p = toneMatrix.getP();
        this.n2 = toneMatrix.getN2();

        System.out.println("\n\nCOLOR "+ toneMatrix.getInputDataMatrix().getColor());
        System.out.println("Coarness:  " + computeCoarness());
        System.out.println("Coarness Parallel:  " + computeCoarnessParallel());
        System.out.println("Contrast:  " + computeContrast());
        System.out.println("Busyness:  " + computeBusyness());
        System.out.println("Complexity:  " + computeComplexity());
        System.out.println("Strength:  " + computeStrength());

//        long startTime = System.currentTimeMillis();
//        computeCoarnessContrastBusynessComplexity();
//        long stopTime = System.currentTimeMillis();
//        long elapsedTime = stopTime - startTime;
//        System.out.println(elapsedTime);
//
//        startTime = System.currentTimeMillis();
//        computeCoarnessContrastBusynessComplexityParallel();
//        stopTime = System.currentTimeMillis();
//        elapsedTime = stopTime - startTime;
//        System.out.println(elapsedTime);
    }

    public Double computeCoarness() {
        Double fcos = 0.001;
        for (Map.Entry<Double, Double> ss : s.entrySet()) {
            fcos += ss.getValue() * p.get(ss.getKey());
        }
        return Math.pow(fcos, -1);
    }

    public Double computeCoarnessParallel() {
        Double fcos = 0.001;
        return Math.pow( s.entrySet()
                .parallelStream()
                .mapToDouble((ss)->ss.getValue()*p.get(ss.getKey()))
                .sum()
                , -1);
    }

    public Double computeContrast() {
        Double Ng = new Double(s.size());

        Map<Double, Double> p1 = new HashMap<>(p);

        Double i = 0.0;
        Double j = 0.0;
        Double firstPart = 0.0;
        for (Map.Entry<Double, Double> pp : p.entrySet()) {
            for (Map.Entry<Double, Double> pp1 : p1.entrySet()) {
                i = pp.getKey();
                j = pp1.getKey();
                firstPart += pp.getValue() * pp1.getValue() * Math.pow((i - j), 2);
            }
        }
        firstPart = 1 / Ng / (Ng - 1) * firstPart;

        Double secondPart = 0.0;
        for (Map.Entry<Double, Double> ss : s.entrySet()) {
            secondPart += ss.getValue();
        }
        return firstPart * secondPart * 1 / n2;
    }

    public Double computeBusyness() {
        Double licznik = 0.0;
        for (Map.Entry<Double, Double> ss : s.entrySet()) {
            licznik += ss.getValue() * p.get(ss.getKey());
        }
        Double i = 0.0;
        Double j = 0.0;
        Double mianownik = 0.0;
        Map<Double, Double> p1 = new HashMap<>(p);
        for (Map.Entry<Double, Double> pp : p.entrySet()) {
            for (Map.Entry<Double, Double> pp1 : p1.entrySet()) {
                i = pp.getKey();
                j = pp1.getKey();
                mianownik += (i * pp.getValue()) - (j * pp1.getValue());
            }
        }
        return licznik / mianownik;
    }

    public Double computeComplexity() {
        Double i = 0.0;
        Double j = 0.0;
        Double part = 0.0;
        Double part2 = 0.0;
        Double suma = 0.0;
        Map<Double, Double> p1 = new HashMap<>(p);
        for (Map.Entry<Double, Double> pp : p.entrySet()) {
            for (Map.Entry<Double, Double> pp1 : p1.entrySet()) {
                i = pp.getKey();
                j = pp1.getKey();
                part = Math.abs(i - j);
                part /= n2 * (pp.getValue() + pp1.getValue());
                part2 = pp.getValue() * s.get(pp.getKey());
                part2 -= pp1.getValue() * s.get(pp1.getKey());
                part *= part2;
                suma += part;
            }
        }
        return suma;
    }

    public Double computeStrength() {
        Double i = 0.0;
        Double j = 0.0;
        Double part = 0.0;
        Double part2 = 0.0;
        Double suma = 0.0;
        Map<Double, Double> p1 = new HashMap<>(p);
        for (Map.Entry<Double, Double> pp : p.entrySet()) {
            for (Map.Entry<Double, Double> pp1 : p1.entrySet()) {
                i = pp.getKey();
                j = pp1.getKey();
                part = Math.pow((i - j), 2);
                part *= (pp.getValue() + pp1.getValue());
                suma += part;
            }
        }
        Double fcos = 0.001;
        for (Map.Entry<Double, Double> ss : s.entrySet()) {
            fcos += ss.getValue();
        }
        return suma / fcos;
    }

    public void computeCoarnessContrastBusynessComplexity() {
        Double Ng = new Double(s.size());
        Map<Double, Double> p1 = new HashMap<>(p);

        Double psiLicznikBusyness = 0.0;

        for (Map.Entry<Double, Double> ss : s.entrySet()) {
            psiLicznikBusyness += ss.getValue() * p.get(ss.getKey());
        }

        Double i = 0.0;
        Double j = 0.0;
        Double firstPartContrast = 0.0;
        Double mianownikBusyness = 0.0;
        Double partComplexity = 0.0;
        Double Complexity = 0.0;
        Double partStrength = 0.0;
        Double Strength = 0.0;
        for (Map.Entry<Double, Double> pp : p.entrySet()) {
            mianownikBusyness += i * pp.getValue();
            for (Map.Entry<Double, Double> pp1 : p1.entrySet()) {
                i = pp.getKey();
                j = pp1.getKey();
                firstPartContrast += pp.getValue() * pp1.getValue() * Math.pow((i - j), 2);
                mianownikBusyness -= j * pp1.getValue();
                partComplexity = Math.abs(i - j) / (n2 * (pp.getValue() + pp1.getValue()));
                partComplexity *= (pp.getValue() * s.get(i)) - (pp1.getValue() * s.get(j));
                Complexity += partComplexity;
                partStrength = Math.pow((i - j), 2) * (pp.getValue() + pp1.getValue());
                Strength += partStrength;

            }
        }
        firstPartContrast = 1 / Ng / (Ng - 1) * firstPartContrast;
        Double suma_si = 0.0;
        for (Map.Entry<Double, Double> ss : s.entrySet()) {
            suma_si += ss.getValue();
        }

        Double Coarness = Math.pow(psiLicznikBusyness, -1);
        Double Contrast = firstPartContrast * suma_si * 1 / n2;
        Double Busyness = psiLicznikBusyness / mianownikBusyness;
        Strength /= suma_si;


        System.out.println("\n\nCoarness:  " + Coarness);
        System.out.println("Contrast:  " + Contrast);
        System.out.println("Busyness:  " + Busyness);
        System.out.println("Complexity:  " + Complexity);
        System.out.println("Strength:  " + Strength);

    }

    public void computeCoarnessContrastBusynessComplexityParallel() {
        Double Ng = new Double(s.size());
        Map<Double, Double> p1 = new HashMap<>(p);

        Double psiLicznikBusyness = 0.0;

        psiLicznikBusyness = s.entrySet()
                            .parallelStream()
                            .mapToDouble((ss)->ss.getValue()*p.get(ss.getKey()))
                            .sum();

        Double i = 0.0;
        Double j = 0.0;
        Double firstPartContrast = 0.0;
        Double mianownikBusyness = 0.0;
        Double Complexity = 0.0;
        Double Strength = 0.0;
        for (Map.Entry<Double, Double> pp : p.entrySet()) {
            mianownikBusyness += i * pp.getValue();
            i = pp.getKey();
            firstPartContrast += p1.entrySet()
                    .parallelStream()
                    .mapToDouble( (pp1) -> pp.getValue() * pp1.getValue() * Math.pow((pp.getKey() - pp1.getKey()), 2))
                    .sum();

            mianownikBusyness -= p1.entrySet()
                    .parallelStream()
                    .mapToDouble( (pp1) ->  pp1.getKey() * pp1.getValue())
                    .sum();

            Complexity += p1.entrySet()
                    .parallelStream()
                    .mapToDouble((pp1) ->
                            (Math.abs(pp.getKey() - pp1.getKey()) / (n2 * (pp.getValue() + pp1.getValue())))
                                * ((pp.getValue() * s.get(pp.getKey())) - (pp1.getValue() * s.get(pp1.getKey())))
                    ).sum();

            Strength +=  p1.entrySet()
                    .parallelStream()
                    .mapToDouble( (pp1) ->  Math.pow((pp.getKey() - pp1.getKey()), 2) * (pp.getValue() + pp1.getValue()) )
                    .sum();
        }

        firstPartContrast = 1 / Ng / (Ng - 1) * firstPartContrast;
        Double suma_si = 0.0;
        for (Map.Entry<Double, Double> ss : s.entrySet()) {
            suma_si += ss.getValue();
        }

        Double Coarness = Math.pow(psiLicznikBusyness, -1);
        Double Contrast = firstPartContrast * suma_si * 1 / n2;
        Double Busyness = psiLicznikBusyness / mianownikBusyness;
        Strength /= suma_si;

        System.out.println("\n\n Parallel");
        System.out.println("Coarness:  " + Coarness);
        System.out.println("Contrast:  " + Contrast);
        System.out.println("Busyness:  " + Busyness);
        System.out.println("Complexity:  " + Complexity);
        System.out.println("Strength:  " + Strength);

    }


}
