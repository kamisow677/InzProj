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

public class GTDMtestNew {
    static int MATRIX_SIZE = 6;
    int SQUARE_SIZE = 5;
    GTDM gdtmNowe;
    GTDM gdtmFirst;
    GTDM gdtmNext;

    @BeforeAll
    public static void createImage(){
        byte[][] dane = {{1, 2, 5, 1, 4, 4}, {1, 2, 3, 4, 1, 4}, {0, 1, 3, 1, 2, 3},
                {4, 3, 1, 1, 2, 2}, {5, 5, 1, 2, 4, 2}, {2, 3, 1, 5, 4, 1}};
        ImagesCreator.createTestPixelImage(dane, MATRIX_SIZE);
    }

//    @Test
//    public void customOnGDTM() {
//        System.out.println("------------------------------------------------");
//        Constans.QUANTIZATION =1 ;
//
//        double[][] matrixAdate = {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 2.0, 2.375, 2.5, 2.875, 0.0}, {0.0, 2.125, 2.0, 2.125, 2.25, 0.0},
//                {0.0, 2.5, 2.125, 2.0, 2.125, 0.0}, {0.0, 2.5, 2.625, 2.375, 2.375, 0.0}, {0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};
//
//        File img = new File(Constans.SAVE_PATH + "Test.png");
//        BufferedImage buffImage=null;
//        try {
//            buffImage = ImageIO.read(img);
//        }catch (Exception ex){
//
//        }
//        Matrix daneTestowe = new ImageMatrix(buffImage, ImageMatrix.COLOR.GREY,Constans.SAVE_PATH +"Test.png");
//        daneTestowe.printf();
//
//        /**
//         *Wpierw to obliczyc normalnie
//         */
//        gdtmNowe = new GTDM(daneTestowe);
//        gdtmNowe.startFirstCalcualtions(true, false);
//        MatrixCommon matrixACalculated = gdtmNowe.getMatrixA();
//        int d = gdtmNowe.getD();
//        for (int i = d; i < MATRIX_SIZE - d; i++) {
//            for (int j = d; j < MATRIX_SIZE - d; j++) {
//                assertEquals(matrixACalculated.get(i, j), matrixAdate[i][j], 0.01);
//            }
//        }
//        ArrayList<Double> sCalculated = gdtmNowe.getS();
//        Double[] expectedS = {0.0,7.875,0.75,2.125,3.125,2.5};
//        for (int i = 0; i < expectedS.length; i++){
//            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
//        }
//        ArrayList<Double> pCalculated = gdtmNowe.getP();
//        Double[] expectedP = {0.0,0.375,0.25,0.1875,0.125,0.0625};
//        assertEquals(Arrays.asList(expectedP).stream().mapToDouble(s->s).sum(),1,0.01);
//        for (int i = 0; i < expectedP.length; i++){
//            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
//        }
//
//    }

    @Test
    public void customGDTMOfSquareSize() {
        Constans.QUANTIZATION =1 ;
        Constans.QUADRATIC_SIZE=5;
        int d;
        ArrayList<Double> sCalculated;
        Double[] expectedS;
        ArrayList<Double> pCalculated;
        Double[] expectedP;


        double[][] expectedMatrixAdate = {{1.375, 2.5, 2.5, 3.375, 2.875, 3.625}, {1.0, 2.0, 2.375, 2.5, 2.875, 3.125}, {2.0, 2.125, 2.0, 2.125, 2.25, 2.5},
                {2.875, 2.5, 2.125, 2.0, 2.125, 2.5}, {3.5, 2.5, 2.625, 2.375, 2.375, 2.25}, {3.375, 2.5, 3.125, 2.75, 3.0, 2.375}};


        File img = new File(Constans.SAVE_PATH + "Test.png");
        BufferedImage buffImage=null;
        try {
            buffImage = ImageIO.read(img);
        }catch (Exception ex){

        }
        /**
         * CREATION OF TexturalPropertiesTest class for matrix A calculation
         */
        Matrix daneTestowe = new ImageMatrix(buffImage, ImageMatrix.COLOR.GREY,Constans.SAVE_PATH +"Test.png");
        gdtmNowe = new GTDM(daneTestowe);
        MatrixCommon matrixA = gdtmNowe.getMatrixA();
        TexturalProperties texturalPropertiesNew;


        /**
         * CALCULACTIONS OF SWIARES 0,0
         */
        Matrix squareMAtrixData = new ImageMatrix(buffImage, ImageMatrix.COLOR.GREY,Constans.SAVE_PATH +"Test.png");
        squareMAtrixData.setHeight(SQUARE_SIZE);
        squareMAtrixData.setWidth(SQUARE_SIZE);
        System.out.println(daneTestowe);
        System.out.println(matrixA);
        gdtmFirst = new GTDM(squareMAtrixData,matrixA);
        gdtmFirst.startFirstCalcualtions(true, false);
        MatrixCommon matrixACalculated = gdtmFirst.getMatrixA();
        d = gdtmFirst.getD();
        System.out.println(matrixACalculated);
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixACalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
        sCalculated = gdtmFirst.getS();
        expectedS = new Double[]{0.0,1.5,1.0,0.0,0.0};
        for (int i = 0; i < expectedS.length; i++){
            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
        }
        pCalculated = gdtmFirst.getP();
        expectedP = new Double[]{0.0,0.666666,0.333333,0.0,0.0};
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }

