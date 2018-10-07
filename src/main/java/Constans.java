public class Constans {
    static int QUADRATIC_SIZE = 16;
    static int D = 2;
    static boolean AVERAGE_MATRIXES = true;
    static String COARNESS ="COARNESS";
    static String CONTRAST ="CONTRAST";
    static String BUSYNESS ="BUSYNESS";
    static String COMPLEXITY ="COMPLEXITY";
    static String STRENGTH ="STRENGTH";
    static long  a = 0 ;
    static long  b = 0;

    public static int getQuadraticSize() {
        return QUADRATIC_SIZE;
    }

    public static int getD() {
        return D;
    }

    public static boolean isAverageMatrixes() {
        return AVERAGE_MATRIXES;
    }

    public static void setQuadraticSize(int quadraticSize) {
        QUADRATIC_SIZE = quadraticSize;
    }

    public static void setD(int d) {
        D = d;
    }

    public static void setAverageMatrixes(boolean averageMatrixes) {
        AVERAGE_MATRIXES = averageMatrixes;
    }

}
