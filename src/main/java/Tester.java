import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;

public class Tester {

    ArrayList<String> listOfPathsToImagePlusName = new ArrayList<>();
    ArrayList<ArrayList<ImageMatrix>> listOfMatrixData = new ArrayList<>();

    public void run() {
        System.out.println("D: " + Constans.getD());
        System.out.println("Quadratic: " + Constans.getQuadraticSize());
        System.out.println("average MAtrixes?: " + Constans.isAverageMatrixes());

        try {
            final File folder = new File(Constans.FOLDER_PATH);
            listFilesForFolder(folder);
            imagePathToMatrix();
            ArrayList<TexturalProperties> tex = new ArrayList<>();
            ArrayList<Map<String, Double>> properties = new ArrayList<>();//final props all to write image
            ArrayList<MatrixCommon> matrixesA = new ArrayList<>();
            GTDM gdtmNext = null;
            ArrayList<GTDM> listaGDTMOWNext = new ArrayList<>();
            long startTime;
            long stopTime;
            long elapsedTime;
            long calc1 = 0;
            long calc2 = 0;


            /**
             * Now i only take one matrix
             */
            ImageMatrix matrix = listOfMatrixData.get(0).get(0);
            int q = Constans.QUADRATIC_SIZE;
            int w = matrix.getWidth();
            int h = matrix.getHeight();
            matrix.setStartHeight(0);
            matrix.setStartWidth(0);
            GTDM gdtmNowe = new GTDM(matrix);
            gdtmNowe.startFirstCalcualtions(true, false);
            gdtmNowe.saveToCSV("");


            TexturalProperties texturalPropertiesNew = null;
            ArrayList<Callable<ArrayList<Map<String, Double>>>> result = new ArrayList<>();


            startTime = System.currentTimeMillis();
            for (ArrayList<ImageMatrix> list : listOfMatrixData) {
                w =  list.get(0).getWidth();
                h =  list.get(0).getHeight();
                for (ImageMatrix l : list) {
                    l.setStartHeight(0);
                    l.setStartWidth(0);
                    gdtmNowe = new GTDM(l);
                    gdtmNowe.setD(Constans.D);
                    gdtmNowe.startFirstCalcualtions(true, false);
                    matrixesA.add(gdtmNowe.getMatrixA());
                    l.setHeight(Constans.QUADRATIC_SIZE);
                    l.setWidth(Constans.QUADRATIC_SIZE);
                }
                if (Constans.AVERAGE_MATRIXES) {
                    for (int i = q / 2; i < h - q / 2; i++) {
                        for (int j = q / 2; j < w - q / 2; j++) {
                            if (i == q / 2 && j == q / 2) {
                                /**
                                 * TO JEST TEN FIRST Z TESTU
                                 */

                                int k = 0;
                                for (ImageMatrix l : list) {
                                    gdtmNext = new GTDM(l, matrixesA.get(k));
                                    gdtmNext.setD(Constans.D);
                                    gdtmNext.startFirstCalcualtions(true, false);
                                    listaGDTMOWNext.add(gdtmNext);
                                    k++;
                                }
                                gdtmNext = new GTDM(listaGDTMOWNext.get(0), listaGDTMOWNext.get(1), listaGDTMOWNext.get(2));
                                texturalPropertiesNew = new TexturalProperties(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();

                            } else if (j == q / 2) {

                                for (int k = 0 ; k < Constans.NUMBER_OF_COLORS ; k++){
                                    listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), false));
                                    listaGDTMOWNext.get(k).startNextRowCalcualtions(true, false);
                                }
                                gdtmNext = new GTDM(listaGDTMOWNext.get(0), listaGDTMOWNext.get(1), listaGDTMOWNext.get(2));

                                texturalPropertiesNew = new TexturalProperties(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();

                            } else {
                                for (int k = 0 ; k < Constans.NUMBER_OF_COLORS ; k++){
                                    listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), true));
                                    listaGDTMOWNext.get(k).startNextColumnCalcualtions(true, false);
                                }
                                gdtmNext = new GTDM(listaGDTMOWNext.get(0), listaGDTMOWNext.get(1), listaGDTMOWNext.get(2));

                                texturalPropertiesNew = new TexturalProperties(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();
                            }
                            System.out.println("i:" + i + " j:" + j);
                        }
                    }
                } else {
                    for (int i = q / 2; i < h - q / 2; i++) {
                        for (int j = q / 2; j < w - q / 2; j++) {
                            if (i == q / 2 && j == q / 2) {
                                /**
                                 * TO JEST TEN FIRST Z TESTU
                                 */

                                int k = 0;
                                for (ImageMatrix l : list) {
                                    gdtmNext = new GTDM(l, matrixesA.get(k));
                                    gdtmNext.setD(Constans.D);
                                    gdtmNext.startFirstCalcualtions(true, false);
                                    listaGDTMOWNext.add(gdtmNext);
                                    tex.add(new TexturalProperties(gdtmNext));
                                    k++;
                                }

                                texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();
                            } else if (j == q / 2) {

                                for (int k = 0 ; k < Constans.NUMBER_OF_COLORS ; k++){
                                    listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), false));
                                    listaGDTMOWNext.get(k).startNextRowCalcualtions(true, false);
                                    tex.add(new TexturalProperties(listaGDTMOWNext.get(k)));
                                }

                                texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();
                            } else {

                                for (int k = 0 ; k < Constans.NUMBER_OF_COLORS ; k++){
                                    listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), true));
                                    listaGDTMOWNext.get(k).startNextColumnCalcualtions(true, false);
                                    tex.add(new TexturalProperties(listaGDTMOWNext.get(k)));
                                }
                                texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();
                            }
                            System.out.println("i:" + i + " j:" + j);
                        }
                    }
                }

                ImagesCreator.createPixelImage(properties, h, w, list.get(0).getImageName());
                properties.clear();
                listaGDTMOWNext.clear();
                matrixesA.clear();
            }
            stopTime = System.currentTimeMillis();
            elapsedTime = stopTime - startTime;
            calc1 += elapsedTime;
            System.out.println("CALC DONE");
            System.out.println("CALC1: " + calc1);
            System.out.println("CALC2: " + calc2);
            System.out.println("a: " + Constans.a);
            System.out.println("b: " + Constans.b);
            calc1 = 0;
            calc2 = 0;
        } catch (Exception ex) {

        }
    }

    private void showOld(ImageMatrix matrix, MatrixCommon matrixA, int q, int w, int h) {
        long startTime = System.currentTimeMillis();
        GTDM gtdmNowe = null;
        for (int i = q / 2; i < h - q / 2; i++) {
            for (int j = q / 2; j < w - q / 2; j++) {
                gtdmNowe = new GTDM(matrix, matrixA);
                gtdmNowe.startFirstCalcualtions(true, false);
            }
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("STARE CALC 1" + elapsedTime);
    }

    private void calculationOfSquareRegion() {
        //for (int i=0; i<)
    }

//    private TexturalProperties calculationsBasedOnSquareSize(ImageMatrix imageMatrix, int measure) {
//        int height = imageMatrix.getHeight();
//        int weight = imageMatrix.getWidth();
//        ArrayList<TexturalProperties> tex = new ArrayList<>();
//        for (int i=0; i< height/measure + 1 ; i++){
//            for (int j=0; j< weight/measure + 1; j++){
//                ImageMatrix m = new ImageMatrix(imageMatrix);
//                m.setStartHeight(measure*i);
//                m.setStartWidth(measure*j);
//                if (i == height/measure)
//                    m.setHeight(height - (height/measure)*measure);
//                else
//                    m.setHeight(measure);
//                if (j == weight/measure)
//                    m.setWidth(weight - (weight/measure)*measure);
//                else
//                    m.setWidth(measure);
//                GTDM gdtmNowe = new GTDM(m);
//                gdtmNowe.startCalcualtions(true);
//                TexturalProperties texturalPropertiesNew = new TexturalProperties(gdtmNowe);
//                String str = Integer.toString (i*(weight/measure +1)+j);
//                texturalPropertiesNew.saveToCsv(str);
//                tex.add(texturalPropertiesNew);
//            }
//        }
//        return Transformer.averageProperties(tex,tex.get(0).getColor());
//    }
//
//    private TexturalProperties calculationsBasedOnSquareSize2(ImageMatrix imageMatrix, int measure) {
//        int height = imageMatrix.getHeight();
//        int weight = imageMatrix.getWidth();
//        ArrayList<GTDM> tex = new ArrayList<>();
//        for (int i=0; i< height/measure + 1 ; i++){
//            for (int j=0; j< weight/measure + 1; j++){
//                ImageMatrix m = new ImageMatrix(imageMatrix);
//                m.setStartHeight(measure*i);
//                m.setStartWidth(measure*j);
//                if (i == height/measure)
//                    m.setHeight(height - (height/measure)*measure);
//                else
//                    m.setHeight(measure);
//                if (j == weight/measure)
//                    m.setWidth(weight - (weight/measure)*measure);
//                else
//                    m.setWidth(measure);
//                GTDM gdtmNowe = new GTDM(m);
//                gdtmNowe.startCalcualtions(false);
//                tex.add(gdtmNowe);
//            }
//        }
//        return new TexturalProperties(new GTDM(tex, imageMatrix.getHeight(), imageMatrix.getWidth()));
//    }

    /**
     * Transform path to new Matrix of data
     */
    private void imagePathToMatrix() {
        for (String pathToImagePlusName : listOfPathsToImagePlusName) {
            File img = new File(pathToImagePlusName);
            BufferedImage buffImage = null;
            try {
                buffImage = ImageIO.read(img);
//                buffImage.ty
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<ImageMatrix> listOfSingleColorImage = new ArrayList<>();
            listOfSingleColorImage.add(new ImageMatrix(buffImage, ImageMatrix.COLOR.BLUE, pathToImagePlusName));
            listOfSingleColorImage.add(new ImageMatrix(buffImage, ImageMatrix.COLOR.RED, pathToImagePlusName));
            listOfSingleColorImage.add(new ImageMatrix(buffImage, ImageMatrix.COLOR.GREEN, pathToImagePlusName));
            listOfMatrixData.add(listOfSingleColorImage);
        }
    }

    /**
     * Creates list of images paths + its name
     *
     * @param folder
     */
    public void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                String fullPathWithNameOfImage = Constans.FOLDER_PATH + fileEntry.getName();
                if (fullPathWithNameOfImage.endsWith(".jpg")) {
                    listOfPathsToImagePlusName.add(Constans.FOLDER_PATH + fileEntry.getName());
                    System.out.println(Constans.FOLDER_PATH + fileEntry.getName());
                }
            }
        }
    }

    public void oldSquareCalc() {
        //            if (!concurrent) {
//                 startTime = System.currentTimeMillis();
//                for (ArrayList<ImageMatrix> list : listOfMatrixData) {
//                    tex = new ArrayList<>();
//                    for (ImageMatrix matrix : list) {
//                        TexturalProperties texturalPropertiesNew;
//                        if (Constans.isAverageMatrixes())
//                            texturalPropertiesNew = calculationsBasedOnSquareSize2(matrix, Constans.getQuadraticSize());
//                        else
//                            texturalPropertiesNew = calculationsBasedOnSquareSize(matrix, Constans.getQuadraticSize());
////                    GTDM gdtmNowe = new GTDM(matrix);
////                    gdtmNowe.startCalcualtions(true);
////                    TexturalProperties texturalPropertiesNew = new TexturalProperties(gdtmNowe);
//                        //texturalPropertiesNew.saveToCsv("partNONE");
//                        tex.add(texturalPropertiesNew);
//                    }
//                    Transformer.averageProperties(tex.get(0), tex.get(1), tex.get(2));
//                }
//                long stopTime = System.currentTimeMillis();
//                long elapsedTime = stopTime - startTime;
//                System.out.println("Elapsed time" + elapsedTime);
//
//            }
//            else {
//                startTime = System.currentTimeMillis();
//                Consumer<? super ArrayList<ImageMatrix>> consumer = (array) -> {
//                    final ArrayList<TexturalProperties> finalTex = new ArrayList<>();
//                    for (ImageMatrix matrix : array) {
//                        TexturalProperties texturalPropertiesNew;
//                        if (Constans.isAverageMatrixes())
//                            texturalPropertiesNew = calculationsBasedOnSquareSize2(matrix, Constans.getQuadraticSize());
//                        else
//                            texturalPropertiesNew = calculationsBasedOnSquareSize(matrix, Constans.getQuadraticSize());
//                        finalTex.add(texturalPropertiesNew);
//                    }
//                    Transformer.averageProperties(finalTex.get(0), finalTex.get(1), finalTex.get(2));
//                };
//
//                listOfMatrixData
//                        .parallelStream()
//                        .forEach(consumer);
//
//                long stopTime = System.currentTimeMillis();
//                long elapsedTime = stopTime - startTime;
//                System.out.println(elapsedTime);
//            }
    }


