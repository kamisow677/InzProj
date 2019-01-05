import java.awt.image.DataBuffer;

/**
 *
 * Interfejs macierzy
 * @author Kamil Sowa
 * @version 1.0
 *
 */
public interface Matrix {

    /**
     * Pobranie wartości odpowiedniego piksela
     * @param j wiersz piksela
     * @param i kolumna piksela
     * @return wartość piksela
     */
    double get(int j, int i);
    /**
     * Akcesor wysokości
     * @return wysokość
     */
    int getHeight();
    /**
     * Akcesor szerokości
     * @return szerokości
     */
    int getWidth();
    /**
     * Wyświetlenie obrazu
     */
    void printf();
    /**
     * Pobranie nazwy koloru
     * @return nazwa koloru
     */
    String getColor();
    /**
     * Pobranie nazwy obrazu
     * @return nazwa obrazu
     */
    String getImageName();
    /**
     * Akcesor zmiennej startWidth
     * @return zmienna startWidth
     */
    int getStartWidth();
    /**
     * Akcesor zmiennej startHeight
     * @return zmienna startHeight
     */
    int getStartHeight();
    /**
     * Mutator zmiennej startWidth
     * @param width początkowa szerokość
     */
    void setStartWidth(int width);
    /**
     * Mutator zmiennej startHeight
     * @param height początkowa wysokość
     */
    void setStartHeight(int height);

    /**
     * Mutator zmiennej width
     * @param width szerokość obrazu
     */
    void setWidth(int width);
    /**
     * Mutator zmiennej height
     * @param height wysokość obrazu
     */
    void setHeight(int height);

}
