import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GTDMkopiaZapasowa {
    /**
     * neighbourhood size
     */
    private  int d = 1;
    private Matrix inputDataMatrix;
    private  String imageName;
    private int height;
    private int width;
    /**
     * average grey tone matrix
     */
    private MatrixCommon matrixA;
    /**
     * value of grey tone difference matrix
     */
    private ArrayList<Double> s;
    /**
     * probabilty of occurence of value
     */
    private ArrayList<Double> p;
    private ArrayList<Double> pRaw;
    private ArrayList<Double> originRawP;
    private ArrayList<Double> originS;
    private ArrayList<Double> originP;
    private Set<Double> changedPixels;

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

    public Set<Double> getChangedPixels() { return changedPixels; }

    public ArrayList<Double> getOriginP() {   return originP;  }

    /**
     * Just for matrix A
     * @param inputData
     */
    public GTDMkopiaZapasowa(Matrix inputData){
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

        //System.out.println("obliczylem A");
    }

    /**
     * For first calculations
     * @param inputData
     * @param matrixA
     */
    public GTDMkopiaZapasowa(Matrix inputData, MatrixCommon matrixA){
        this.matrixA = matrixA;
        this.s = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        this.p = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        this.pRaw = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        this.changedPixels = new HashSet<>(Constans.PIXEL_NUMBER_PLUS_1);
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
     * For next calculations in row and column
     * @param previousGDTM
     * @param GoingRight
     */
    public GTDMkopiaZapasowa(GTDMkopiaZapasowa previousGDTM, boolean GoingRight){
        this.d = previousGDTM.d;
        if (inputDataMatrix instanceof  ImageMatrix)
            this.inputDataMatrix =  new ImageMatrix((ImageMatrix) previousGDTM.getInputDataMatrix());
        else
            this.inputDataMatrix = previousGDTM.getInputDataMatrix();
        this.changedPixels = new HashSet<>(Constans.PIXEL_NUMBER_PLUS_1);
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
    public GTDMkopiaZapasowa(GTDMkopiaZapasowa gdtm){
        this.d = gdtm.d;
        if (inputDataMatrix instanceof  ImageMatrix)
            this.inputDataMatrix =  new ImageMatrix((ImageMatrix) gdtm.inputDataMatrix);
        else
            this.inputDataMatrix = gdtm.inputDataMatrix;
        imageName = gdtm.imageName;
        height = gdtm.height;
        width = gdtm.width;
        matrixA =gdtm.getMatrixA();
        s = new ArrayList<>(gdtm.s);
        p = new ArrayList<>(gdtm.p);
        pRaw = new ArrayList<>(gdtm.pRaw);
        originRawP = new ArrayList<>(gdtm.originRawP);
        originS = new ArrayList<>(gdtm.originS);
        originP = new ArrayList<>(gdtm.originP);
        changedPixels = new HashSet<>(gdtm.changedPixels);
    }


    public GTDMkopiaZapasowa(GTDMkopiaZapasowa matrix1, GTDMkopiaZapasowa matrix2, GTDMkopiaZapasowa matrix3){
        this.matrixA = matrix1.getMatrixA();
        this.d = matrix1.d;
        this.s = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        this.p = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        height = matrix1.getInputDataMatrix().getHeight();
        width = matrix2.getInputDataMatrix().getWidth();
        this.imageName = matrix1.imageName;

        //this.inputDataMatrix = matrix1.getInputDataMatrix();
        if (inputDataMatrix instanceof  ImageMatrix)
            this.inputDataMatrix =  new ImageMatrix((ImageMatrix) matrix1.getInputDataMatrix());
        else
            this.inputDataMatrix = matrix1.getInputDataMatrix();
        n2 = (double) (height - 2 * d) * (width - 2 * d);
        //inputDataMatrix.printf();

        initializaS();
        calculateS(matrix1.getS(),matrix2.getS(),matrix3.getS());
        //  printfS();

        initializaP();
        //computeP(matrix1.getInputDataMatrix(),matrix2.getInputDataMatrix(),matrix3.getInputDataMatrix());
        computeP(matrix1.getP(),matrix2.getP(),matrix3.getP());
        //  printfP();
    }

    public GTDMkopiaZapasowa(ArrayList<GTDMkopiaZapasowa> matrixes, int heigth , int width) {
        this.s = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        this.p = new ArrayList<Double>(Constans.PIXEL_NUMBER_PLUS_1);
        this.d = matrixes.get(0).getD();
        height = heigth;
        this.width = width;
        this.imageName = imageName;

        this.inputDataMatrix = matrixes.get(0).getInputDataMatrix();
        n2 = (double) (height - 2 * d) * (this.width - 2 * d);

        initializaS();
        calculateS(matrixes);

        initializaP();
        computeP();
    }


    /**
     * average gray-tone over a neighborhood centered at, but excluding ( k , I )
     */
    public double calculateA(int k, int l) {
        double suma = 0;
        for (int m = -d; m <= d; m++) {
            for (int n = -d; n <= d; n++) {
                if (m == 0 && n == 0)
                    continue;
                if (k + m < 0 || k + m >= height)
                    continue;
                if (l + n < 0 || l + n >= width)
                    continue;
                suma += inputDataMatrix.get(k + m, l + n);
            }
        }
        return suma / (Math.pow(2 * d + 1, 2) - 1);
    }

    public void calculateMatrixA() {
        for (int k = d; k < height - d; k++) {
            for (int l = d; l < width - d; l++) {
                matrixA.set(k, l, calculateA(k, l));
            }
        }
    }

    /**
     * Needs calculations made for the first squareRegion
     * @param calculateP
     */
    public void startFirstCalcualtions(Boolean calculateP, Boolean print){
        initializaS();
        calculateS();
        if (calculateP) {
            initializaP();
            computeP();
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
     * Needs calculations for rest
     * @param calculateP
     */
    public void startNextColumnCalcualtions(Boolean calculateP, Boolean print){
        if (Constans.slowGTDMcalc==true) {
            startFirstCalcualtions(calculateP, print);
        }else {
            calculateNextColumnS();
            if (calculateP) {
                computeNextColumnP();
            }
            if (print) {
                System.out.println("Matrix A");
                matrixA.printf();
                printfS();
                printfP();
            }
        }
    }

    /**
     * Needs calculations for rest
     * @param calculateP
     */
    public void startNextRowCalcualtions(Boolean calculateP, Boolean print){
        if (Constans.slowGTDMcalc==true) {
            startFirstCalcualtions(calculateP, print);
        }else {
            calculateNextRowS();
            if (calculateP) {
                computeNextRowP();
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


    private void initializaS() {
        s.clear();
        for (int i = 0; i<Constans.PIXEL_NUMBER_PLUS_1; i++) {
            s.add(0.0);
        }
    }

    private void calculateNextColumnS() {
        Double i = 0.0;
        /**
         * First remove remove old components associeted with old square
         */
        int startY = inputDataMatrix.getStartHeight();
        int startX = inputDataMatrix.getStartWidth();
        for (int k = d ; k < height - d ; k++) {
            i = inputDataMatrix.get(k,  d - 1);
            Double partSum = s.get(i.intValue());
            if (partSum == null)
                partSum = 0.0;
            partSum -= Math.abs(i - matrixA.get(k + startY, d - 1 + startX));// |i-A|
            s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|

            changedPixels.add(i);
        }

        /**
         * Add new components associeted with new square
         */
        for (int k = d ; k < height - d ; k++) {
            i = inputDataMatrix.get(k, - d + width - 1);
            Double partSum = s.get(i.intValue());
            if (partSum == null)
                partSum = 0.0;
            partSum += Math.abs(i - matrixA.get(k + startY, - d + width + startX - 1));// |i-A|
            s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|

            changedPixels.add(i);
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
    private void calculateNextRowS() {
        Double i = 0.0;
        /**
         * First remove remove old components associeted with old square
         */
        int startY = inputDataMatrix.getStartHeight();
        int startX = inputDataMatrix.getStartWidth();
        for (int k = d ; k < width - d ; k++) {
            i = inputDataMatrix.get(d - 1,  k);
            Double partSum = s.get(i.intValue());
            if (partSum == null)
                partSum = 0.0;
            partSum -= Math.abs(i - matrixA.get(d - 1 + startY, k + startX ));// |i-A|
            s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|

            changedPixels.add(i);
        }

        /**
         * Add new components associeted with new square
         */
        for (int k = d ; k < width - d ; k++) {
            i = inputDataMatrix.get(- d + height - 1, k);
            Double partSum = s.get(i.intValue());
            if (partSum == null)
                partSum = 0.0;
            partSum += Math.abs(i - matrixA.get(- d + height - 1 + startY, k + startX));// |i-A|
            s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|

            changedPixels.add(i);
        }
    }

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

    private void calculateS(ArrayList<Double> s1, ArrayList<Double> s2, ArrayList<Double> s3) {
        for (int i = 0; i < Constans.PIXEL_NUMBER ; i++){
            s.set(i, (s1.get(i)+s2.get(i)+s3.get(i))/3.0);//s(i)= SIGMA |i-A|
        }
    }

    private void calculateS(ArrayList<GTDMkopiaZapasowa> matrixes) {
        for (int i = 0; i <  Constans.PIXEL_NUMBER ; i++){
            final int il = i;
            s.set(i,matrixes
                    .stream()
                    .map(matrix -> matrix.getS())
                    .mapToDouble(s -> s.get(il))
                    .sum()/matrixes.size()
            );
        }
    }

    public void printfS() {
        System.out.println("S(i)");
        for (int i = 0; i<s.size(); i++) {
            System.out.println( i +":  " + s.get(i));
        }
        System.out.println();
    }

    private void initializaP() {
        p.clear();
        for (int i = 0; i<Constans.PIXEL_NUMBER_PLUS_1; i++) {
            p.add(0.0);
        }
    }

    public void printfP() {
        System.out.println("Tablica z p");
        for (int i = 0; i<p.size(); i++) {
            System.out.println( i +":  " + p.get(i));
        }
        System.out.println("Suma: "+ p.stream().mapToDouble(x->x).sum());
    }

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
        int a = 0 ;
    }

    public void computeNextColumnP() {
        /**
         * First remove remove old components associeted with old square
         */
        for (int k = d ; k < height - d ; k++) {
            Double iNumber = pRaw.get((int) inputDataMatrix.get(k, d -1));//i
            if (iNumber == null)
                iNumber = 0.0;
            iNumber -= 1;
            pRaw.set((int) inputDataMatrix.get(k, d - 1), iNumber);
        }

        /**
         * Add new components associeted with new square
         */
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

    public void computeNextRowP() {
        /**
         * First remove remove old components associeted with old square
         */
        for (int k = d ; k < width - d ; k++) {
            Double iNumber = pRaw.get((int) inputDataMatrix.get(d - 1 , k ));//i
            if (iNumber == null)
                iNumber = 0.0;
            iNumber -= 1;
            pRaw.set((int) inputDataMatrix.get(d - 1, k), iNumber);
        }

        /**
         * Add new components associeted with new square
         */
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

    private void computeP(ArrayList<Double> p1, ArrayList<Double> p2, ArrayList<Double> p3) {
        int i=0;
        for (i = 0; i <  Constans.PIXEL_NUMBER ; i++){
            p.set(i, (p1.get(i)+p2.get(i)+p3.get(i))/3.0);//s(i)= SIGMA |i-A|
        }
    }

    public void computeP(Matrix inputDataMatrix1, Matrix inputDataMatrix2, Matrix inputDataMatrix3) {

        for (int k = d; k < height - d; k++) {
            for (int l = d; l < width - d; l++) {
                Double iNumber = p.get((int) inputDataMatrix1.get(k, l));//i
                if (iNumber == null)
                    iNumber = 0.0;
                iNumber += 1;
                p.set((int) inputDataMatrix1.get(k, l), iNumber);

                iNumber = p.get((int) inputDataMatrix2.get(k, l));//i
                if (iNumber == null)
                    iNumber = 0.0;
                iNumber += 1;
                p.set((int) inputDataMatrix1.get(k, l), iNumber);

                iNumber = p.get((int) inputDataMatrix3.get(k, l));//i
                if (iNumber == null)
                    iNumber = 0.0;
                iNumber += 1;
                p.set((int) inputDataMatrix3.get(k, l), iNumber);
            }
        }
        for (int i = 0 ; i< Constans.PIXEL_NUMBER_PLUS_1 ; i++) {
            p.set( i ,p.get(i) / n2/3.0);
        }
    }

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
    static String nameFromPath(String path){
        return path.substring(path.lastIndexOf("\\"));
    }

}
