import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

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

//@RunWith(MockitoJUnitRunner.class)
public class GDTMTexturalPropertiestestOld {
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
        ArrayList<ImageMatrix> listOfSingleColorImage = new ArrayList<>();
        BufferedImage buffImage=null;
        try {
            buffImage = ImageIO.read(img);
        }catch (Exception ex){

        }
        Matrix daneTestowe = new ImageMatrix(buffImage, ImageMatrix.COLOR.GREY,Constans.SAVE_PATH +"Test.png");
        daneTestowe.printf();

        /**
         *Wpierw to obliczyc normalnie
         */
        gdtmNowe = new GTDM(daneTestowe);
        gdtmNowe.startFirstCalcualtions(true, false);
        MatrixCommon matrixACalculated = gdtmNowe.getMatrixA();
        int d = gdtmNowe.getD();
        for (int i = d; i < WIELKSC_MACIRZY - d; i++) {
            for (int j = d; j < WIELKSC_MACIRZY - d; j++) {
                assertEquals(matrixACalculated.get(i, j), matrixAdate[i][j], 0.01);
            }
        }
        ArrayList<Double> sCalculated = gdtmNowe.getS();
        Double[] expectedS = {0.0,7.875,0.75,2.125,3.125,2.5};
        for (int i = 0; i < expectedS.length; i++){
            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
        }
        ArrayList<Double> pCalculated = gdtmNowe.getP();
        Double[] expectedP = {0.0,0.375,0.25,0.1875,0.125,0.0625};
        assertEquals(Arrays.asList(expectedP).stream().mapToDouble(s->s).sum(),1,0.01);
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }

    }

    @Test
    public void customGDTMOfSquareSize() {
        Constans.QUANTIZATION =1 ;
        Random rand = new Random();
        int d;
        ArrayList<Double> sCalculated;
        Double[] expectedS;
        ArrayList<Double> pCalculated;
        Double[] expectedP;

        double[][] dane = {{1, 2, 5, 1, 4, 4}, {1, 2, 3, 4, 1, 4}, {0, 1, 3, 1, 2, 3},
                {4, 3, 1, 1, 2, 2}, {5, 5, 1, 2, 4, 2}, {2, 3, 1, 5, 4, 1}};
        double[][] expectedMatrixAdate = {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 2.0, 2.375, 2.5, 2.875, 0.0}, {0.0, 2.125, 2.0, 2.125, 2.25, 0.0},
                {0.0, 2.5, 2.125, 2.0, 2.125, 0.0}, {0.0, 2.5, 2.625, 2.375, 2.375, 0.0}, {0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};

        /**
         * CREATION OF TexturalPropertiesTestOld class for matrix A calculation
         */
        Matrix daneTestowe = new MatrixCommon(dane, WIELKSC_MACIRZY, WIELKSC_MACIRZY, 0,0 );
        gdtmNowe = new GTDM(daneTestowe);
        MatrixCommon matrixA = gdtmNowe.getMatrixA();
        TexturalProperties texturalPropertiesNew;


        /**
         * CALCULACTIONS OF SWIARES 0,0
         */
        Matrix squareMAtrixData = new MatrixCommon(dane, SQUARE_SIZE ,SQUARE_SIZE,0,0);
        System.out.println(daneTestowe);
        System.out.println(matrixA);
        gdtmFirst = new GTDM(squareMAtrixData,matrixA);
        gdtmFirst.startFirstCalcualtions(true, false);
        MatrixCommon matrixACalculated = gdtmFirst.getMatrixA();
        d = gdtmFirst.getD();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixACalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
        sCalculated = gdtmFirst.getS();
        expectedS = new Double[]{0.0,1.125,0.0,1.625,0.0};
        for (int i = 0; i < expectedS.length; i++){
            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
        }
        pCalculated = gdtmFirst.getP();
        expectedP = new Double[]{0.0,0.25,0.25,0.5,0.0};
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }
        texturalPropertiesNew = new TexturalProperties(gdtmFirst);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.91428571428, 0.0001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.15755208333, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 2.28125, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 3.09090909, 0.0001);


        /**
         * 0,1
         */
        MatrixCommon matrixANextCalculated;
        gdtmNext = new GTDM(gdtmFirst, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        int startY = gdtmNext.getInputDataMatrix().getStartHeight();
        int startX = gdtmNext.getInputDataMatrix().getStartWidth();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
        sCalculated = gdtmNext.getS();
        expectedS = new Double[]{0.0,1.125,0.0,1.625,1.5};
        for (int i = 0; i < expectedS.length; i++){
            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
        }
        pCalculated = gdtmNext.getP();
        expectedP = new Double[]{0.0,0.25,0.0,0.5,0.25};
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }
//        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew,true);
        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.68085106383, 0.0001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.42057291666, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 4.21875, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 3.8823529411764706, 0.0001);

        /**
         * 0,2
         */

        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        startY = gdtmNext.getInputDataMatrix().getStartHeight();
        startX = gdtmNext.getInputDataMatrix().getStartWidth();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
        sCalculated = gdtmNext.getS();
        expectedS = new Double[]{0.0,3.0,0.25,0.0,1.5};
        for (int i = 0; i < expectedS.length; i++){
            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
        }
        pCalculated = gdtmNext.getP();
        expectedP = new Double[]{0.0,0.5,0.25,0.0,0.25};
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }
//        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew,true);
        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.51612903225, 0.001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.59375, 0.001);
        assertEquals(texturalPropertiesNew.getComplexity(), 5.66666666, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 4.0, 0.0001);

        /**
         * GOING DOWN 1,0
         */
        gdtmNext = new GTDM(gdtmNext, false);
        gdtmNext.startNextRowCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
         startY = gdtmNext.getInputDataMatrix().getStartHeight();
         startX = gdtmNext.getInputDataMatrix().getStartWidth();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
        sCalculated = gdtmNext.getS();
        expectedS = new Double[]{0.0,2.25,0.0,1.5,0.0};
        for (int i = 0; i < expectedS.length; i++){
            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
        }
        pCalculated = gdtmNext.getP();
        expectedP = new Double[]{0.0,0.5,0.0,0.5,0.0};
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }
//        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew, false);
        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.5333333333, 0.001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.9375, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 1.875, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 2.1333333333333333, 0.0001);


        /**
         * GOING DOWN 1,1
         */
        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        startY = gdtmNext.getInputDataMatrix().getStartHeight();
        startX = gdtmNext.getInputDataMatrix().getStartWidth();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
        sCalculated = gdtmNext.getS();
        expectedS = new Double[]{0.0,3.25,0.0,1.0,0.0};
        for (int i = 0; i < expectedS.length; i++){
            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
        }
        pCalculated = gdtmNext.getP();
        expectedP = new Double[]{0.0,0.75,0.0,0.25,0.0};
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }
//        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew,true);
        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.37209302325, 0.001);
        assertEquals(texturalPropertiesNew.getContrast(), 0.796875, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 2.6875, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 1.8823529411764706, 0.0001);


        /**
         * GOING DOWN 1,2
         */
        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        startY = gdtmNext.getInputDataMatrix().getStartHeight();
        startX = gdtmNext.getInputDataMatrix().getStartWidth();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
