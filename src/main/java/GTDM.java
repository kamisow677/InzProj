import java.io.*;
import java.util.ArrayList;

/**
 * Klasa ta jest odpowiedzialna za obliczanie macierzy różnic szarości
 * Oprócz tego wylicza wektor prawdopodobieństwa występowania piksela w obrazie lub jego kwadratowym regionie
 * @author Kamil Sowa
 * @version 1.0
 */
public class GTDM {
    /**
     * Wielkość okna obliczeń, szerokość okna obliczeń
     */
    private  int d = 1;
    /**
     * Macierz z danymi o obrazie wejściowym
     */
    private Matrix inputDataMatrix;
    /**
     * Nazwa obrazu
     */
    private  String imageName;
    /**
     * Wysokość obrazu
     */
    private int height;
    /**
     * Szerokość obrazu
     */
    private int width;
    /**
     * Macierz średniej wartości pikseli
     */
    private MatrixCommon matrixA;
    /**
     * Wartości macierzy różnic poziomów szarości
     */
    private ArrayList<Double> s;
    /**
     *  Wektor prawdopodobieństwa występowania piksela
     */
    private ArrayList<Double> p;
    /**
     *  Wektor ilości pikseli o danej wartości
     */
    private ArrayList<Double> pRaw;
    /**
     *  Wektor ilości pikseli o danej wartości z pierwszej kolumny
     */
    private ArrayList<Double> originRawP;
    /**
     * Wartości macierzy różnić poziomów szarości jednej kolumny
     */
    private ArrayList<Double> originS;
    /**
     * Wektor prawdopodobieństwa występowania piksela jednej kolumny
     */
    private ArrayList<Double> originP;
    /**
     * Pole obrazu bez regionu peryferyjnego
     */
    private double n2;
    /**
     * Akcesor zmiennej d
     * @return zmienna d
     */
    public  int getD() {
        return d;
    }
    /**
     * Mutator zmiennej d
     * @param d zmienna szerokość regionu peryferyjnego
     */
    public  void setD(int d) {
        this.d = d;
    }
    /**
     * Akcesor zmiennej inputDataMatrix
     * @return zmienna inputDataMatrix
     */
    public Matrix getInputDataMatrix() { return inputDataMatrix;}
    /**
     * Akcesor zmiennej matrixA
     * @return zmienna matrixA
     */
    public MatrixCommon getMatrixA() {return matrixA; }
    /**
     * Akcesor zmiennej s
     * @return zmienna s
     */
    public ArrayList<Double> getS() { return s; }
    /**
     * Mutator zmiennej s
     * @param s macierz różnic poziomów szarości
     */
    public void setS(ArrayList<Double> s) {this.s = s; }
    /**
     * Akcesor zmiennej p
     * @return zmienna p
     */
    public ArrayList<Double> getP() { return p;}
    /**
     * Mutator zmiennej p
     * @param p wektor prawd. wystąpienia piksela
     */
    public void setP(ArrayList<Double> p) {this.p = p; }
    /**
     * Akcesor zmiennej n2
     * @return zmienna n2
     */
    public double getN2() {return n2; }

    public ArrayList<Double> getpRaw() { return pRaw; }

    public ArrayList<Double> getOriginRawP() { return originRawP; }

    public ArrayList<Double> getOriginS() { return originS; }

    public ArrayList<Double> getOriginP() {   return originP;  }

    /**
     * Konstruktor, najprostszy.
     * @param inputData macierz z danymi o obrazie
     */
    public GTDM(Matrix inputData){
        this.matrixA = new MatrixCommon(inputData.getHeight(), inputData.getWidth());
        this.s = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        this.p = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        this.imageName = inputData.getImageName();
        this.pRaw = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        height = inputData.getHeight();
        width = inputData.getWidth();

        if (inputDataMatrix instanceof  ImageMatrix)
            this.inputDataMatrix =  new ImageMatrix((ImageMatrix) inputData);
        else
            this.inputDataMatrix = inputData;
        n2 = (double) (height - 2 * d) * (width - 2 * d);
        calculateMatrixA();

    }