        /**
         * 0,1
         */
        MatrixCommon matrixANextCalculated;
        gdtmNext = new GTDM(gdtmFirst, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
        sCalculated = gdtmNext.getS();
        expectedS = new Double[]{0.0,0.75,1.0,0.625,0.0,5.0};
        for (int i = 0; i < expectedS.length; i++){
            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
        }
        pCalculated = gdtmNext.getP();
        expectedP = new Double[]{0.0,0.333333,0.333333,0.1111111,0.0,0.222222};
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }

        /**
         * 0,2
         */
        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
//        sCalculated = gdtmNext.getS();
//        expectedS = new Double[]{0.0,3.0,0.25,0.0,1.5};
//        for (int i = 0; i < expectedS.length; i++){
//            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
//        }
//        pCalculated = gdtmNext.getP();
//        expectedP = new Double[]{0.0,0.5,0.25,0.0,0.25};
//        for (int i = 0; i < expectedP.length; i++){
//            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
//        }
//
        /**
         * 0,3
         */
        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }

        /**
         * 0,4
         */
        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }

        /**
         * 0,5
         */
        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }


        /**
         * GOING DOWN 1,0
         */
        gdtmNext = new GTDM(gdtmNext, false);
        gdtmNext.startNextRowCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
        sCalculated = gdtmNext.getS();
        expectedS = new Double[]{4.0,1.875,0.5,0.0,0.0};
        for (int i = 0; i < expectedS.length; i++){
            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
        }
        pCalculated = gdtmNext.getP();
        expectedP = new Double[]{0.222222,0.555555,0.222222,0.0,0.0};
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }

//
//
        /**
         * GOING DOWN 1,1
         */
        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
//        sCalculated = gdtmNext.getS();
//        expectedS = new Double[]{0.0,3.25,0.0,1.0,0.0};
//        for (int i = 0; i < expectedS.length; i++){
//            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
//        }
        pCalculated = gdtmNext.getP();
        expectedP = new Double[]{0.111111,0.333333,0.222222,0.2222222,0.0,0.111111};
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }
//
        /**
         * GOING DOWN 1,2
         */
        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
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
        pCalculated = gdtmNext.getP();
        expectedP = new Double[]{0.0,0.333333,0.222222,0.222222,0.111111, 0.11111};
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }

        /**
         * GOING DOWN 1,3
         */
        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
        /**
         * GOING DOWN 1,4
         */
        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }
        /**
         * GOING DOWN 1,5
         */
        gdtmNext = new GTDM(gdtmNext, true);
        gdtmNext.startNextColumnCalcualtions(true, false);
        matrixANextCalculated = gdtmNext.getMatrixA();
        d = gdtmNext.getD();
        for (int i = d; i < SQUARE_SIZE - d; i++) {
            for (int j = d; j < SQUARE_SIZE - d; j++) {
                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
            }
        }

        pCalculated = gdtmNext.getP();
        expectedP = new Double[]{0.0,0.111111,0.111111,0.2222222,0.555555,0.0};
        for (int i = 0; i < expectedP.length; i++){
            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
        }


        /**
         * GOING DOWN 2,0
         */
//        gdtmNext = new GTDM(gdtmNext, false);
//        gdtmNext.startNextRowCalcualtions(true, false);
//        matrixANextCalculated = gdtmNext.getMatrixA();
//        d = gdtmNext.getD();
//        for (int i = d; i < SQUARE_SIZE - d; i++) {
//            for (int j = d; j < SQUARE_SIZE - d; j++) {
//                assertEquals(matrixANextCalculated.get(i, j), expectedMatrixAdate[i][j], 0.01);
//            }
//        }
//        sCalculated = gdtmNext.getS();
//        expectedS = new Double[]{0.0,2.75,0.0,0.5,0.0, 2.5};
//        for (int i = 0; i < expectedS.length; i++){
//            assertEquals(sCalculated.get(i), expectedS[i], 0.01);
//        }
//        pCalculated = gdtmNext.getP();
//        expectedP = new Double[]{0.0,0.5,0.0,0.25,0.0, 0.25};
//        for (int i = 0; i < expectedP.length; i++){
//            assertEquals(pCalculated.get(i), expectedP[i], 0.01);
//        }
    }
}