//        sCalculated = gdtmNext.getS();
//        expectedS = new Double[]{0.0,2.125,0.0,1.0,0.0};
//        for (int i = 0; i < expectedS.length; i++){
//            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
//        }
//        pCalculated = gdtmNext.getP();
//        expectedP = new Double[]{0.0,0.75,0.0,0.25,0.0};
//        for (int i = 0; i < expectedP.length; i++){
//            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
//        }
//        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew,true);
        texturalPropertiesNew = new TexturalProperties(gdtmNext);
//        assertEquals(texturalPropertiesNew.getCoarness(), 0.37209302325, 0.001);
//        assertEquals(texturalPropertiesNew.getContrast(), 0.796875, 0.0001);
//        assertEquals(texturalPropertiesNew.getComplexity(), 2.6875, 0.0001);
//        assertEquals(texturalPropertiesNew.getStrength(), 1.8823529411764706, 0.0001);


        /**
         * GOING DOWN 2,0
         */
        gdtmNext = new GTDM(gdtmNext, false);
        gdtmNext.startNextRowCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        startY = gdtmNext.getInputDataMatrix().getStartHeight();
        startX = gdtmNext.getInputDataMatrix().getStartWidth();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
        sCalculated = gdtmNext.getS();
        expectedS = new Double[]{0.0,2.75,0.0,0.5,0.0, 2.5};
        for (int i = 0; i < expectedS.length; i++){
            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
        }
        pCalculated = gdtmNext.getP();
        expectedP = new Double[]{0.0,0.5,0.0,0.25,0.0, 0.25};
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }
//        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew, false);
        texturalPropertiesNew = new TexturalProperties(gdtmNext);
        assertEquals(texturalPropertiesNew.getCoarness(), 0.4705882330795848, 0.001);
        assertEquals(texturalPropertiesNew.getContrast(), 1.3177083333, 0.0001);
        assertEquals(texturalPropertiesNew.getComplexity(), 8.8333333, 0.0001);
        assertEquals(texturalPropertiesNew.getStrength(), 5.91304347826, 0.0001);

    }
}
