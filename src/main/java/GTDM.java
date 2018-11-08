import java.awt.image.DataBuffer;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kamil Sowa
 * @version 1.0
 * Klasa ta jest odpowiedzialna za obliczanie macierzy różnic szarości
 * Oprócz tego wylicza wektor prawdopodobieństwa występowania piksela w obrazie lub jego kwadratowym regionie
 *
 */
public class GTDM {
    /**
     * wielkość okna obliczeń.
     */
    private  int d = 1;
    /**
     * macierz z danymi o obrazie wejściowym
     */
    private Matrix inputDataMatrix;
    /**
     * nazwa obrazu
     */
    private  String imageName;
    /**
     * wysokość obrazu
     */
    private int height;
    /**
     * szerokość obrazu
     */
    private int width;
    /**
     * macierz średniej wartości piksel
     */
    private MatrixCommon matrixA;
    /**
     * wartości macierzy różnić poziomów szarości
     */
    private ArrayList<Double> s;
    /**
     *  wektor prawdopodobieństwa występowania piksela
     */
    private ArrayList<Double> p;
    /**
     *  wektor ilości pikseli o danej wartości
     */
    private ArrayList<Double> pRaw;
    /**
     *  wektor ilości pikseli o danej wartości z pierwszej kolumny
     */
    private ArrayList<Double> originRawP;
    /**
     * wartości macierzy różnić poziomów szarości 1 kolumny
     */
    private ArrayList<Double> originS;
    /**
     *  wektor prawdopodobieństwa występowania piksela 1 kolumny
     */
    private ArrayList<Double> originP;

    private double n2;

    public  int getD() {
        return d;
    }

    public  void setD(int d) {
        this.d = d;
    }

    public Matrix getInputDataMatrix() {
        return inputDataMatrix;
    }

    public void setInputDataMatrix(Matrix inputDataMatrix) {
        this.inputDataMatrix = inputDataMatrix;
    }

    public MatrixCommon getMatrixA() {
        return matrixA;
    }

    public void setMatrixA(MatrixCommon matrixA) {
        this.matrixA = matrixA;
    }

    public ArrayList<Double> getS() {
        return s;
    }

    public void setS(ArrayList<Double> s) {
        this.s = s;
    }

    public ArrayList<Double> getP() {
        return p;
    }

    public void setP(ArrayList<Double> p) {
        this.p = p;
    }

    public double getN2() {
        return n2;
    }

