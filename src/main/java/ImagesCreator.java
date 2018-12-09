import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Kamil Sowa
 * @version 1.0
 * Klasa ta służy do tworzenia obrazów
 *
 */
public class ImagesCreator {


    /**
     * Tworzy obraz RGB
     * @param prop Lista map z wartościami 5 cech. Kluczami sąnazwy parametrów metody
     * @param height wysokość obrazu
     * @param width szerokośćobrazu
     * @param imageName nazwa obrazu
     */
    public static void createRGBPixelImage(ArrayList<Map<String,Double>> prop , int height, int width, String imageName){
        System.out.println("Width: "+(width-Constans.QUADRATIC_SIZE));
        System.out.println("Height: "+(height-Constans.QUADRATIC_SIZE));

        System.out.println(prop.get(0).size());
        String[] featuresNames = {Constans.COARNESS, Constans.CONTRAST, Constans.BUSYNESS, Constans.COMPLEXITY, Constans.STRENGTH};
        Double[] scalesValues = new Double[5];
        BufferedImage[] buffImages = new BufferedImage[5];

        for (int i = 0 ; i < prop.get(0).size() ; i++){
            final int number = i;
            OptionalDouble max = prop
                    .stream()
                    .mapToDouble(s -> s.get(featuresNames[number]))
                    .max();
            scalesValues[i] = max.getAsDouble() / 255.0;
        }

        try {
            for (int i = 0 ; i<5 ; i++){
                buffImages[i] =  new BufferedImage(width-Constans.QUADRATIC_SIZE, height-Constans.QUADRATIC_SIZE, BufferedImage.TYPE_INT_ARGB);
            }

            for (int i = 0; i < height-Constans.QUADRATIC_SIZE ; i++) {
                for (int j = 0; j < width-Constans.QUADRATIC_SIZE ; j++) {
                    for (int k = 0 ; k<5 ; k++) {
                        int red = (int) Math.abs(((prop.get(i * (width - Constans.QUADRATIC_SIZE) + j).get(featuresNames[k])) / scalesValues[k]));
                        int green = (int) Math.abs(((prop.get(i * (width - Constans.QUADRATIC_SIZE) + j).get(featuresNames[k])) / scalesValues[k]));
                        int blue = (int) Math.abs(((prop.get(i * (width - Constans.QUADRATIC_SIZE) + j).get(featuresNames[k])) / scalesValues[k]));
                        int alpha = 255;
                        int rgb = alpha;
                        rgb = (rgb << 8) + red;
                        rgb = (rgb << 8) + green;
                        rgb = (rgb << 8) + blue;
                        buffImages[k].setRGB(j, i, rgb);
                    }
                }
            }

            for (int i = 0 ; i < 5 ; i++) {
                System.out.println("SAVED " + featuresNames[i] + ".png");
                File f = new File(Constans.SAVE_PATH + nameFromPath(imageName) + featuresNames[i] + ".png");
                ImageIO.write(buffImages[i], "PNG", f);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pobranie nazwy ze ścieżki
     * @param path ścieżka
     * @return nazwę pliku
     */
    static String nameFromPath(String path){
        return path.substring(path.lastIndexOf("\\"));
    }

    /**
     * Tworzy obraz wykorzystywany do testów jednostkowych
     * @param data dane obrazu, macierz wartości
     * @param size wielkość macierzy
     */
    public static void createTestPixelImage(byte [][] data, int size){
       // byte [][] daneTestowe = new byte[][]{{1, 2, 3, 4, 5}, {2, 3, 1, 4, 1}, {5, 2, 2, 3, 3}, {3, 1, 4, 4, 5}, {1, 5, 4, 2, 2}};
        BufferedImage buffImage;
        try {
            byte [] newData ;
            buffImage =  new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
            newData = ((DataBufferByte) buffImage.getRaster().getDataBuffer()).getData();
            for (int i = 0 ; i<size ; i++) {
                for (int k = 0; k < size; k++) {
                    newData[i*size+k] = data[i][k];
                }
            }
            System.out.println("SAVED "+ nameFromPath("\\Test") + ".png");
            File f = new File(Constans.SAVE_PATH + nameFromPath("\\Test")  + ".png");
            ImageIO.write(buffImage, "PNG", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tworzy obraz w skali szarości
     * @param prop Lista map z wartościami 5 cech. Kluczami sąnazwy parametrów metody
     * @param height wysokość obrazu
     * @param width szerokośćobrazu
     * @param imageName nazwa obrazu
     */
    public static void createGreyPixelImage(ArrayList<Map<String,Double>> prop , int height, int width, String imageName){
        //byte [][] daneTestowe = new byte[][]{{1, 2, 3, 4, 5}, {2, 3, 1, 4, 1}, {5, 2, 2, 3, 3}, {3, 1, 4, 4, 5}, {1, 5, 4, 2, 2}};
        System.out.println("Width: "+(width-Constans.QUADRATIC_SIZE));
        System.out.println("Height: "+(height-Constans.QUADRATIC_SIZE));

       // createChange(prop);

        System.out.println(prop.get(0).size());
        String[] featuresNames = {Constans.COARNESS, Constans.CONTRAST, Constans.BUSYNESS, Constans.COMPLEXITY, Constans.STRENGTH};
        Double[] scalesValues = new Double[5];
        BufferedImage[] buffImages = new BufferedImage[5];

        for (int i = 0 ; i < prop.get(0).size() ; i++){
            final int number = i;
            OptionalDouble max = prop
                    .stream()
                    .mapToDouble(s -> s.get(featuresNames[number]))
                    .max();
            scalesValues[i] = max.getAsDouble() / 255.0;
        }

        try {
            byte [] newData ;
            byte [][] newDates = new byte[5][(height)*(width)];
            for (int i = 0 ; i<5 ; i++){
                buffImages[i] =  new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
                newData = ((DataBufferByte) buffImages[i].getRaster().getDataBuffer()).getData();
                newDates[i] = newData;
            }

            for (int i = 0; i < height ; i++) {
                for (int j = 0; j < width ; j++) {
                    for (int k = 0 ; k<5 ; k++) {
                        Integer grey = (int) Math.abs(((prop.get(i * (width ) + j).get(featuresNames[k])) / scalesValues[k]));
                        newDates[k][i*(width )+j] =(grey.byteValue()) ;
                    }
                }
            }

            for (int i = 0 ; i < 5 ; i++) {
                System.out.println("SAVED " + featuresNames[i] + ".png");
                File f = new File(Constans.SAVE_PATH + nameFromPath(imageName) + featuresNames[i] + ".png");
                ImageIO.write(buffImages[i], "PNG", f);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void createChange(ArrayList<Map<String,Double>> prop){
        String[] featuresNames = {Constans.COARNESS, Constans.CONTRAST, Constans.BUSYNESS, Constans.COMPLEXITY, Constans.STRENGTH};
        ArrayList<List<Prop>>  propsyAll =new ArrayList<>();

        for (int k = 0 ; k<5 ; k++) {
            ArrayList propsy = new ArrayList();
            for (int i = 0; i < prop.size(); i++) {
                propsy.add(new Prop(featuresNames[k], Math.abs(prop.get(i).get(featuresNames[k])), i));
            }
            propsyAll.add(propsy);
        }
        for (int k = 0 ; k<5 ; k++){
//            Collections.sort(propsyAll.get(k), (f1, f2) -> Double.compare(f1.value, f2.value));
            List<Prop> propsy = propsyAll.get(k).parallelStream().sorted((f1, f2) -> Double.compare(f1.value, f2.value)).collect(Collectors.toList());

            int ile = propsyAll.get(k).size();
            Double wsp = new Double(ile);
            wsp*=0.999;
            Prop top = propsy.get( wsp.intValue() );
            for (int i = wsp.intValue(); i<ile ; i++){
                propsy.get(i).setValue(top.getValue());
            }
//            Collections.sort(propsy, (f1, f2) -> Double.compare(f1.pos, f2.pos));
            List<Prop> collect = propsy.parallelStream().sorted((f1, f2) -> Double.compare(f1.pos, f2.pos)).collect(Collectors.toList());
            for (int i = 0; i < prop.size(); i++) {
                prop.get(i).put(featuresNames[k],collect.get(i).getValue());
            }
        }
    }

}
