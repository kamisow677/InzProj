import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Tester {

    ArrayList<String> listOfPathsToImagePlusName = new ArrayList<>();
    ArrayList<ArrayList<ImageMatrix>> listOfMatrixData = new ArrayList<>();
    String FOLDER_PATH = "C:\\Users\\Kamil Sowa\\Desktop\\obrazki21\\";


    public void run(){

        System.out.println("D: "+Constans.getD());
        System.out.println("Quadratic: "+Constans.getQuadraticSize());
        System.out.println("average MAtrixes?: "+Constans.isAverageMatrixes());

        try {
            final File folder = new File(FOLDER_PATH);
            listFilesForFolder(folder);
            imagePathToMatrix();
            ArrayList<TexturalPropertiesNew> tex = new ArrayList<>();
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

            GTDMNew gdtmNowe = new GTDMNew(matrix);
            gdtmNowe.startFirstCalcualtions(true,false);
            gdtmNowe.saveToCSV("");

            MatrixCommon matrixA = gdtmNowe.getMatrixA();
            GTDMNew gdtmNext = null;
            ArrayList<GTDMNew> listaGDTMOWNext = new ArrayList<>();


            matrix.setHeight(Constans.QUADRATIC_SIZE);
            matrix.setWidth(Constans.QUADRATIC_SIZE);

            TexturalPropertiesNew texturalPropertiesNew = null;
            ArrayList<Map<String,Double>> properties = new ArrayList<>();

            //showOld(matrix,matrixA,q,w,h);

            for ( ArrayList<ImageMatrix>  list : listOfMatrixData ) {
                for (ImageMatrix l : list){
                    l.setStartHeight(0);
                    l.setStartWidth(0);
                    l.setHeight(Constans.QUADRATIC_SIZE);
                    l.setWidth(Constans.QUADRATIC_SIZE);
                }
                if (Constans.AVERAGE_MATRIXES){
                    for (int i = q / 2; i < h - q / 2; i++) {
                        for (int j = q / 2; j < w - q / 2; j++) {
                            if (i == q / 2 && j == q / 2) {
                                /**
                                 * TO JEST TEN FIRST Z TESTU
                                 */

                                startTime = System.currentTimeMillis();
                                for (ImageMatrix l : list){
                                    gdtmNext = new GTDMNew(l, matrixA);
                                    gdtmNext.startFirstCalcualtions(true, false);
                                    listaGDTMOWNext.add(gdtmNext);
                                }
                                gdtmNext = new GTDMNew(listaGDTMOWNext.get(0),listaGDTMOWNext.get(1),listaGDTMOWNext.get(2));

                                stopTime = System.currentTimeMillis();
                                elapsedTime = stopTime - startTime;
                                calc1 += elapsedTime;


                                startTime = System.currentTimeMillis();
                                texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                stopTime = System.currentTimeMillis();
                                elapsedTime = stopTime - startTime;
                                calc2 += elapsedTime;
                                tex.clear();
                            } else if (j == q / 2) {
                                startTime = System.currentTimeMillis();

                                listaGDTMOWNext.set(0, new GTDMNew(listaGDTMOWNext.get(0), false));
                                listaGDTMOWNext.set(1, new GTDMNew(listaGDTMOWNext.get(1), false));
                                listaGDTMOWNext.set(2, new GTDMNew(listaGDTMOWNext.get(2), false));

                                listaGDTMOWNext.get(0).startNextRowCalcualtions(true, false);
                                listaGDTMOWNext.get(1).startNextRowCalcualtions(true, false);
                                listaGDTMOWNext.get(2).startNextRowCalcualtions(true, false);

                                gdtmNext = new GTDMNew(listaGDTMOWNext.get(0),listaGDTMOWNext.get(1),listaGDTMOWNext.get(2));

                                stopTime = System.currentTimeMillis();
                                elapsedTime = stopTime - startTime;
                                calc1 += elapsedTime;

                                startTime = System.currentTimeMillis();
//                        texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext,texturalPropertiesNew,false);
                                texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                stopTime = System.currentTimeMillis();
                                elapsedTime = stopTime - startTime;
                                calc2 += elapsedTime;
                                tex.clear();

                            } else {
                                startTime = System.currentTimeMillis();

                                listaGDTMOWNext.set(0, new GTDMNew(listaGDTMOWNext.get(0), true));
                                listaGDTMOWNext.set(1, new GTDMNew(listaGDTMOWNext.get(1), true));
                                listaGDTMOWNext.set(2, new GTDMNew(listaGDTMOWNext.get(2), true));

                                listaGDTMOWNext.get(0).startNextColumnCalcualtions(true, false);
                                listaGDTMOWNext.get(1).startNextColumnCalcualtions(true, false);
                                listaGDTMOWNext.get(2).startNextColumnCalcualtions(true, false);

                                gdtmNext = new GTDMNew(listaGDTMOWNext.get(0),listaGDTMOWNext.get(1),listaGDTMOWNext.get(2));
                                stopTime = System.currentTimeMillis();
                                elapsedTime = stopTime - startTime;
                                calc1 += elapsedTime;

                                startTime = System.currentTimeMillis();
//                        texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext,texturalPropertiesNew,true);
                                texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                stopTime = System.currentTimeMillis();
                                elapsedTime = stopTime - startTime;
                                calc2 += elapsedTime;
                                tex.clear();

                            }
                            System.out.println("i:" + i + " j:"+j);
                        }
                    }
                    int a =2;
                } else {
                    for (int i = q / 2; i < h - q / 2; i++) {
                        for (int j = q / 2; j < w - q / 2; j++) {
                            if (i == q / 2 && j == q / 2) {
                                /**
                                 * TO JEST TEN FIRST Z TESTU
                                 */
                                startTime = System.currentTimeMillis();
                                for (ImageMatrix l : list) {
                                    gdtmNext = new GTDMNew(l, matrixA);
                                    gdtmNext.startFirstCalcualtions(true, false);
                                    listaGDTMOWNext.add(gdtmNext);
                                    tex.add(new TexturalPropertiesNew(gdtmNext));
                                }
                                stopTime = System.currentTimeMillis();
                                elapsedTime = stopTime - startTime;
                                calc1 += elapsedTime;


                                startTime = System.currentTimeMillis();
                                texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                                properties.add(texturalPropertiesNew.getProps());
                                stopTime = System.currentTimeMillis();
                                elapsedTime = stopTime - startTime;
                                calc2 += elapsedTime;
                                tex.clear();
                            } else if (j == q / 2) {
                                startTime = System.currentTimeMillis();
                                listaGDTMOWNext.set(0, new GTDMNew(listaGDTMOWNext.get(0), false));
                                listaGDTMOWNext.set(1, new GTDMNew(listaGDTMOWNext.get(1), false));
                                listaGDTMOWNext.set(2, new GTDMNew(listaGDTMOWNext.get(2), false));

                                listaGDTMOWNext.get(0).startNextRowCalcualtions(true, false);
                                listaGDTMOWNext.get(1).startNextRowCalcualtions(true, false);
                                listaGDTMOWNext.get(2).startNextRowCalcualtions(true, false);

                                stopTime = System.currentTimeMillis();
                                elapsedTime = stopTime - startTime;
                                calc1 += elapsedTime;

                                startTime = System.currentTimeMillis();
//                        texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext,texturalPropertiesNew,false);
                                tex.add(new TexturalPropertiesNew(listaGDTMOWNext.get(0)));
                                tex.add(new TexturalPropertiesNew(listaGDTMOWNext.get(1)));
                                tex.add(new TexturalPropertiesNew(listaGDTMOWNext.get(2)));

                                texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                                properties.add(texturalPropertiesNew.getProps());
                                stopTime = System.currentTimeMillis();
                                elapsedTime = stopTime - startTime;
                                calc2 += elapsedTime;
                                tex.clear();
                            } else {
                                startTime = System.currentTimeMillis();
                                listaGDTMOWNext.set(0, new GTDMNew(listaGDTMOWNext.get(0), true));
                                listaGDTMOWNext.set(1, new GTDMNew(listaGDTMOWNext.get(1), true));
                                listaGDTMOWNext.set(2, new GTDMNew(listaGDTMOWNext.get(2), true));

                                listaGDTMOWNext.get(0).startNextColumnCalcualtions(true, false);
                                listaGDTMOWNext.get(1).startNextColumnCalcualtions(true, false);
                                listaGDTMOWNext.get(2).startNextColumnCalcualtions(true, false);

                                stopTime = System.currentTimeMillis();
                                elapsedTime = stopTime - startTime;
                                calc1 += elapsedTime;

                                startTime = System.currentTimeMillis();
//                        texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext,texturalPropertiesNew,true);
                                tex.add(new TexturalPropertiesNew(listaGDTMOWNext.get(0)));
                                tex.add(new TexturalPropertiesNew(listaGDTMOWNext.get(1)));
                                tex.add(new TexturalPropertiesNew(listaGDTMOWNext.get(2)));

                                texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                                properties.add(texturalPropertiesNew.getProps());
                                stopTime = System.currentTimeMillis();
                                elapsedTime = stopTime - startTime;
                                calc2 += elapsedTime;

                                tex.clear();
                            }
                            System.out.println("i:" + i + " j:" + j);
                        }
                    }
                }
            }

            System.out.println("CALC DONE");
            System.out.println("CALC1: "+calc1);
            System.out.println("CALC2: "+calc2);
            System.out.println("a: "+Constans.a);
            System.out.println("b: "+Constans.b);
            calc1 = 0;
            calc2 = 0;
            ImagesCreator.creatPixelImage(properties,h,w);


            /**
             *Wpierw to obliczyc normalnie
             */
//            GTDMNew gdtmNowe = new GTDMNew(matrix);
//            gdtmNowe.startFirstCalcualtions(true, false);
//            TexturalPropertiesNew texturalPropertiesNew = new TexturalPropertiesNew(gdtmNowe);








        }
        catch (Exception ex){

        }
    }

    private void showOld(ImageMatrix matrix, MatrixCommon matrixA, int q, int w, int h) {
        long startTime = System.currentTimeMillis();
        GTDMNew gtdmNowe = null;
        for (int i=q/2; i<h-q/2;i++ ) {
            for (int j = q / 2; j < w - q / 2; j++) {
                gtdmNowe = new GTDMNew(matrix,matrixA);
                gtdmNowe.startFirstCalcualtions(true,false);
            }
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("STARE CALC 1" + elapsedTime);
    }

    private void calculationOfSquareRegion() {
       //for (int i=0; i<)
    }

//    private TexturalPropertiesNew calculationsBasedOnSquareSize(ImageMatrix imageMatrix, int measure) {
//        int height = imageMatrix.getHeight();
//        int weight = imageMatrix.getWidth();
//        ArrayList<TexturalPropertiesNew> tex = new ArrayList<>();
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
//                GTDMNew gdtmNowe = new GTDMNew(m);
//                gdtmNowe.startCalcualtions(true);
//                TexturalPropertiesNew texturalPropertiesNew = new TexturalPropertiesNew(gdtmNowe);
//                String str = Integer.toString (i*(weight/measure +1)+j);
//                texturalPropertiesNew.saveToCsv(str);
//                tex.add(texturalPropertiesNew);
//            }
//        }
//        return Transformer.averageProperties(tex,tex.get(0).getColor());
//    }
//
//    private TexturalPropertiesNew calculationsBasedOnSquareSize2(ImageMatrix imageMatrix, int measure) {
//        int height = imageMatrix.getHeight();
//        int weight = imageMatrix.getWidth();
//        ArrayList<GTDMNew> tex = new ArrayList<>();
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
//                GTDMNew gdtmNowe = new GTDMNew(m);
//                gdtmNowe.startCalcualtions(false);
//                tex.add(gdtmNowe);
//            }
//        }
//        return new TexturalPropertiesNew(new GTDMNew(tex, imageMatrix.getHeight(), imageMatrix.getWidth()));
//    }

    /**
     * Transform path to new Matrix of data
     */
    private void imagePathToMatrix() {
        for (String pathToImagePlusName : listOfPathsToImagePlusName){
            File img = new File(pathToImagePlusName);
            BufferedImage buffImage = null;
            try { buffImage = ImageIO.read(img); }
            catch (IOException e) { e.printStackTrace(); }
            ArrayList<ImageMatrix> listOfSingleColorImage = new ArrayList<>();
            listOfSingleColorImage.add(new ImageMatrix(buffImage, ImageMatrix.COLOR.BLUE, pathToImagePlusName));
            listOfSingleColorImage.add(new ImageMatrix(buffImage, ImageMatrix.COLOR.RED, pathToImagePlusName));
            listOfSingleColorImage.add(new ImageMatrix(buffImage, ImageMatrix.COLOR.GREEN, pathToImagePlusName));
            listOfMatrixData.add(listOfSingleColorImage);
        }
    }

    /**
     * Creates list of images paths + its name
     * @param folder
     */
    public  void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                String fullPathWithNameOfImage = FOLDER_PATH+fileEntry.getName();
                if (fullPathWithNameOfImage.endsWith(".jpg")){
                    listOfPathsToImagePlusName.add(FOLDER_PATH+fileEntry.getName());
                    System.out.println(FOLDER_PATH+fileEntry.getName());
                }
            }
        }
    }

    public void oldSquareCalc(){
        //            if (!concurrent) {
//                 startTime = System.currentTimeMillis();
//                for (ArrayList<ImageMatrix> list : listOfMatrixData) {
//                    tex = new ArrayList<>();
//                    for (ImageMatrix matrix : list) {
//                        TexturalPropertiesNew texturalPropertiesNew;
//                        if (Constans.isAverageMatrixes())
//                            texturalPropertiesNew = calculationsBasedOnSquareSize2(matrix, Constans.getQuadraticSize());
//                        else
//                            texturalPropertiesNew = calculationsBasedOnSquareSize(matrix, Constans.getQuadraticSize());
////                    GTDMNew gdtmNowe = new GTDMNew(matrix);
////                    gdtmNowe.startCalcualtions(true);
////                    TexturalPropertiesNew texturalPropertiesNew = new TexturalPropertiesNew(gdtmNowe);
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
//                    final ArrayList<TexturalPropertiesNew> finalTex = new ArrayList<>();
//                    for (ImageMatrix matrix : array) {
//                        TexturalPropertiesNew texturalPropertiesNew;
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
}



