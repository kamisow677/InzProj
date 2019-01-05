
/**
 *
 * Zwykłą macierz z danymi
 * @author Kamil Sowa
 * @version 1.0
 *
 */
public class MatrixCommon implements Matrix {
    /**
     * Dane w macierzy
     */
    public double[][] data;
    /**
     * Wysokość macierzy
     */
    private int height;
    /**
     * Szerokość macierzy
     */
    private int width;
    /**
     * Początkowa wysokość lewego górnego rogu kwadratowego regionu lub obrazu
     */
    private int startHeight;
    /**
     * Początkowa szerokość lewego górnego rogu kwadratowego regionu lub obrazu
     */
    private int startWidth;

    /**
     * Konstruktor
     * @param data dane macierzy
     * @param height wysokość macierzy
     * @param width szerokość macierzy
     */
    public MatrixCommon(double[][] data, int height, int width ) {
        this.data = data;
        this.height = height;
        this.width = width;
        this.startWidth = 0;
        this.startHeight = 0;
    }

    /**
     * Konstruktor
     * @param data dane macierzy
     * @param height wysokość macierzy
     * @param width szerokość macierzy
     * @param startHeight początkowa wysokość lewego górnego rogu kwadratowego regionu lub obrazu
     * @param startWidth początkowa szerokość lewego górnego rogu kwadratowego regionu lub obrazu
     */
    public MatrixCommon(double[][] data, int height, int width, int startHeight, int startWidth) {
        this(data,height,width);
        this.startHeight = startHeight;
        this.startWidth = startWidth;
    }

    /**
     * Konstruktor
     * @param height wysokość macierzy
     * @param width szerokość macierzy
     */
    public MatrixCommon(int height, int width) {
        this.data = new double[height][width];
        this.height = height;
        this.width = width;
        this.startHeight = 0;
        this.startWidth = 0;
    }
    /**
     * Akcesor wysokości
     * @return wysokość
     */
    @Override
    public int getHeight() {
        return height;
    }
    /**
     * Mutator zmiennej height
     * @param height wysokość obrazu
     */
    public void setHeight(int height) {
        this.height = height;
    }
    /**
     * Akcesor szerokości
     * @return szerokości
     */
    @Override
    public int getWidth() {
        return width;
    }
    /**
     * Mutator zmiennej width
     * @param width szerokość obrazu
     */
    public void setWidth(int width) {
        this.width = width;
    }

    public void set(int i, int j, double value) {
        data[i+startHeight][j+startWidth] = value;
    }

    /**
     *
     * @param i wiersz piksela
     * @param j kolumna piksela
     * @return wartość piksela
     */
    @Override
    public double get(int i, int j) {
        int ileZa;
        if (j<0) {
            j=Math.abs(j+1);
        }
        if (j>=width) {
            ileZa=Math.abs( j- (width - 1));
            j=width-ileZa;
            j-=startWidth;
        }
        if (i<0) {
            i=Math.abs(i+1);
        }
        if (i>=height) {
            ileZa=Math.abs(i-(height- 1));
            i=height-ileZa;
            i-=startHeight;
        }
        return data[i+startHeight][j+startWidth];
    }

    /**
     * Wypisanie zawartości macierzy
     */
    public void printf() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(data[i+startHeight][j+getStartWidth()] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Wypisanie zawartości macierzy
     */
    @Override
    public String toString() {
        String str = new String();
        try {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    str += (data[i + startWidth][j + getStartHeight()] + " ");
                }
                str += "\n";
            }
            str += "\n";
        } catch (Exception ex){
            System.out.println(ex.getStackTrace());
        }
        return  str;
    }
    /**
     * Pobranie nazwy koloru
     * @return nazwa koloru
     */
    @Override
    public String getColor() {
        return "GREY";
    }
    /**
     * Pobranie nazwy obrazu
     * @return nazwa obrazu
     */
    @Override
    public String getImageName() {
        return null;
    }
    /**
     * Akcesor zmiennej startHeight
     */
    @Override
    public int getStartHeight() {
        return startHeight;
    }
    /**
     * Mutator zmiennej startHeight
     * @param startHeight początkowa wysokość
     */
    @Override
    public void setStartHeight(int startHeight) {
        this.startHeight = startHeight;
    }
    /**
     * Akcesor zmiennej startWidth
     */
    @Override
    public int getStartWidth() {
        return startWidth;
    }
    /**
     * Mutator zmiennej startWidth
     * @param startWidth początkowa szerokość
     */
    @Override
    public void setStartWidth(int startWidth) {
        this.startWidth = startWidth;
    }
}
