import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

/**
 *
 * Klasa ta jest macierzą przechowującą dane o obrazie
 * @author Kamil Sowa
 * @version 1.0
 *
 */
public class ImageMatrix implements Matrix {
    /**
     * Obraz buforowany
     */
    BufferedImage bufferedImage;
    /**
     * Nazwa obrazu wraz z ścieżka do obrazu
     */
    String imageNameWithPathName;
    /**
     * Szerokość obrazu
     */
    int width = 0;
    /**
     * Wysokość obrazu
     */
    int height = 0;
    /**
     * Wartość x lewego górnego rogu początku obrazu lub kwadratowego regionu w obrazie
     */
    int startWidth = 0;
    /**
     * Wartość y lewego górnego rogu początku obrazu lub kwadratowego regionu w obrazie
     */
    int startHeight = 0;
    /**
     * Szerokość sąsiedztwa, regionu peryferyjnego
     */
    int d;
    /**
     * Bufor z danymi o obrazie
     */
    public DataBuffer dataBuffer;

    /**
     * Możliwe kolory kanałów
     */
    public enum COLOR {RED, GREEN, BLUE, GREY, ALL}
    /**
     * Kolor obrazu
     */
    private COLOR color;

    /**
     * Konstruktor
     * @param bufferedImage obraz buforowany
     * @param color kolor obrazu
     * @param imageNameWithPathName nazwa obrazu ze ścieżką
     */
    public ImageMatrix(BufferedImage bufferedImage, COLOR color, String imageNameWithPathName) {
        this.bufferedImage = bufferedImage;
        this.color = color;
        this.imageNameWithPathName = imageNameWithPathName;
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
        startWidth = 0;
        startHeight = 0;
        this.dataBuffer = bufferedImage.getData().getDataBuffer();
    }

    /**
     * Konstruktor kopiujący
     * @param matrix macierz z obrazem
     */
    public ImageMatrix(ImageMatrix matrix) {
        this.bufferedImage = matrix.bufferedImage;
        this.color = matrix.color;
        this.imageNameWithPathName = matrix.imageNameWithPathName;
        width = matrix.bufferedImage.getWidth();
        height = matrix.bufferedImage.getHeight();
        startWidth = 0;
        startHeight = 0;
        this.dataBuffer = bufferedImage.getData().getDataBuffer();
    }

    public String getImageName() {
        return imageNameWithPathName;
    }

    /**
     * Pobranie wartości odpowiedniego piksela
     * @param j wiersz piksela
     * @param i kolumna piksela
     * @return wartość piksela
     */
    @Override
    public double get(int j, int i) {
        if (i==13 && j==-6 && startWidth==1 && startHeight==0){
            System.out.println("WSZEDLEDM");
            int elo= 2;
        }
        int ileZa;
        int jw=j;
        int iw= i;
        if (j+startHeight<0) {
            j=Math.abs(j + startHeight +1);
            j-=startHeight;
        }
        if (j+startHeight>=bufferedImage.getHeight()) {
             ileZa=Math.abs(j + startHeight - (bufferedImage.getHeight() - 1));
             j= bufferedImage.getHeight() - ileZa;
             j-=startHeight;
        }
        if (i+startWidth<0) {
            i=Math.abs(i + startWidth +1);
            i-=startWidth;
        }
        if (i+startWidth>=bufferedImage.getWidth()) {
            ileZa=Math.abs( i + startWidth - (bufferedImage.getWidth() - 1) );
            i= bufferedImage.getWidth() - ileZa;
            i-=startWidth;
        }
        int argb= 0;
        try {
            argb = bufferedImage.getRGB(i + startWidth, j + startHeight);
            switch (color) {
                case GREY: {
                    Integer a = dataBuffer.getElem((j + startHeight) * bufferedImage.getWidth() + i + startWidth) / Constans.QUANTIZATION;
                    return a;
                }
                case BLUE: {
                    return ((argb) & 0xFF) / Constans.QUANTIZATION;
                }
                case GREEN: {
                    return ((argb >> 8) & 0xFF) / Constans.QUANTIZATION;
                }
                case RED: {
                    return ((argb >> 16) & 0xFF) / Constans.QUANTIZATION;
                }
                case ALL: {
                    return (((argb) & 0xFF + (argb >> 8) & 0xFF + (argb >> 16) & 0xFF) / 3.0) / Constans.QUANTIZATION;
                }

            }
        }catch (IndexOutOfBoundsException ex){
            System.out.println("BLAD "+i+" "+j+ "  "+iw+" "+jw);
            System.out.println("StartHeight "+startHeight+" StartWIdth" +startWidth);

        }
        return ((argb) & 0xFF)/Constans.QUANTIZATION;
    }

    /**
     * Pobranie nazwy koloru
     * @return nazwę koloru
     */
    public String getColor() {
        switch (color) {
            case GREY: {
                return "GREY";
            }
            case BLUE: {
                return  "BLUE";
            }
            case GREEN: {
                return "GREEN";
            }
            case RED: {
                return "RED";
            }
            case ALL: {
                return "ALL";
            }
        }
        return "???";
    }

    /**
     * UStawienie koloru
     * @param color kolor
     */
    public void setColor(COLOR color) {
        this.color = color;
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
     * Akcesor szerokości
     * @return szerokości
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Wyświetlenie obrazu
     */
    public void printf() {
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                System.out.print(get(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Mutator zmiennej width
     * @param width szerokość obrazu
     */
    public void setWidth(int width) {
        this.width = width;
    }
    /**
     * Mutator zmiennej height
     * @param height wysokość obrazu
     */
    public void setHeight(int height) {
        this.height = height;
    }
    /**
     * Mutator zmiennej startWidth
     * @param startWidth początkowa szerokość
     */
    public void setStartWidth(int startWidth) {
        this.startWidth = startWidth;
    }
    /**
     * Mutator zmiennej startHeight
     * @param startHeight początkowa wysokość
     */
    public void setStartHeight(int startHeight) {
        this.startHeight = startHeight;
    }
    /**
     * Akcesor zmiennej startWidth
     */
    public int getStartWidth() {
        return startWidth;
    }
    /**
     * Akcesor zmiennej startHeight
     */
    public int getStartHeight() {
        return startHeight;
    }
    /**
     * Akcesor zmiennej bufferedImage
     * @return zmienna bufferedImage
     */
    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
