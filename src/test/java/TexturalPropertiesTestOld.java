import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Na kartce liczone coarness i contrast
 */

public class TexturalPropertiesTestOld {
    static int WIELKSC_MACIRZY = 6;
    int SQUARE_SIZE = 4;
    GTDM gdtmNowe;
    GTDM gdtmFirst;
    GTDM gdtmNext;

    @BeforeAll
    public static void createImage(){
        byte[][] dane = {{1, 2, 5, 1, 4, 4}, {1, 2, 3, 4, 1, 4}, {0, 1, 3, 1, 2, 3},
                {4, 3, 1, 1, 2, 2}, {5, 5, 1, 2, 4, 2}, {2, 3, 1, 5, 4, 1}};
        ImagesCreator.createTestPixelImage(dane,WIELKSC_MACIRZY);
    }

    @Test
    public void customOnGDTM() {
        System.out.println("------------------------------------------------");
        Constans.QUANTIZATION =1 ;

        double[][] matrixAdate = {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 2.0, 2.375, 2.5, 2.875, 0.0}, {0.0, 2.125, 2.0, 2.125, 2.25, 0.0},
                {0.0, 2.5, 2.125, 2.0, 2.125, 0.0}, {0.0, 2.5, 2.625, 2.375, 2.375, 0.0}, {0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};

        File img = new File(Constans.SAVE_PATH + "Test.png");
        BufferedImage buffImage=null;
        try {
            buffImage = ImageIO.read(img);
        }catch (Exception ex){

        }
        Matrix testData = new ImageMatrix(buffImage, ImageMatrix.COLOR.GREY,Constans.SAVE_PATH +"Test.png");
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
        Double[] expectedS;
        Double[] expectedP;

        double[][] expectedMatrixAdate = {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 2.0, 2.375, 2.5, 2.875, 0.0}, {0.0, 2.125, 2.0, 2.125, 2.25, 0.0},
                {0.0, 2.5, 2.125, 2.0, 2.125, 0.0}, {0.0, 2.5, 2.625, 2.375, 2.375, 0.0}, {0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};

        /**
         * CREATION OF TexturalPropertiesTestOld class for matrix A calculation
         */
        File img = new File(Constans.SAVE_PATH + "Test.png");
        BufferedImage buffImage=null;
        try {
            buffImage = ImageIO.read(img);
        }catch (Exception ex){

        }
        TexturalProperties texturalPropertiesNew;

        /**
         * CALCULACTIONS OF SWIARES 0,0
         */
        Matrix daneTestowe = new ImageMatrix(buffImage, ImageMatrix.COLOR.GREY,Constans.SAVE_PATH +"Test.png");
        daneTestowe.setHeight(SQUARE_SIZE);
        daneTestowe.setWidth(SQUARE_SIZE);

        expectedS = new Double[]{0.0,1.125,0.0,1.625,0.0};
        expectedP = new Double[]{0.0,0.25,0.25,0.5,0.0};

        gdtmFirst = new GTDM(daneTestowe,new MatrixCommon(expectedMatrixAdate,WIELKSC_MACIRZY,WIELKSC_MACIRZY));
        gdtmFirst.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmFirst.setS(new ArrayList<>(Arrays.asList(expectedS)));

        texturalPropertiesNew = new TexturalProperties(gdtmFirst);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.91428571428, 0.0001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.15755208333, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 2.28125, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 3.09090909, 0.0001);

        /**
         * 0,1
         */
        expectedS = new Double[]{0.0,1.125,0.0,1.625,1.5};
        expectedP = new Double[]{0.0,0.25,0.0,0.5,0.25};

        gdtmNext = new GTDM(gdtmFirst, true);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));

//        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew,true);
        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.68085106383, 0.0001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.42057291666, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 4.21875, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 3.8823529411764706, 0.0001);

        /**
         * 0,2
         */
        expectedS = new Double[]{0.0,3.0,0.25,0.0,1.5};
        expectedP = new Double[]{0.0,0.5,0.25,0.0,0.25};

        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));

//        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew,true);
        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.51612903225, 0.001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.59375, 0.001);
        assertEquals(texturalPropertiesNew.getComplexity(), 5.66666666, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 4.0, 0.0001);

        /**
         * GOING DOWN 1,0
         */
        expectedS = new Double[]{0.0,2.25,0.0,1.5,0.0};
        expectedP = new Double[]{0.0,0.5,0.0,0.5,0.0};

        gdtmNext = new GTDM(gdtmNext, false);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));

//        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew, false);
        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.5333333333, 0.001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.9375, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 1.875, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 2.1333333333333333, 0.0001);


        /**
         * GOING DOWN 1,1
         */

        expectedS = new Double[]{0.0,3.25,0.0,1.0,0.0};
        expectedP = new Double[]{0.0,0.75,0.0,0.25,0.0};

        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));

//        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew,true);
        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.37209302325, 0.001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.796875, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 2.6875, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 1.8823529411764706, 0.0001);


        /**
         * GOING DOWN 1,2
         */

        expectedS = new Double[]{0.0,2.125,0.0,1.0,0.0};
        expectedP = new Double[]{0.0,0.75,0.0,0.25,0.0};

        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.setP(new ArrayList<>(Arrays.asList(expectedP)));
        gdtmNext.setS(new ArrayList<>(Arrays.asList(expectedS)));


       // texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew,true);
        texturalPropertiesNew = new TexturalProperties(gdtmNext);
//        assertEquals(texturalPropertiesNew.getCoarness(), 0.37209302325, 0.001);
//        assertEquals(texturalPropertiesNew.getContrast(), 0.796875, 0.0001);
//        assertEquals(texturalPropertiesNew.getComplexity(), 2.6875, 0.0001);
//        assertEquals(texturalPropertiesNew.getStrength(), 1.8823529411764706, 0.0001);


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
        assertEquals(texturalPropertiesNew.getCoarness(), 0.4705882330795848, 0.001);
        assertEquals(texturalPropertiesNew.getContrast(), 1.3177083333, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 8.8333333, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 5.91304347826, 0.0001);

    }
}