    public void setN2(double n2) {
        this.n2 = n2;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

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
     * @param previousGDTM poprednia macierz gtdm
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
     * Konstruktor dla gdtm będącego średnią poszczególnych trzech kolorów
     * @param gtdm1 gdtm koloru 1
     * @param gtdm2 gdtm koloru 2
     * @param gtdm3 gdtm koloru 3
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
     * Obliczanie średnią wartość piksela w macierzy A
     * @param k numer kolumny
     * @param l numer wiersza
     * @return zwraca średnią wartość piksela w oknie
     */
    public double calculateA(int k, int l) {
        double suma = 0;
        for (int m = -d; m <= d; m++) {
            for (int n = -d; n <= d; n++) {
                if (m == 0 && n == 0)
                    continue;
//                if (k + m < 0 || k + m >= height)
//                    continue;
//                if (l + n < 0 || l + n >= width)
//                    continue;
                suma += inputDataMatrix.get(k + m, l + n);
                int pla=2;
            }
        }
        return suma / (Math.pow(2 * d + 1, 2) - 1);
    }

    /**
     * Obliczanie macierzy A
     */
    public void calculateMatrixA() {
        for (int k = 0; k < height - 0; k++) {
            for (int l = 0; l < width - 0; l++) {
                matrixA.set(k, l, calculateA(k, l));
            }
        }
    }

    /**
     * Obliczanie gdtm oraz wektora prawdopodobieństwa dla całego obrazu lub regionu
     * @param calculateP jeśli ustawione na true to oblicza p
     * @param print jeśli true to wypisuje zawartość s i p
      */
    public void startFirstCalcualtions(Boolean calculateP, Boolean print){
        initializaS();
        calculateSNOWE();
        if (calculateP) {
            initializaP();
            computePNowe();
        }
        if (print) {
            System.out.println("Matrix A");
            matrixA.printf();
            printfS();
            printfP();
        }
        originRawP = new ArrayList<>(pRaw);
        originS = new ArrayList<>(s);
        originP = new ArrayList<>(p);
    }

    /**
     * Obliczanie gdtm oraz wektora prawdopodobieństwa dla następnej kolumny obrazu lub regionu
     * @param calculateP jeśli ustawione na true to oblicza p
     * @param print jeśli true to wypisuje zawartość s i p
     */
    public void startNextColumnCalcualtions(Boolean calculateP, Boolean print){
        try {
            if (Constans.slowGTDMcalc == true) {
                startFirstCalcualtions(calculateP, print);
            } else {
                calculateNextColumnSNowe();
                if (calculateP) {
                    computeNextColumnPNowe();
                }
                if (print) {
                    System.out.println("Matrix A");
                    matrixA.printf();
                    printfS();
                    printfP();
                }
            }
        }catch (NullPointerException ex){
            System.out.println("MAM");
        }
    }

    /**
     * Obliczanie gdtm oraz wektora prawdopodobieństwa dla następnego wiersza obrazu lub regionu
     * @param calculateP jeśli ustawione na true to oblicza p
     * @param print jeśli true to wypisuje zawartość s i p
     */
    public void startNextRowCalcualtions(Boolean calculateP, Boolean print){
        if (Constans.slowGTDMcalc==true) {
            startFirstCalcualtions(calculateP, print);
        }else {
            calculateNextRowSNowe();
            if (calculateP) {
                computeNextRowPNowe();
            }
            if (print) {
                System.out.println("Matrix A");
                matrixA.printf();
                printfS();
                printfP();
            }
            originRawP =  new ArrayList<>(pRaw);
            originS = new ArrayList<>(s);
            originP = new ArrayList<>(p);
        }
    }

    /**
     * Initcalizacja gdtm
     */
    private void initializaS() {
        s.clear();
        for (int i = 0; i<Constans.PIXEL_NUMBER_PLUS_1; i++) {
            s.add(0.0);
        }
    }


    /**
     * Obliczanie gdtm dla całego regionu lub obrazu
     */
    private void calculateS() {
        Double i=0.0;
        int startY = inputDataMatrix.getStartHeight();
        int startX = inputDataMatrix.getStartWidth();
        for (int k = d; k < height - d; k++) {
            for (int l = d; l < width - d; l++) {
                i = inputDataMatrix.get(k, l);
                Double partSum = s.get(i.intValue());
                if (partSum == null)
                    partSum = 0.0;
                partSum += Math.abs(i - matrixA.get(k + startY, l + startX));// |i-A|
                s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|
            }
        }
    }

    private void calculateSNOWE() {
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
     * Obliczanie gdtm dla regionu będącego kolejną kolumną
     */
    private void calculateNextColumnS() {
        Double i = 0.0;

        int startY = inputDataMatrix.getStartHeight();
        int startX = inputDataMatrix.getStartWidth();
        for (int k = d ; k < height - d ; k++) {
            i = inputDataMatrix.get(k,  d - 1);
            Double partSum = s.get(i.intValue());
            if (partSum == null)
                partSum = 0.0;
            partSum -= Math.abs(i - matrixA.get(k + startY, d - 1 + startX));// |i-A|
            s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|

        }

        for (int k = d ; k < height - d ; k++) {
            i = inputDataMatrix.get(k, - d + width - 1);
            Double partSum = s.get(i.intValue());
            if (partSum == null)
                partSum = 0.0;
            partSum += Math.abs(i - matrixA.get(k + startY, - d + width + startX - 1));// |i-A|
            s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|

        }
    }

    private void calculateNextColumnSNowe() {
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


    public void writeAllValuesOfImage(){

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(Constans.FOLDER_PATH + "\\" + nameFromPath(imageName) + inputDataMatrix.getColor() +  ".txt", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int k = 0; k < height ; k++) {
            for (int l = 0; l < width ; l++) {
                System.out.print(inputDataMatrix.get(k, l)+ " ");
//                System.out.print(matrixA.get(k,l)+" ");
               // writer.println(inputDataMatrix.get(k, l));
               // System.out.print(" ");
//                if (String.valueOf(inputDataMatrix.get(k, l)).equals("8.0"))
            }
            System.out.println();
        }
        writer.close();
    }

    /**
     * Obliczanie gdtm dla regionu będącego kolejnym wierszem
     */
    private void calculateNextRowS() {
        Double i = 0.0;
        int startY = inputDataMatrix.getStartHeight();
        int startX = inputDataMatrix.getStartWidth();
        for (int k = d ; k < width - d ; k++) {
            i = inputDataMatrix.get(d - 1,  k);
            Double partSum = s.get(i.intValue());
            if (partSum == null)
                partSum = 0.0;
            partSum -= Math.abs(i - matrixA.get(d - 1 + startY, k + startX ));// |i-A|
            s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|

        }

        for (int k = d ; k < width - d ; k++) {
            i = inputDataMatrix.get(- d + height - 1, k);
            Double partSum = s.get(i.intValue());
            if (partSum == null)
                partSum = 0.0;
            partSum += Math.abs(i - matrixA.get(- d + height - 1 + startY, k + startX));// |i-A|
            s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|

        }
    }

    private void calculateNextRowSNowe() {
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
     * Obliczanie gdtm dla całego regionu lub obrazu na podstawie trzech innych gdtm jako średnia
     */
    private void calculateS(ArrayList<Double> s1, ArrayList<Double> s2, ArrayList<Double> s3) {
        for (int i = 0; i < Constans.PIXEL_NUMBER ; i++){
            s.set(i, (s1.get(i)+s2.get(i)+s3.get(i))/3.0);//s(i)= SIGMA |i-A|
        }
    }

    /**
     * Wypisanie gdtm na ekran
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
    public void computeP() {
        for (int k = d; k < height - d; k++) {
            for (int l = d; l < width - d; l++) {
                Double iNumber = p.get((int) inputDataMatrix.get(k, l));//i
                if (iNumber == null)
                    iNumber = 0.0;
                iNumber += 1;
                p.set((int) inputDataMatrix.get(k, l), iNumber);
            }//((int) inputDataMatrix.get(k, l))==83 && k>2
        }
        pRaw = new ArrayList<>(p);
        originRawP = new ArrayList<>(p);
        for (int i = 0 ; i< Constans.PIXEL_NUMBER_PLUS_1 ; i++) {
            p.set( i ,p.get(i) / n2);
        }
    }

    public void computePNowe() {
        for (int k = d - Constans.QUADRATIC_SIZE/2; k < height - d - Constans.QUADRATIC_SIZE/2 ; k++) {
            for (int l = d - Constans.QUADRATIC_SIZE/2; l < width - d - Constans.QUADRATIC_SIZE/2 ; l++) {
                Double iNumber = p.get((int) inputDataMatrix.get(k, l));//i
                if (iNumber == null)
                    iNumber = 0.0;
                iNumber += 1;
                p.set((int) inputDataMatrix.get(k, l), iNumber);
            }//((int) inputDataMatrix.get(k, l))==83 && k>2
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
    public void computeNextColumnP() {
        for (int k = d ; k < height - d ; k++) {
            Double iNumber = pRaw.get((int) inputDataMatrix.get(k, d -1));//i
            if (iNumber == null)
                iNumber = 0.0;
            iNumber -= 1;
            pRaw.set((int) inputDataMatrix.get(k, d - 1), iNumber);
        }

        for (int k = d ; k < height - d ; k++) {
            Double iNumber = pRaw.get((int) inputDataMatrix.get(k, width - d - 1));//i
            if (iNumber == null)
                iNumber = 0.0;
            iNumber += 1;
            pRaw.set((int) inputDataMatrix.get(k, width - d - 1), iNumber);
        }
        for (int i = 0 ; i< Constans.PIXEL_NUMBER_PLUS_1 ; i++) {
            p.set( i ,pRaw.get(i) / n2);
        }
    }

    public void computeNextColumnPNowe() {
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
    public void computeNextRowP() {
        for (int k = d ; k < width - d ; k++) {
            Double iNumber = pRaw.get((int) inputDataMatrix.get(d - 1 , k ));//i
            if (iNumber == null)
                iNumber = 0.0;
            iNumber -= 1;
            pRaw.set((int) inputDataMatrix.get(d - 1, k), iNumber);
        }

        for (int k = d ; k < width - d ; k++) {
            Double iNumber = pRaw.get((int) inputDataMatrix.get(height - d - 1, k));//i
            if (iNumber == null)
                iNumber = 0.0;
            iNumber += 1;
            pRaw.set((int) inputDataMatrix.get(height - d - 1, k), iNumber);
        }
        for (int i = 0 ; i< Constans.PIXEL_NUMBER_PLUS_1 ; i++) {
            p.set( i ,pRaw.get(i) / n2);
        }
    }

    public void computeNextRowPNowe() {
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
     */
    private void computeP(ArrayList<Double> p1, ArrayList<Double> p2, ArrayList<Double> p3) {
        int i=0;
        for (i = 0; i <  Constans.PIXEL_NUMBER ; i++){
            p.set(i, (p1.get(i)+p2.get(i)+p3.get(i))/3.0);//s(i)= SIGMA |i-A|
        }
    }

    /**
     * Zapisanie gdtm do pliku .csv
     */
    public  void saveToCSV(String part) {
        PrintWriter w = null;
        File fileForCsv = new File(Constans.SAVE_PATH + nameFromPath(inputDataMatrix.getImageName()) + "CsvFiles\\");
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
     * Zapisanie gdtm do pliku .csv
     */
    static String nameFromPath(String path){
        return path.substring(path.lastIndexOf("\\"));
    }
}
