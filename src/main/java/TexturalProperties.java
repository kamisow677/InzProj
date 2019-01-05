import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 *
 * Klasa obliczająca 5 parametrów metody
 * @author Kamil Sowa
 * @version 1.0
 *
 */
public class TexturalProperties {

    /**
     * Obiekt klasy GTDM
     */
    private GTDM gtdm;
    /**
     * Wektor GTDM
     */
    private ArrayList<Double> s;
    /**
     * Wektor prawdopodobieństwa wystąpienia piksela w obrazie
     */
    private ArrayList<Double> p;
    /**
     * Współczynnik n2, pole obrazu lub kwadratowego regionu bez obszaru peryferyjnego
     */
    private Double n2;
    /**
     * Parametr szorstkość
     */
    private Double coarness;
    /**
     * Parametr kontrast
     */
    private Double contrast;
    /**
     * Parametr zajętość
     */
    private Double busyness;
    /**
     * Parametr złożoność
     */
    private Double complexity;
    /**
     * Parametr siła
     */
    private Double strength;
    /**
     * Lolor obrazu
     */
    private String color;
    /**
     * Wartość epsilon zabezpiecza przed uzyskiwaniem 0 w mianownikach wzorów
     */
    private Double epsilon = 0.0000001;
    /**
     * Mianownik parametru zajętości
     */
    private Double psiNumeratorBusyness;
    /**
     * Częściowe obliczenia kontrastu
     */
    private Double partContrast;
    /**
     * Częściowe obliczenia siły
     */
    private Double partStrength;
    /**
     * Częściowe obliczenia złożoności
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
        this.color = gtdm.getInputDataMatrix().getColor();

        calculateProperties();
    }

    /**
     * Konstruktor
     * @param coarness szorstkość
     * @param contrast kontrast
     * @param busyness zajętość
     * @param complexity złożoność
     * @param strength siła
     * @param color kolor
     */
    public TexturalProperties(Double coarness, Double contrast, Double busyness, Double complexity, Double strength, String color)  {
        this.coarness = coarness;
        this.contrast = contrast;
        this.busyness = busyness;
        this.complexity = complexity;
        this.strength = strength;
        this.color = color;
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
        complexity = 0.0;
        strength = 0.0;
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
                partComplexity += (2*(Math.abs(i - j)) / ((n2 * (p.get(i) + p1.get(j))) +0.000000) ) * ((p.get(i) * s.get(i)) + (p1.get(j) * s.get(j)));
                partStrength += 2*Math.pow((i - j), 2) * (p.get(i) + p1.get(j));
            }
        }

        complexity = partComplexity;
        contrast = 1 / Ng / (Ng - 1) * partContrast;
        Double siSum = s
                .stream()
                .mapToDouble(s -> s)
                .sum();

        coarness = Math.pow(epsilon + psiNumeratorBusyness, -1);
        contrast = contrast * siSum * 1 / n2;
        busyness = psiNumeratorBusyness / (denominatorBusyness + epsilon);
        strength = partStrength / siSum;
    }

    /**
     * Opis obiektu klasy
     * @return Lańcuch z opisem obiektu klasy
     */
    @Override
    public String toString(){
        String str = "\n";
        if (gtdm!=null)
            str = "\n\nImage Name: "+ gtdm.getInputDataMatrix().getImageName();
        str +="\nCOLOR:  " + color +
            "\ncoarness:  " + coarness +
            "\ncontrast:  " + contrast +
            "\nbusyness:  " + busyness +
            "\ncomplexity:  " + complexity +
            "\nstrength:  " + strength;
        return  str;
    }
    /**
     * Akcesor zmiennej coarness
     * @return zmienna coarness
     */
    public Double getCoarness() {
        return coarness;
    }
    /**
     * Akcesor zmiennej contrast
     * @return zmienna contrast
     */
    public Double getContrast() {
        return contrast;
    }
    /**
     * Akcesor zmiennej busyness
     * @return zmienna busyness
     */
    public Double getBusyness() {
        return busyness;
    }
    /**
     * Akcesor zmiennej complexity
     * @return zmienna complexity
     */
    public Double getComplexity() {
        return complexity;
    }
    /**
     * Akcesor zmiennej strength
     * @return zmiennej strength
     */
    public Double getStrength() {
        return strength;
    }
    /**
     * Akcesor cech dla danego kwadratowego regionu lub obrazu
     * @return mape gdzie klucze to nazwy cech, a wartości to wartości danej cechy
     */
    public Map<String,Double> getProps(){
        Map map = new HashMap<String,Double>();
        map.put(Constans.COARNESS, coarness);
        map.put(Constans.CONTRAST, contrast);
        map.put(Constans.BUSYNESS,Math.abs(busyness));
        map.put(Constans.COMPLEXITY, complexity);
        map.put(Constans.STRENGTH, strength);
        return  map;
    }
    /**
     * Zapisanie wartości cech tekstury do pliku txt
     */
    public  void saveToTXT() {
        PrintWriter w = null;
        File fileForCsv = new File(Constans.SAVE_PATH + ImagesCreator.nameFromPath(gtdm.getInputDataMatrix().getImageName()) + "Files\\");
        try {
            w = new PrintWriter(fileForCsv.getAbsolutePath()+ "\\" + color + "Properties.txt", "UTF-8");
            w.println( "Nazwa obrazu: "+ gtdm.getInputDataMatrix().getImageName());
            w.println("Szorstkosc:  " + coarness );
            w.println( "Kontrast:  " + contrast );
            w.println( "Zajetosc:  " + busyness );
            w.println( "Zlozonosc:  " + complexity );
            w.println("Sila:  " + strength);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        w.close();
    }
}