    /**
     * Konstruktor z macierzą średniej wartości pikseli
     * @param inputData macierz z danymi o obrazie
     * @param matrixA  macierz średniej wartości pikseli
     */
    public GTDM(Matrix inputData, MatrixCommon matrixA){
        this.matrixA = matrixA;
        this.s = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        this.p = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        this.pRaw = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        this.imageName = inputData.getImageName();
        height = inputData.getHeight();
        width = inputData.getWidth();

        if (inputDataMatrix instanceof  ImageMatrix)
            this.inputDataMatrix =  new ImageMatrix((ImageMatrix) inputData);
        else
            this.inputDataMatrix = inputData;
        n2 = (double) (height - 2 * d) * (width - 2 * d);

    }

    /**
     * Konstruktor dla kolejnej macierzy poziomów szarości
     * @param previousGDTM poprednia macierz GTDM
     * @param GoingRight jeżeli ustawione na true to wyliczane dla kolejnej kolumny, jeśli na false do dla kolejnego wiersza
     */
    public GTDM(GTDM previousGDTM, boolean GoingRight){
        this.d = previousGDTM.d;
        if (inputDataMatrix instanceof  ImageMatrix)
            this.inputDataMatrix =  new ImageMatrix((ImageMatrix) previousGDTM.getInputDataMatrix());
        else
            this.inputDataMatrix = previousGDTM.getInputDataMatrix();
        this.matrixA = previousGDTM.getMatrixA();

        if (GoingRight){
            inputDataMatrix.setStartWidth( inputDataMatrix.getStartWidth() + 1);
            this.s = previousGDTM.getS();
            this.p = previousGDTM.getP();
            this.pRaw = previousGDTM.getpRaw();
            this.originRawP = previousGDTM.getOriginRawP();
            this.originS = previousGDTM.getOriginS();
            this.originP = previousGDTM.getOriginP();
        } else{
            inputDataMatrix.setStartWidth(0);
            inputDataMatrix.setStartHeight( inputDataMatrix.getStartHeight() + 1);
            this.s = previousGDTM.getOriginS();
            this.p = previousGDTM.getP();
            this.pRaw = previousGDTM.getOriginRawP();
        }

        this.height = inputDataMatrix.getHeight();
        this.width = inputDataMatrix.getWidth();
        n2 = (double) (height - 2 * d) * (width - 2 * d);

    }

    /**
     * Konstruktor dla GTDM będącego średnią z poszczególnych trzech kolorów RGB
     * @param gtdm1 GTDM koloru 1
     * @param gtdm2 GTDM koloru 2
     * @param gtdm3 GTDM koloru 3
     */
    public GTDM(GTDM gtdm1, GTDM gtdm2, GTDM gtdm3){
        this.matrixA = gtdm1.getMatrixA();
        this.d = gtdm1.d;
        this.s = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        this.p = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        height = gtdm1.getInputDataMatrix().getHeight();
        width = gtdm2.getInputDataMatrix().getWidth();
        this.imageName = gtdm1.imageName;

        if (inputDataMatrix instanceof  ImageMatrix)
            this.inputDataMatrix =  new ImageMatrix((ImageMatrix) gtdm1.getInputDataMatrix());
        else
            this.inputDataMatrix = gtdm1.getInputDataMatrix();
        n2 = (double) (height - 2 * d) * (width - 2 * d);

        initializaS();
        calculateS(gtdm1.getS(),gtdm2.getS(),gtdm3.getS());

        initializaP();
        computeP(gtdm1.getP(),gtdm2.getP(),gtdm3.getP());
    }

    /**
     * Obliczanie średniej wartość piksela w macierzy A
     * @param k numer kolumny
     * @param l numer wiersza
     * @return zwraca średnią wartość piksela w oknie
     */
    private double calculateA(int k, int l) {
        double suma = 0;
        for (int m = -d; m <= d; m++) {
            for (int n = -d; n <= d; n++) {
                if (m == 0 && n == 0)
                    continue;
                suma += inputDataMatrix.get(k + m, l + n);
            }
        }
        return suma / (Math.pow(2 * d + 1, 2) - 1);
    }

