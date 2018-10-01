import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

//@RunWith(MockitoJUnitRunner.class)
public class GDTMtest {
    int WIELKSC_MACIRZY = 6;
    int SQUARE_SIZE = 4;
    GTDMNew gdtmNowe;
    GTDMNew gdtmFirst;
    GTDMNew gdtmNext;

    @Test
    public void customOnGDTM() {
        Random rand = new Random();

        double[][] dane = {{1, 2, 5, 1, 4, 4}, {1, 2, 3, 4, 1, 4}, {0, 1, 3, 1, 2, 3},
                {4, 3, 1, 1, 2, 2}, {5, 5, 1, 2, 4, 2}, {2, 3, 1, 5, 4, 1}};
        double[][] matrixAdate = {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, {0.0, 2.0, 2.375, 2.5, 2.875, 0.0}, {0.0, 2.125, 2.0, 2.125, 2.25, 0.0},
                {0.0, 2.5, 2.125, 2.0, 2.125, 0.0}, {0.0, 2.5, 2.625, 2.375, 2.375, 0.0}, {0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};

        int startPointX = 0;
        int startPointY = 0;
        Matrix daneTestowe = new MatrixCommon(dane, WIELKSC_MACIRZY, WIELKSC_MACIRZY, startPointY,startPointX );
        System.out.println("TESTOWE");
        System.out.println(daneTestowe);
        int q = 4;

        /**
         * Tu jest wylicznie punktu startowego czyli pierwszego srodka z kwadratwego regionu
         */


        /**
         *Wpierw to obliczyc normalnie
         */
        gdtmNowe = new GTDMNew(daneTestowe);
        gdtmNowe.startFirstCalcualtions(true, true);
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
         * CREATION OF GDTM class for matrix A calculation
         */
        Matrix daneTestowe = new MatrixCommon(dane, WIELKSC_MACIRZY, WIELKSC_MACIRZY, 0,0 );
        gdtmNowe = new GTDMNew(daneTestowe);
        MatrixCommon matrixA = gdtmNowe.getMatrixA();


        /**
         * CALCULACTIONS OF SWIARES 0,0
         */
        Matrix squareMAtrixData = new MatrixCommon(dane, SQUARE_SIZE ,SQUARE_SIZE,0,0);
        System.out.println("TESTOWE");
        System.out.println(daneTestowe);

        gdtmFirst = new GTDMNew(squareMAtrixData,matrixA);
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




        /**
         * 0,1
         */
        MatrixCommon matrixANextCalculated;
        gdtmNext = new GTDMNew(gdtmFirst, true);
        gdtmNext.startNextCalcualtions(true, false);
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


        /**
         * 0,2
         */

        gdtmNext = new GTDMNew(gdtmNext, true);
        gdtmNext.startNextCalcualtions(true, false);
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




        /**
         * GOING DOWN 1,0
         */
        gdtmNext = new GTDMNew(gdtmNext, false);
        gdtmNext.startFirstCalcualtions(true, false);
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
    }
}
