/**
 * @author Kamil Sowa
 * @version 1.0
 * Klasa ze stałymi
 */
public class Constans {
    /**
     * Całkowita wielkość kwadratowego regionu
     */
    static int QUADRATIC_SIZE = 15;
    /**
     * Szerokość regionu peryferyjnego
     */
    static int D = 1;
    /**
     * Dla obrazów kolorowych obliczanie cech na podstawie średniej wartości macierzy średnich wartości i GTDM
     */
    static boolean AVERAGE_MATRIXES = true;
    /**
     * Część nazwy mapy cech utworzonej dla cechy ang. coarness
     */
    static String COARNESS ="Szorstkosc";
    /**
     * Część nazwy mapy cech utworzonej dla cechy ang. contrast
     */
    static String CONTRAST ="Kontrast";
    /**
     * Część nazwy mapy cech utworzonej dla cechy ang. busyness
     */
    static String BUSYNESS ="Zajetosc";
    /**
     * Część nazwy mapy cech utworzonej dla cechy ang. complexity
     */
    static String COMPLEXITY ="Złozonosc";
    /**
     * Część nazwy mapy cech utworzonej dla cechy ang. strength
     */
    static String STRENGTH ="Sila";
    /**
     * Łańcuch przechowuje ścieżkę do folderu przechowującego dane wejściowe
     */
    static String FOLDER_PATH = "C:\\Users\\Kamil Sowa\\Desktop\\obrazki21\\";
    /**
     * Łańcuch przechowuje ścieżkę do folderu przechowującego wyniki pracy wtyczki
     */
    static String SAVE_PATH = "C:\\Users\\Kamil Sowa\\Desktop\\obrazki21\\Results\\";
    /**
     * Współczynnik kwantyzacji
     */
    static int QUANTIZATION = 1;
    /**
     * Liczba kolorów RGB
     */
    static int NUMBER_OF_COLORS = 3;
    /**
     * Maksymalna wartość piksela
     */
    static int PIXEL_NUMBER = 255;
    /**
     * Liczba wszystkich możliwych wartości piksela
     */
    static int PIXEL_NUMBER_PLUS_1 = 256;
    /**
     * Wiadomość o błędzie
     */
    static String validationMessage;
    /**
     * Zmienna typu logicznego. Przyjmuje true jeżeli dane wejściowe są poprawne
     */
    static boolean validInputData = false;
    /**
     * Zmienna typu logicznego. Przyjmuje true jeżeli walidacja się zakończyła
     */
    static boolean validationEnd = false;
    /**
     * Zmienna typu logicznego. Przyjmuje true jeżeli algorytm ma być wykonany używająć wolnej metody obliczania GTDM
     */
    static boolean slowGTDMcalc = false;
    /**
     * Zmienna typu logicznego. Przyjmuje true jeżeli obliczanie map cech ma być zrównoleglone
     */
    static boolean parallel = true;
}