    /**
     * Obliczanie macierzy A
     */
    private void calculateMatrixA() {
        for (int k = 0; k < height - 0; k++) {
            for (int l = 0; l < width - 0; l++) {
                matrixA.set(k, l, calculateA(k, l));
            }
        }
    }

    /**
     * Obliczanie GTDM oraz wektora prawdopodobieństwa dla całego obrazu lub regionu
      */
    public void startFirstCalcualtions(){
        initializaS();
        calculateSN();
        initializaP();
        computePN();

        originRawP = new ArrayList<>(pRaw);
        originS = new ArrayList<>(s);
        originP = new ArrayList<>(p);
    }

    /**
     * Obliczanie GTDM oraz wektora prawdopodobieństwa dla następnej kolumny obrazu lub regionu
     */
    public void startNextColumnCalcualtions(){
        try {
            if (Constans.slowGTDMcalc == true) {
                startFirstCalcualtions();
            } else {
                calculateNextColumnSN();
                computeNextColumnPN();
            }
        }catch (NullPointerException ex){
            System.out.println("MAM");
        }
    }

    /**
     * Obliczanie GTDM oraz wektora prawdopodobieństwa dla następnego wiersza obrazu lub regionu
     */
    public void startNextRowCalcualtions(){
        if (Constans.slowGTDMcalc==true) {
            startFirstCalcualtions();
        }else {
            calculateNextRowSN();
                computeNextRowPN();
            originRawP =  new ArrayList<>(pRaw);
            originS = new ArrayList<>(s);
            originP = new ArrayList<>(p);
        }
    }

    /**
     * Inicjalizacja GTDM
     */
    private void initializaS() {
        s.clear();
        for (int i = 0; i<Constans.PIXEL_NUMBER_PLUS_1; i++) {
            s.add(0.0);
        }
    }

    /**
     * Obliczanie GTDM dla całego regionu lub obrazu
     */
    private void calculateSN() {
        Double i=0.0;
        int startY = inputDataMatrix.getStartHeight();
        int startX = inputDataMatrix.getStartWidth();
        for (int k = d - Constans.QUADRATIC_SIZE/2; k < height - d - Constans.QUADRATIC_SIZE/2 ; k++) {
            for (int l = d - Constans.QUADRATIC_SIZE/2; l < width - d - Constans.QUADRATIC_SIZE/2 ; l++) {
                i = inputDataMatrix.get(k, l);
                if (i==null)
                    continue;
                Double partSum = s.get(i.intValue());
                if (partSum == null)
                    partSum = 0.0;
                partSum += Math.abs(i - matrixA.get(k + startY, l + startX));// |i-A|
                s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|
            }
        }
    }

    /**
     * Obliczanie GTDM dla regionu będącego kolejną kolumną
     */
    private void calculateNextColumnSN() {
        Double i = 0.0;

        int startY = inputDataMatrix.getStartHeight();
        int startX = inputDataMatrix.getStartWidth();
        for (int k = d - Constans.QUADRATIC_SIZE/2 ; k < height - d - Constans.QUADRATIC_SIZE/2 ; k++) {
            i = inputDataMatrix.get(k,  d - 1 - Constans.QUADRATIC_SIZE/2);
            Double partSum = s.get(i.intValue());
            if (partSum == null)
                partSum = 0.0;
            partSum -= Math.abs(i - matrixA.get(k + startY, d - 1 - Constans.QUADRATIC_SIZE/2 + startX));// |i-A|
            s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|
        }

        for (int k = d -Constans.QUADRATIC_SIZE/2; k < height - d - Constans.QUADRATIC_SIZE/2 ; k++) {
            i = inputDataMatrix.get(k, - d  + Constans.QUADRATIC_SIZE/2);
            Double partSum = s.get(i.intValue());
            if (partSum == null)
                partSum = 0.0;
            partSum += Math.abs(i - matrixA.get(k + startY, - d +  startX + Constans.QUADRATIC_SIZE/2));// |i-A|
            s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|
        }
    }