//    public void oldWithoutConcurrent(int startRow, int h, int q, int w
//            , ArrayList<ImageMatrix> list, ArrayList<MatrixCommon> matrixesA) {
//
//    for (int i = q / 2; i < h - q / 2; i++) {
//            for (int j = q / 2; j < w - q / 2; j++) {
//
//            if (i == q / 2 && j == q / 2) {
//            /**
//             * TO JEST TEN FIRST Z TESTU
//             */
//
//            startTime = System.currentTimeMillis();
//            gdtmNext = new GTDM(matrix, matrixA);
//            gdtmNext.startFirstCalcualtions(true, false);
//            stopTime = System.currentTimeMillis();
//            elapsedTime = stopTime - startTime;
//            calc1 += elapsedTime;
//
//            startTime = System.currentTimeMillis();
//            texturalPropertiesNew = new TexturalProperties(gdtmNext);
//            properties.add(texturalPropertiesNew.getProps());
//            stopTime = System.currentTimeMillis();
//            elapsedTime = stopTime - startTime;
//            calc2 += elapsedTime;
//            } else if (j == q / 2) {
//            startTime = System.currentTimeMillis();
//            gdtmNext = new GTDM(gdtmNext, false);
//            gdtmNext.startNextRowCalcualtions(true, false);
//            stopTime = System.currentTimeMillis();
//            elapsedTime = stopTime - startTime;
//            calc1 += elapsedTime;
//
//            startTime = System.currentTimeMillis();
//    //                        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew,false);
//            texturalPropertiesNew = new TexturalProperties(gdtmNext);
//            properties.add(texturalPropertiesNew.getProps());
//            stopTime = System.currentTimeMillis();
//            elapsedTime = stopTime - startTime;
//            calc2 += elapsedTime;
//
//            } else {
//            startTime = System.currentTimeMillis();
//            gdtmNext = new GTDM(gdtmNext, true);
//            gdtmNext.startNextColumnCalcualtions(true, false);
//            stopTime = System.currentTimeMillis();
//            elapsedTime = stopTime - startTime;
//            calc1 += elapsedTime;
//
//            startTime = System.currentTimeMillis();
//    //                        texturalPropertiesNew = new TexturalProperties(gdtmNext,texturalPropertiesNew,true);
//            texturalPropertiesNew = new TexturalProperties(gdtmNext);
//            properties.add(texturalPropertiesNew.getProps());
//            stopTime = System.currentTimeMillis();
//            elapsedTime = stopTime - startTime;
//            calc2 += elapsedTime;
//
//
//            }
//            //System.out.println("i:" + i + " j:"+j);
//            }
//        }
//    }
}