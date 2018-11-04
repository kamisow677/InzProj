import java.util.*;

/**
 * @author Kamil Sowa
 * @version 1.0
 * Klasa obliczająca 5 parametrów metody
 *
 */
public class TexturalProperties {

    /**
     * obiekt klasy gtdm
     */
    private GTDM gtdm;
    /**
     * wektor gtdm
     */
    private ArrayList<Double> s;
    /**
     * wektor prawdopodobieństwa wystąpienia piksela w obrazie
     */
    private ArrayList<Double> p;
    /**
     * współczynnik n2
     */
    private Double n2;
    /**
     * parametr Coarness
     */
    private Double Coarness;
    /**
     * parametr Contrast
     */
    private Double Contrast;
    /**
     * parametr Busyness
     */
    private Double Busyness;
    /**
     * parametr Complexity
     */
    private Double Complexity;
    /**
     * parametr Strength
     */
    private Double Strength;
    /**
     * kolor obrazu
     */
    private String Color;
    /**
     * wartość epsilon zabezpiecza przed uzyskiwaniem 0 w mianownikach wzorów
     */
    private Double Epsilon = 0.0000001;
    /**
     * Mianownik parametru Busyness
     */
    private Double psiNumeratorBusyness;
    /**
     * częściowe obliczenia Contrastu
     */
    private Double partContrast;
    /**
     * częściowe obliczenia Strength
     */
    private Double partStrength;
    /**
     * częściowe obliczenia Complexity
     */
    private Double partComplexity;

    /**
     * Konstruktor
     * @param gtdm macierz różnic poziomów szarości
     */
    public TexturalProperties(GTDM gtdm)  {
        this.gtdm = gtdm;
        this.s = new ArrayList<>( gtdm.getS());
        this.p = new ArrayList<>( gtdm.getP());
        this.n2 = gtdm.getN2();
        this.Color = gtdm.getInputDataMatrix().getColor();

        calculateProperties();
    }

    /**
     * Konstruktor
     * @param coarness coarness
     * @param contrast contrast
     * @param busyness busyness
     * @param complexity complexity
     * @param strength strength
     * @param color kolor
     */
    public TexturalProperties(Double coarness, Double contrast, Double busyness, Double complexity, Double strength, String color)  {
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

    /**
     * Wyliczenie wszystkich parametrów metody
     */
    public void calculateProperties() {
        Double Ng = 0.0;
        for (int i = 0 ; i < p.size() ;i++){
            if (p.get(i)!=0.0){
                Ng ++;
            }
        }
        ArrayList<Double> p1 = new ArrayList<>(p);
        psiNumeratorBusyness = 0.0;
        partContrast = 0.0;
        Double denominatorBusyness = 0.0;
        partComplexity = 0.0;
        Complexity = 0.0;
        Strength = 0.0;
        partStrength = 0.0;


        for (int i = 0; i< p.size(); i++){
            if (p.get(i).compareTo(0.0)==0)
                continue;
            psiNumeratorBusyness += s.get(i)* p.get(i);
            for (int j = i; j< p1.size(); j++){
                if (p1.get(j).compareTo(0.0)==0)
                    continue;
                denominatorBusyness += 2*Math.abs( i * p.get(i)- j * p1.get(j));
                partContrast += 2*p.get(i) * p1.get(j) * Math.pow((i - j), 2);
                partComplexity += 2*(Math.abs(i - j) / (n2 * (p.get(i) + p1.get(j)))) * ((p.get(i) * s.get(i)) + (p1.get(j) * s.get(j)));
                partStrength += 2*Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
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
    }

    @Override
    public String toString(){
        return
        "\n\nImage Name: "+gtdm.getInputDataMatrix().getImageName()+
        "\nCOLOR:  " + Color +
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