    /**
     * Obliczanie GTDM dla regionu będącego kolejnym wierszem
     */
    private void calculateNextRowSN() {
        Double i = 0.0;
        int startY = inputDataMatrix.getStartHeight();
        int startX = inputDataMatrix.getStartWidth();
        for (int k = d - Constans.QUADRATIC_SIZE/2; k < width - d - Constans.QUADRATIC_SIZE/2 ; k++) {
            i = inputDataMatrix.get(d - 1 - Constans.QUADRATIC_SIZE/2,  k);
            Double partSum = s.get(i.intValue());
            if (partSum == null)
                partSum = 0.0;
            partSum -= Math.abs(i - matrixA.get(d - 1 - Constans.QUADRATIC_SIZE/2 + startY, k + startX ));// |i-A|
            s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|

        }

        for (int k = d - Constans.QUADRATIC_SIZE/2; k < width - d - Constans.QUADRATIC_SIZE/2 ; k++) {
            i = inputDataMatrix.get(- d  + Constans.QUADRATIC_SIZE/2, k);
            Double partSum = s.get(i.intValue());
            if (partSum == null)
                partSum = 0.0;
            partSum += Math.abs(i - matrixA.get(- d + Constans.QUADRATIC_SIZE/2 + startY, k + startX));// |i-A|
            s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|

        }
    }

    /**
     * Obliczanie GTDM dla całego regionu lub obrazu na podstawie trzech innych gdtm jako średnia
     * @param s1 GTDM pierwszego koloru
     * @param s2 GTDM drugiego koloru
     * @param s3 GTDM trzeciego koloru
     */
    private void calculateS(ArrayList<Double> s1, ArrayList<Double> s2, ArrayList<Double> s3) {
        for (int i = 0; i < Constans.PIXEL_NUMBER ; i++){
            s.set(i, (s1.get(i)+s2.get(i)+s3.get(i))/3.0);
        }
    }

    /**
     * Wypisanie GTDM na ekran
     */
    public void printfS() {
        System.out.println("S(i)");
        for (int i = 0; i<s.size(); i++) {
            System.out.println( i +":  " + s.get(i));
        }
        System.out.println();
    }

    /**
     * Inicjalizacja wektora prawdopodobieństwa
     */
    private void initializaP() {
        p.clear();
        for (int i = 0; i<Constans.PIXEL_NUMBER_PLUS_1; i++) {
            p.add(0.0);
        }
    }
    /**
     * Wypisanie wektora prawdopodobieństwa na ekran
     */
    public void printfP() {
        System.out.println("Tablica z p");
        for (int i = 0; i<p.size(); i++) {
            System.out.println( i +":  " + p.get(i));
        }
        System.out.println("Suma: "+ p.stream().mapToDouble(x->x).sum());
    }

    /**
     * Obliczanie wektora prawdopodobieństwa
     */
    public void computePN() {
        for (int k = d - Constans.QUADRATIC_SIZE/2; k < height - d - Constans.QUADRATIC_SIZE/2 ; k++) {
            for (int l = d - Constans.QUADRATIC_SIZE/2; l < width - d - Constans.QUADRATIC_SIZE/2 ; l++) {
                Double iNumber = p.get((int) inputDataMatrix.get(k, l));//i
                if (iNumber == null)
                    iNumber = 0.0;
                iNumber += 1;
                p.set((int) inputDataMatrix.get(k, l), iNumber);
            }
        }
        pRaw = new ArrayList<>(p);
        originRawP = new ArrayList<>(p);
        for (int i = 0 ; i< Constans.PIXEL_NUMBER_PLUS_1 ; i++) {
            p.set( i ,p.get(i) / n2);
        }
    }


