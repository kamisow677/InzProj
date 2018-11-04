import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

/**
 * @author Kamil Sowa
 * @version 1.0
 * Klasa ta jest macierzą przechowującą dane o obrazie
 *
 */
public class ImageMatrix implements Matrix {
    /**
     * Obraz buforowany
     */
    BufferedImage bufferedImage;
    /**
     * nazwa obrazu wraz z ścieżka do obrazu
     */
    String imageNameWithPathName;
    /**
     * szerokość obrazu
     */
    int width = 0;
    /**
     * wysokość obrazu
     */
    int height = 0;
    /**
     * Wartość x lewego górnego rogu początku obrazu lub kwadratowego regionu do przetwarzania
     */
    int startWidth = 0;
    /**
     * Wartość y lewego górnego rogu początku obrazu lub kwadratowego regionu do przetwarzania
     */
    int startHeight = 0;
    /**
     * szerokość sąsiedztwa
     */
    int d;
    /**
     * bufor z danymi o obrazie
     */
    public DataBuffer dataBuffer;

    /**
     * możliwe kolory kanałó
     */
    public enum COLOR {RED, GREEN, BLUE, GREY, ALL}

    /**
     * kolor obrazu
     */
    private COLOR color;

    /**
     * Konstruktoe
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
     * @param j kolumna piksela
     * @param i wiersz piksela
     * @return wartość piksela
     */
    @Override
    public double get(int j, int i) {
        int argb = bufferedImage.getRGB(i+startWidth, j+startHeight);
        switch (color) {
            case GREY: {
               // System.out.println((j+startHeight)*width + i + startWidth);
                //return (((argb) & 0xFF + (argb >> 8) & 0xFF + (argb >> 16) & 0xFF)/3.0)/Constans.QUANTIZATION;
               // return dataBuffer.getElem((i+startWidth)*height + j + startHeight);
              //  System.out.println("j: "+j+" i:"+i+ "startHeight: "+startHeight+ " startWidth:"+startWidth);
                Integer a = dataBuffer.getElem((j+startHeight)*bufferedImage.getWidth() + i + startWidth)/Constans.QUANTIZATION;
              //  Integer a = dataBuffer
                int b;
                if (a.byteValue()>0)
                    b =a.byteValue()+128;
                else
                    b =a.byteValue()+256;
                return  a;
//                return ((argb) & 0xFF)/Constans.QUANTIZATION;
            }
            case BLUE: {
                return ((argb) & 0xFF)/Constans.QUANTIZATION;
            }
            case GREEN: {
                return ((argb >> 8) & 0xFF)/Constans.QUANTIZATION;
            }
            case RED: {
                return ((argb >> 16) & 0xFF)/Constans.QUANTIZATION;
            }
            case ALL:{
                return (((argb) & 0xFF + (argb >> 8) & 0xFF + (argb >> 16) & 0xFF)/3.0)/Constans.QUANTIZATION;
            }

        }
        return ((argb) & 0xFF)/Constans.QUANTIZATION;
    }

    /**
     *  Pobranie nazwy koloru
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

    public void setColor(COLOR color) {
        this.color = color;
    }

    @Override
    public int getHeight() {
        return height;
    }

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

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public void setImageName(String imageName) {
        this.imageNameWithPathName = imageName;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setStartWidth(int startWidth) {
        this.startWidth = startWidth;
    }

    public void setStartHeight(int startHeight) {
        this.startHeight = startHeight;
    }

    public int getStartWidth() {
        return startWidth;
    }

    public int getStartHeight() {
        return startHeight;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }
}
