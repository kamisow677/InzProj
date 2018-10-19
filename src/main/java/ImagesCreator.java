import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.OptionalDouble;

public class ImagesCreator {


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

    static String nameFromPath(String path){
        return path.substring(path.lastIndexOf("\\"));
    }

    public static void createGreyPixelImage(ArrayList<Map<String,Double>> prop , int height, int width, String imageName){
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
            byte [] newData ;
            byte [][] newDates = new byte[5][(height-Constans.QUADRATIC_SIZE)*(width-Constans.QUADRATIC_SIZE)];
            for (int i = 0 ; i<5 ; i++){
                buffImages[i] =  new BufferedImage(width-Constans.QUADRATIC_SIZE, height-Constans.QUADRATIC_SIZE, BufferedImage.TYPE_BYTE_GRAY);
                newData = ((DataBufferByte) buffImages[i].getRaster().getDataBuffer()).getData();
                newDates[i] = newData;
            }

            for (int i = 0; i < height-Constans.QUADRATIC_SIZE ; i++) {
                for (int j = 0; j < width-Constans.QUADRATIC_SIZE ; j++) {
                    for (int k = 0 ; k<5 ; k++) {
                        Integer grey = (int) Math.abs(((prop.get(i * (width - Constans.QUADRATIC_SIZE) + j).get(featuresNames[k])) / scalesValues[k]));
                        newDates[k][i*(width-Constans.QUADRATIC_SIZE )+j] =(grey.byteValue()) ;
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

}
