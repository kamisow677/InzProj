import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Na kartce liczone coarness i contrast
 */

public class ImageCreatorTest {
    static int WIELKSC_MACIRZY = 6;
    byte[][] dane = {{1, 2, 5, 1, 4, 4}, {1, 2, 3, 4, 1, 4}, {0, 1, 3, 1, 2, 3},
            {4, 3, 1, 1, 2, 2}, {5, 5, 1, 2, 4, 2}, {2, 3, 1, 5, 4, 1}};

    @Test
    public void imageCreation() {

        Constans.SAVE_PATH=Paths.get(".").toAbsolutePath().normalize().toString();
        ImagesCreator.createTestPixelImage(dane,WIELKSC_MACIRZY);
        System.out.println("------------------------------------------------");

        File img = new File(Constans.SAVE_PATH + "\\Test.png");
        BufferedImage buffImage=null;
        try {
            buffImage = ImageIO.read(img);
        }catch (Exception ex){

        }
        Matrix testData = new ImageMatrix(buffImage, ImageMatrix.COLOR.GREY,Constans.SAVE_PATH +"\\Test.png");
        for (int i = 0 ; i < WIELKSC_MACIRZY; i++){
            for (int j = 0 ; j < WIELKSC_MACIRZY; j++){
                assertEquals(dane[i][j],testData.get(i,j),0.0001);
            }
        }
        System.out.println("Dane testowe");
        testData.printf();


    }
}
