import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import javax.imageio.ImageIO;

public class MainTestClass {

    public static void main(String[] args) {
//    public  static  void start(){
        System.out.println("TESTOWA KLASA DO BUFOROW");
        File img = new File("ork.jpg");
        int WIELKSC_MACIRZY = 10;

        MatrixData matrixData = null;
        try {
            BufferedImage buffImage = ImageIO.read(img);
            buffImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    int red = 255;
                    int green = 255;
                    int blue = 0;
                    int alpha = 255;
                    int rgb = alpha;
                    rgb = (rgb << 8) + red;
                    rgb = (rgb << 8) + green;
                    rgb = (rgb << 8) + blue;
                    buffImage.setRGB(i, j, rgb);
                }
            }
            File f = new File("MyFile.png");
            ImageIO.write(buffImage, "PNG", f);

            matrixData = new MatrixData(buffImage, MatrixData.COLOR.GREY);
            System.out.println("Height: " + matrixData.getHeight());
            System.out.println("Width: " + matrixData.getWidth());


        } catch (IOException e) {
            e.printStackTrace();
        }


        Random rand = new Random();
        double[][] dane = new double[WIELKSC_MACIRZY][WIELKSC_MACIRZY];
        for (int i = 0; i < WIELKSC_MACIRZY; i++) {
            for (int j = 0; j < WIELKSC_MACIRZY; j++) {
                dane[i][j] = Math.abs(rand.nextInt() % 30);
            }
        }

        Matrix daneTestowe = new MatrixCommon(dane, WIELKSC_MACIRZY, WIELKSC_MACIRZY);

        try {
            GTDM gdtm = new GTDM(daneTestowe);
            TexturalProperties texturalProperties = new TexturalProperties(gdtm);
            //GTDMNew gdtmnew = new GTDMNew(daneTestowe);
            //TexturalProperties texturalProperties = new TexturalProperties(gdtm);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}

