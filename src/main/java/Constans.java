/**
 *Klasa ze stałymi
 */
public class Constans {
    static int QUADRATIC_SIZE = 15;
    static int D = 1;
    static boolean AVERAGE_MATRIXES = true;
    static String COARNESS ="Szorstkość";
    static String CONTRAST ="Kontrast";
    static String BUSYNESS ="Zajętość";
    static String COMPLEXITY ="Złożoność";
    static String STRENGTH ="Siła";
    static String FOLDER_PATH = "C:\\Users\\Kamil Sowa\\Desktop\\obrazki21\\";
    static String SAVE_PATH = "C:\\Users\\Kamil Sowa\\Desktop\\obrazki21\\Results\\";
    static int QUANTIZATION = 1;
    static long  a = 0 ;
    static long  b = 0;
    static int NUMBER_OF_COLORS = 3;
    static int PIXEL_NUMBER = 255;
    static int PIXEL_NUMBER_PLUS_1 = 256;
    static String validationMessage;
    static boolean validInputData = false;
    static boolean validationEnd = false;
    static boolean slowGTDMcalc = false;
    static boolean parallel = true;

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
