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

public class TexturalPropertiesTest {
    static int WIELKSC_MACIRZY = 6;
    int SQUARE_SIZE = 5;
    GTDM gdtmNowe;
    GTDM gdtmFirst;
    GTDM gdtmNext;

    @BeforeAll
    public static void createImage(){
        byte[][] dane = {{1, 2, 5, 1, 4, 4}, {1, 2, 3, 4, 1, 4}, {0, 1, 3, 1, 2, 3},
                {4, 3, 1, 1, 2, 2}, {5, 5, 1, 2, 4, 2}, {2, 3, 1, 5, 4, 1}};
        Constans.SAVE_PATH=Paths.get(".").toAbsolutePath().normalize().toString();
        ImagesCreator.createTestPixelImage(dane,WIELKSC_MACIRZY);
    }

    @Test
    public void customOnGDTM() {
        System.out.println("------------------------------------------------");
        Constans.QUANTIZATION =1 ;
        Constans.QUADRATIC_SIZE=SQUARE_SIZE;


        File img = new File(Constans.SAVE_PATH + "\\Test.png");
        BufferedImage buffImage=null;
        try {
            buffImage = ImageIO.read(img);
        }catch (Exception ex){

        }
        Matrix testData = new ImageMatrix(buffImage, ImageMatrix.COLOR.GREY,Constans.SAVE_PATH +"\\Test.png");
        System.out.println("Dane testowe");
        testData.printf();

        Double[] expectedS = {0.0,7.875,0.75,2.125,3.125,2.5};
        Double[] expectedP = {0.0,0.375,0.25,0.1875,0.125,0.0625};

        gdtmNowe = new GTDM(testData);
        gdtmNowe.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNowe.setS(new ArrayList<>(Arrays.asList(expectedS)));


        TexturalProperties texturalPropertiesNew = new TexturalProperties(gdtmNowe);
//        assertEquals(texturalPropertiesNew.getCoarness(), 0.91428571428, 0.0001);
//        assertEquals(texturalPropertiesNew.getContrast(), 0.15755208333, 0.0001);
//        assertEquals(texturalPropertiesNew.getComplexity(), 2.28125, 0.0001);
//        assertEquals(texturalPropertiesNew.getStrength(), 3.09090909, 0.0001);

    }

    @Test
    public void customGDTMOfSquareSize() {
        Constans.QUANTIZATION =1 ;
        Constans.QUADRATIC_SIZE=SQUARE_SIZE;
        Double[] expectedS;
        Double[] expectedP;

        double[][] expectedMatrixAdate = {{1.375, 2.5, 2.5, 3.375, 2.875, 3.625}, {1.0, 2.0, 2.375, 2.5, 2.875, 3.125}, {2.0, 2.125, 2.0, 2.125, 2.25, 2.5},
                {2.875, 2.5, 2.125, 2.0, 2.125, 2.5}, {3.5, 2.5, 2.625, 2.375, 2.375, 2.25}, {3.375, 2.5, 3.125, 2.75, 3.0, 2.375}};

        /**
         * CREATION OF TexturalPropertiesTestOld class for matrix A calculation
         */
        File img = new File(Constans.SAVE_PATH + "\\Test.png");
        BufferedImage buffImage=null;
        try {
            buffImage = ImageIO.read(img);
        }catch (Exception ex){

        }
        TexturalProperties texturalPropertiesNew;

        /**
         * CALCULACTIONS OF SWIARES 0,0
         */
        Matrix daneTestowe = new ImageMatrix(buffImage, ImageMatrix.COLOR.GREY,Constans.SAVE_PATH +"\\Test.png");
        daneTestowe.setHeight(SQUARE_SIZE);
        daneTestowe.setWidth(SQUARE_SIZE);

        expectedS = new Double[]{0.0,1.5,1.0,0.0,0.0,0.0};
        expectedP = new Double[]{0.0,0.666666,0.333333,0.0,0.0};

        gdtmFirst = new GTDM(daneTestowe,new MatrixCommon(expectedMatrixAdate,WIELKSC_MACIRZY,WIELKSC_MACIRZY));
        gdtmFirst.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmFirst.setS(new ArrayList<>(Arrays.asList(expectedS)));

        texturalPropertiesNew = new TexturalProperties(gdtmFirst);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.75, 0.0001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.06172827160499999, 0.0001);
          assertEquals(texturalPropertiesNew.getBusyness(),1.333332*10000000,0.01);
        assertEquals(texturalPropertiesNew.getComplexity(), 0.29629629629629634, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 0.7999992, 0.0001);