//for (int i = q / 2; i < h - q / 2; i++) {
//        for (int j = q / 2; j < w - q / 2; j++) {
//
//        if (i == q / 2 && j == q / 2) {
//        /**
//         * TO JEST TEN FIRST Z TESTU
//         */
//
//        startTime = System.currentTimeMillis();
//        gdtmNext = new GTDMNew(matrix, matrixA);
//        gdtmNext.startFirstCalcualtions(true, false);
//        stopTime = System.currentTimeMillis();
//        elapsedTime = stopTime - startTime;
//        calc1 += elapsedTime;
//
//        startTime = System.currentTimeMillis();
//        texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext);
//        properties.add(texturalPropertiesNew.getProps());
//        stopTime = System.currentTimeMillis();
//        elapsedTime = stopTime - startTime;
//        calc2 += elapsedTime;
//        } else if (j == q / 2) {
//        startTime = System.currentTimeMillis();
//        gdtmNext = new GTDMNew(gdtmNext, false);
//        gdtmNext.startNextRowCalcualtions(true, false);
//        stopTime = System.currentTimeMillis();
//        elapsedTime = stopTime - startTime;
//        calc1 += elapsedTime;
//
//        startTime = System.currentTimeMillis();
////                        texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext,texturalPropertiesNew,false);
//        texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext);
//        properties.add(texturalPropertiesNew.getProps());
//        stopTime = System.currentTimeMillis();
//        elapsedTime = stopTime - startTime;
//        calc2 += elapsedTime;
//
//        } else {
//        startTime = System.currentTimeMillis();
//        gdtmNext = new GTDMNew(gdtmNext, true);
//        gdtmNext.startNextColumnCalcualtions(true, false);
//        stopTime = System.currentTimeMillis();
//        elapsedTime = stopTime - startTime;
//        calc1 += elapsedTime;
//
//        startTime = System.currentTimeMillis();
////                        texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext,texturalPropertiesNew,true);
//        texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext);
//        properties.add(texturalPropertiesNew.getProps());
//        stopTime = System.currentTimeMillis();
//        elapsedTime = stopTime - startTime;
//        calc2 += elapsedTime;
//
//
//        }
//        //System.out.println("i:" + i + " j:"+j);
//        }
//        }