
/**
 * @author Kamil Sowa
 * @version 1.0
 * Zwykłą macierz z danymi
 *
 */
public class MatrixCommon implements Matrix {
    /**
     * dane w macierzy
     */
    public double[][] data;
    /**
     * wysokość macierzy
     */
    private int height;
    /**
     * szerokość macierzy
     */
    private int width;
    /**
     * początkowa wysokość lewego górnego rogu kwadratowego regionu lub obrazu
     */
    private int startHeight;
    /**
     * początkowa szerokość lewego górnego rogu kwadratowego regionu lub obrazu
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

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void set(int i, int j, double value) {
        data[i+startHeight][j+startWidth] = value;
    }

    @Override
    public double get(int i, int j) {
        if (j<0 || i<0)
            return -1;
        if (j>639 || i>639)
            return -1;

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
    public String toString2(int num) {
        String str = new String();
        try {
            for (int j = 0; j < width; j++) {
                str += (data[num + startWidth][j + getStartHeight()] + " ");
                str += "\n";
            }
        } catch (Exception ex){
            System.out.println(ex.getStackTrace());
        }
        return  str;
    }

    @Override
    public String getColor() {
        return "GREY";
    }

    @Override
    public String getImageName() {
        return null;
    }

    @Override
    public int getStartHeight() {
        return startHeight;
    }

    @Override
    public void setStartHeight(int startHeight) {
        this.startHeight = startHeight;
    }

    @Override
    public int getStartWidth() {
        return startWidth;
    }

    @Override
    public void setStartWidth(int startWidth) {
        this.startWidth = startWidth;
    }
}