        /**
         * 0,1
         */
        expectedS = new Double[]{0.0,0.75,1.0,0.625,0.0,5.0};
        expectedP = new Double[]{0.0,0.333333,0.333333,0.1111111,0.0,0.222222};

        gdtmNext = new GTDM(gdtmFirst, true);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));

        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.56692913385, 0.0001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.3068695629, 0.0001);
         assertEquals(texturalPropertiesNew.getBusyness(), 0.33072916667, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 6.200462550636283, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 4.911483091525424, 0.0001);

        /**
         * 0,2
         */
        expectedS = new Double[]{0.0,4.75,1.0,0.625,1.5,5.0};
        expectedP = new Double[]{0.0,0.22222,0.333333,0.1111111,0.111111,0.222222};

        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));

        texturalPropertiesNew = new TexturalProperties(gdtmNext);

        /**
         * 0,3
         */
        expectedS = new Double[]{0.0,6.625,0.0,0.625,3.75,5.0};
        expectedP = new Double[]{0.0,0.33333,0.0,0.1111111,0.333333,0.222222};

        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));

        texturalPropertiesNew = new TexturalProperties(gdtmNext);

        /**
         * 0,4
         */
        expectedS = new Double[]{0.0,6.625,0.0,0.0,5.375,0.0};
        expectedP = new Double[]{0.0,0.33333,0.0,0.0,0.666666,0.0};

        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));

        texturalPropertiesNew = new TexturalProperties(gdtmNext);

        /**
         * 0,5
         */
        expectedS = new Double[]{0.0,1.875,0.0,0.0,5.5,0.0};
        expectedP = new Double[]{0.0,0.11111,0.0,0.0,0.888888,0.0};

        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));

        texturalPropertiesNew = new TexturalProperties(gdtmNext);

        /**
         * GOING DOWN 1,0
         */
        expectedS = new Double[]{4.0,1.875,0.5,0.0,0.0,0.0};
        expectedP = new Double[]{0.222222,0.555555,0.222222,0.0,0.0};

        gdtmNext = new GTDM(gdtmNext, false);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));

        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.48979591836, 0.001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.1049380617285, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 1.8809523809523807, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 1.0457505882352942, 0.0001);


        /**
         * GOING DOWN 1,1
         */

        expectedS = new Double[]{2.0,1.5,0.5,1.625,0.0,2.5};
        expectedP = new Double[]{0.111111,0.333333,0.222222,0.2222222,0.0,0.111111};

        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));

        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.67924528301, 0.001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.1805552486112375, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 8.82685179232406, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 6.153840886153847, 0.0001);


        /**
         * GOING DOWN 1,2
         */

        expectedS = new Double[]{0.0,2.125,0.0,1.0,0.0};
        expectedP = new Double[]{0.0,0.75,0.0,0.25,0.0};

        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));


        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.5423728519390996, 0.001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.2604166666666667, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 0.81944444, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 2.56, 0.0001);


        /**
         * GOING DOWN 2,0
         */
        expectedS = new Double[]{0.0,2.75,0.0,0.5,0.0, 2.5};
        expectedP = new Double[]{0.0,0.5,0.0,0.25,0.0, 0.25};

        gdtmNext = new GTDM(gdtmNext, false);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));
        System.out.println("Maceirz AAAAA");
        gdtmNext.getMatrixA().printf();

//        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew, false);
        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.47058821314879, 0.001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.5856481481481481, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 3.9259259259259256, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 5.91304347826, 0.0001);

    }
}
