public class Constans {
    static int QUADRATIC_SIZE = 32;
    static int D = 1;
    static boolean AVERAGE_MATRIXES = true;
    static String COARNESS ="COARNESS";
    static String CONTRAST ="CONTRAST";
    static String BUSYNESS ="BUSYNESS";
    static String COMPLEXITY ="COMPLEXITY";
    static String STRENGTH ="STRENGTH";
    static String FOLDER_PATH = "C:\\Users\\Kamil Sowa\\Desktop\\obrazki21\\";
    static String SAVE_PATH = "C:\\Users\\Kamil Sowa\\Desktop\\obrazki21\\";
    static int QUANTIZATION = 1;
    static long  a = 0 ;
    static long  b = 0;
    static int NUMBER_OF_COLORS = 3;
    static int PIXEL_NUMBER = 255;
    static int PIXEL_NUMBER_PLUS_1 = 256;
    static int PROGRESS = 0;

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
