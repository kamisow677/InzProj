import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GTDMNew {
    /**
     * neighbourhood size
     */
    private static int d = 1;
    private Matrix inputDataMatrix;
    private  String imageName;
    private int inputDataHeight;
    private int inputDataWidth;
    private static int PIXELS_NUMBER = 256;
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

    private double n2;

    public static int getD() {
        return d;
    }

    public static void setD(int d) {
        GTDMNew.d = d;
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

    /**
     * average gray-tone over a neighborhood centered at, but excluding ( k , I )
     */
    public double calculateA(int k, int l) {
        double suma = 0;
        for (int m = -d; m <= d; m++) {
            for (int n = -d; n <= d; n++) {
                if (m == 0 && n == 0)
                    continue;
                if (k + m < 0 || k + m >= inputDataHeight)
                    continue;
                if (l + n < 0 || l + n >= inputDataMatrix.getWidth())
                    continue;
                suma += inputDataMatrix.get(k + m, l + n);
            }
        }
        return suma / (Math.pow(2 * d + 1, 2) - 1);
    }

    public void calculateMatrixA() {
        for (int k = d; k < inputDataHeight - d; k++) {
            for (int l = d; l < inputDataWidth - d; l++) {
                matrixA.set(k, l, calculateA(k, l));
            }
        }
    }

    public GTDMNew(Matrix inputData){
        this.matrixA = new MatrixCommon(inputData.getHeight(), inputData.getWidth());
        this.s = new ArrayList<Double>(PIXELS_NUMBER);
        this.p = new ArrayList<Double>(PIXELS_NUMBER);
        this.imageName = imageName;
        inputDataHeight = inputData.getHeight();
        inputDataWidth = inputData.getWidth();

        this.inputDataMatrix = inputData;
        n2 = (double) (inputDataHeight - 2 * d) * (inputDataWidth - 2 * d);
    }

    public void startCalcualtions(Boolean calculateP){
        calculateMatrixA();
//        System.out.println("Matrix A");
//        matrixA.printf();

        initializaS();
        calculateS();
        //  printfS();

        if (calculateP) {
            initializaP();
            computeP();
            //  printfP();
        }
    }

    public GTDMNew(GTDMNew matrix1, GTDMNew matrix2, GTDMNew matrix3){
        this.s = new ArrayList<Double>(PIXELS_NUMBER);
        this.p = new ArrayList<Double>(PIXELS_NUMBER);
        inputDataHeight = matrix1.getInputDataMatrix().getHeight();
        inputDataWidth = matrix2.getInputDataMatrix().getWidth();
        this.imageName = imageName;

        this.inputDataMatrix = matrix1.getInputDataMatrix();
        n2 = (double) (inputDataHeight - 2 * d) * (inputDataWidth - 2 * d);
        //inputDataMatrix.printf();

        initializaS();
        calculateS(matrix1.getS(),matrix2.getS(),matrix3.getS());
        //  printfS();

        initializaP();
        computeP(matrix1.getInputDataMatrix(),matrix2.getInputDataMatrix(),matrix3.getInputDataMatrix());
        //  printfP();
    }

    public GTDMNew( ArrayList<GTDMNew> matrixes, int heigth , int width) {
        this.s = new ArrayList<Double>(PIXELS_NUMBER);
        this.p = new ArrayList<Double>(PIXELS_NUMBER);
        inputDataHeight = heigth;
        inputDataWidth = width;
        this.imageName = imageName;

        this.inputDataMatrix = matrixes.get(0).getInputDataMatrix();
        n2 = (double) (inputDataHeight - 2 * d) * (inputDataWidth - 2 * d);
        //inputDataMatrix.printf();

        initializaS();
        calculateS(matrixes);
        //  printfS();

        initializaP();
        computeP();
        //  printfP();
    }

    private void initializaS() {
        for (int i = 0; i<PIXELS_NUMBER; i++) {
            s.add(0.0);
        }
    }

    private void calculateS() {
        Double i=0.0;
        try {
            for (int k = d; k < inputDataHeight - d; k++) {
                for (int l = d; l < inputDataWidth - d; l++) {
                    i = inputDataMatrix.get(k, l);
                    Double partSum = s.get(i.intValue());
                    if (partSum == null)
                        partSum = 0.0;
                    partSum += Math.abs(i - matrixA.get(k, l));// |i-A|
                    s.set(i.intValue(), partSum);//s(i)= SIGMA |i-A|
                }
            }
        }
        catch (Exception ex){
            System.out.println(ex);
        }
    }

    private void calculateS(ArrayList<Double> s1, ArrayList<Double> s2, ArrayList<Double> s3) {
        int i=0;
        for (i = 0; i < 255 ; i++){
            s.set(i, (s1.get(i)+s2.get(i)+s3.get(i))/3.0);//s(i)= SIGMA |i-A|
        }
    }

    private void calculateS(ArrayList<GTDMNew> matrixes) {
        int i=0;
        for (i = 0; i < 255 ; i++){
            final int il = i;
            s.set(i,matrixes
                    .stream()
                    .map(matrix -> matrix.getS())
                    .mapToDouble(s -> s.get(il))
                    .sum()/matrixes.size()
            );
        }
    }

    private void printfS() {
        System.out.println("S(i)");
        for (int i = 0; i<s.size(); i++) {
            System.out.println( i +":  " + s.get(i));
        }
        System.out.println();
    }

    private void initializaP() {
        for (int i = 0; i<PIXELS_NUMBER; i++) {
            p.add(0.0);
        }
    }

    private void printfP() {
        System.out.println("Tablica z p");
        for (int i = 0; i<p.size(); i++) {
            System.out.println( i +":  " + p.get(i));
        }
    }

    public void computeP() {
        for (int k = d; k < inputDataHeight - d; k++) {
            for (int l = d; l < inputDataWidth - d; l++) {
                Double iNumber = p.get((int) inputDataMatrix.get(k, l));//i
                if (iNumber == null)
                    iNumber = 0.0;
                iNumber += 1;
                p.set((int) inputDataMatrix.get(k, l), iNumber);
            }
        }
        for (int i = 0 ; i< PIXELS_NUMBER ; i++) {
            p.set( i ,p.get(i) / n2);
        }
    }
    public void computeP(Matrix inputDataMatrix1, Matrix inputDataMatrix2, Matrix inputDataMatrix3) {
        for (int k = d; k < inputDataHeight - d; k++) {
            for (int l = d; l < inputDataWidth - d; l++) {
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
        for (int i = 0 ; i< PIXELS_NUMBER ; i++) {
            p.set( i ,p.get(i) / n2/3.0);
        }
    }


//    private void computeP(ArrayList<GTDMNew> matrixes) {
//        Double iNumber;
//        for (int k = d; k < inputDataHeight - d; k++) {
//            for (int l = d; l < inputDataWidth - d; l++) {
//                for (GTDMNew gtdm: matrixes) {
//
//                }
//                Double iNumber = p.get((int) inputDataMatrix1.get(k, l));//i
//                if (iNumber == null)
//                    iNumber = 0.0;
//                iNumber += 1;
//                p.set((int) inputDataMatrix1.get(k, l), iNumber);
//
//                iNumber = p.get((int) inputDataMatrix2.get(k, l));//i
//                if (iNumber == null)
//                    iNumber = 0.0;
//                iNumber += 1;
//                p.set((int) inputDataMatrix1.get(k, l), iNumber);
//
//                iNumber = p.get((int) inputDataMatrix3.get(k, l));//i
//                if (iNumber == null)
//                    iNumber = 0.0;
//                iNumber += 1;
//                p.set((int) inputDataMatrix3.get(k, l), iNumber);
//            }
//        }
//        for (int i = 0 ; i< PIXELS_NUMBER ; i++) {
//            p.set( i ,p.get(i) / n2/3.0);
//        }
//    }


    public  void saveToCSV(String part) {
        PrintWriter w = null;
        File fileForCsv = new File(inputDataMatrix.getImageName() + "CsvFiles\\");
        boolean success = (fileForCsv).mkdirs();
        try {
            w = new PrintWriter(fileForCsv.getAbsolutePath()+ "\\" + inputDataMatrix.getColor() + "X" + part + ".csv", "UTF-8");
            boolean firstVal = true;
            Integer i = 0;
            for (Double val : s) {
                if (!firstVal) {
                    w.write(";");
                }
                w.write(i.toString());
                w.write(";");
               // w.write("\"");
                w.write(val.toString());
//                String val2 = val.toString();
//                for (int j = 0; j < val2.length(); j++) {
//                    char ch = val2.charAt(j);
//                    if (ch == '\"') {
//                        w.write("\"");
//                    }
//                    w.write(ch);
//                }
                i++;
               // w.write("\"");
                firstVal = false;
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

}