    /**
     * Obliczanie wektora prawdopodobieństwa dla regionu będącego w kolejnej kolumnie
     */
    public void computeNextColumnPN() {
        for (int k = d - Constans.QUADRATIC_SIZE/2; k < height - d - Constans.QUADRATIC_SIZE/2 ; k++) {
            Double iNumber = pRaw.get((int) inputDataMatrix.get(k, d - 1 - Constans.QUADRATIC_SIZE/2));//i
            if (iNumber == null)
                iNumber = 0.0;
            iNumber -= 1;
            pRaw.set((int) inputDataMatrix.get(k, d - 1 - Constans.QUADRATIC_SIZE/2), iNumber);
        }

        for (int k = d - Constans.QUADRATIC_SIZE/2; k < height - d - Constans.QUADRATIC_SIZE/2 ; k++) {
            Double iNumber = pRaw.get((int) inputDataMatrix.get(k,  - d + Constans.QUADRATIC_SIZE/2));//i
            if (iNumber == null)
                iNumber = 0.0;
            iNumber += 1;
            pRaw.set((int) inputDataMatrix.get(k,  - d + Constans.QUADRATIC_SIZE/2), iNumber);
        }
        for (int i = 0 ; i< Constans.PIXEL_NUMBER_PLUS_1 ; i++) {
            p.set( i ,pRaw.get(i) / n2);
        }
    }



    /**
     * Obliczanie wektora prawdopodobieństwa dla regionu będącego w kolejnym wierszu
     */
    public void computeNextRowPN() {
        for (int k = d - Constans.QUADRATIC_SIZE/2; k < width - d - Constans.QUADRATIC_SIZE/2 ; k++) {
            Double iNumber = pRaw.get((int) inputDataMatrix.get(d - 1 - Constans.QUADRATIC_SIZE/2 , k ));//i
            if (iNumber == null)
                iNumber = 0.0;
            iNumber -= 1;
            pRaw.set((int) inputDataMatrix.get(d - 1 - Constans.QUADRATIC_SIZE/2, k), iNumber);
        }

        for (int k = d - Constans.QUADRATIC_SIZE/2; k < width - d - Constans.QUADRATIC_SIZE/2 ; k++) {
            Double iNumber = pRaw.get((int) inputDataMatrix.get( - d + Constans.QUADRATIC_SIZE/2, k));//i
            if (iNumber == null)
                iNumber = 0.0;
            iNumber += 1;
            pRaw.set((int) inputDataMatrix.get(- d + Constans.QUADRATIC_SIZE/2, k), iNumber);
        }

        for (int i = 0 ; i< Constans.PIXEL_NUMBER_PLUS_1 ; i++) {
            p.set( i ,pRaw.get(i) / n2);
        }
    }


    /**
     * Obliczanie wektora prawdopodobieństwa na podstwie trzech innych
     * @param p1 wektor prawdopodobieństwa wystąpienia piksela koloru pierwszego
     * @param p2 wektor prawdopodobieństwa wystąpienia piksela koloru drugiego
     * @param p3 wektor prawdopodobieństwa wystąpienia piksela koloru trzeciego
     */
    private void computeP(ArrayList<Double> p1, ArrayList<Double> p2, ArrayList<Double> p3) {
        int i=0;
        for (i = 0; i <  Constans.PIXEL_NUMBER ; i++){
            p.set(i, (p1.get(i)+p2.get(i)+p3.get(i))/3.0);
        }
    }

    /**
     * Zapisanie GTDM do pliku .csv
     * @param part dodatkowy opis pliku
     */
    public  void saveToCSV(String part) {
        PrintWriter w = null;
        File fileForCsv = new File(Constans.SAVE_PATH + nameFromPath(inputDataMatrix.getImageName()) + "Files\\");
        boolean success = (fileForCsv).mkdirs();
        try {
            w = new PrintWriter(fileForCsv.getAbsolutePath()+ "\\" + inputDataMatrix.getColor() + part + ".csv", "UTF-8");
            Integer i = 0;
            for (Double val : s) {
                w.write(i.toString());
                w.write(";");
                w.write(val.toString());
                i++;
                w.write("\n");
            }
            w.write("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        w.close();
    }

    /**
     * Zwraca nazwę obrazu wraz z rozszerzeniem z pełnej ścieżki
     * @param path ścieżka
     * @return nazwa pliku
     */
    static String nameFromPath(String path){
        return path.substring(path.lastIndexOf("\\"));
    }
}
