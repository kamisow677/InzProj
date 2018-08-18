import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class GTDM {
    /**
     * neighbourhood size
     */
    private static int d = 1;
    private Matrix inputDataMatrix;
    /**
     * average grey tone matrix
     */
    private MatrixCommon matrixA;
    /**
     * valuee of grey tone difference matrix
     */
    private Map<Double, Double> s;
    /**
     * probabilty of occurence of value
     */
    private Map<Double, Double> p;

    private double n2;

    public static int getD() {
        return d;
    }

    public static void setD(int d) {
        GTDM.d = d;
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

    public Map<Double, Double> getS() {
        return s;
    }

    public void setS(Map<Double, Double> s) {
        this.s = s;
    }

    public Map<Double, Double> getP() {
        return p;
    }

    public void setP(Map<Double, Double> p) {
        this.p = p;
    }

    public double getN2() {
        return n2;
    }

    public void setN2(double n2) {
        this.n2 = n2;
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
                if (k + m < 0 || k + m >= inputDataMatrix.getHeight())
                    continue;
                if (l + n < 0 || l + n >= inputDataMatrix.getWidth())
                    continue;
                suma += inputDataMatrix.get(k + m, l + n);
            }
        }
        return suma / (Math.pow(2 * d + 1, 2) - 1);
    }

    public void findAverageToneMatrix() {
        for (int k = d; k < inputDataMatrix.getHeight() - d; k++) {
            for (int l = d; l < inputDataMatrix.getWidth() - d; l++) {
                matrixA.set(k, l, calculateA(k, l));
            }
        }
    }


    public GTDM(Matrix inputData) throws FileNotFoundException, UnsupportedEncodingException {
        this.matrixA = new MatrixCommon(inputData.getHeight(), inputData.getWidth());
        this.s = new HashMap<Double, Double>();
        this.p = new HashMap<Double, Double>();
        this.inputDataMatrix = inputData;
        n2 = (double) (inputDataMatrix.getHeight() - 2 * d) * (inputDataMatrix.getWidth() - 2 * d);
        inputDataMatrix.printf();

        findAverageToneMatrix();
        System.out.println("Matrix A");
        matrixA.printf();

        calculateS();
        printfS();
        computeP();
        printfP();

    }

    private void printfP() {
        System.out.println("Tablica z p");
        for (Map.Entry<Double, Double> pp : p.entrySet()) {
            System.out.print(pp.getKey() + ": " + pp.getValue() + "    ");
        }
    }

    private void calculateS() {
        Double i=0.0;
        for (int k = d; k < inputDataMatrix.getHeight() - d; k++) {
            for (int l = d; l < inputDataMatrix.getWidth() - d; l++) {
                i=inputDataMatrix.get(k, l);
                Double partSum = s.get(i);
                if (partSum == null)
                    partSum = 0.0;
                partSum += Math.abs(i - matrixA.get(k, l));// |i-A|
                s.put(inputDataMatrix.get(k, l), partSum);//s(i)= SIGMA |i-A|
            }
        }
    }

    private void printfS() {
        System.out.println("S(i)");
        for (Map.Entry<Double, Double> ss : s.entrySet()) {
            System.out.println(ss.getKey() + "  " + ss.getValue());
        }
        System.out.println();
    }

    public void computeP() {
        for (int k = d; k < inputDataMatrix.getHeight() - d; k++) {
            for (int l = d; l < inputDataMatrix.getWidth() - d; l++) {
                Double iNumber = p.get(inputDataMatrix.get(k, l));//i
                if (iNumber == null)
                    iNumber = 0.0;
                iNumber += 1;
                p.put(inputDataMatrix.get(k, l), iNumber);//s(i)= SIGMA |i-A|
            }
        }
        for (Map.Entry<Double, Double> pp : p.entrySet()) {
            double Ni = pp.getValue();
            pp.setValue(Ni / n2);
        }
    }
//
//    public Double computeCoarness() {
//        Double fcos = 0.001;
//        for (Map.Entry<Double, Double> ss : s.entrySet()) {
//            fcos += ss.getValue() * p.get(ss.getKey());
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
//                mianownik += i * pp.getValue() - j * pp1.getValue();
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
}
