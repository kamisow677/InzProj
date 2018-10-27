import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class Tester3 {

    public ArrayList<String> listOfPathsToImagePlusName = new ArrayList<>();
    ArrayList<ArrayList<ImageMatrix>> listOfMatrixData = new ArrayList<>();
    public Map<String,Integer> progress = new HashMap();
    public Map<String,Integer> progressMax = new HashMap();
    public String name;

    public void run() {
        System.out.println("D: " + Constans.getD());
        System.out.println("Quadratic: " + Constans.getQuadraticSize());
        System.out.println("Average matrixes?: " + Constans.isAverageMatrixes());
        Constans.NUMBER_OF_COLORS = 3;
        Constans.PIXEL_NUMBER = Constans.PIXEL_NUMBER/Constans.QUANTIZATION;
        Constans.PIXEL_NUMBER_PLUS_1 = Constans.PIXEL_NUMBER_PLUS_1/Constans.QUANTIZATION;
        listOfPathsToImagePlusName = new ArrayList<>();
        listOfMatrixData = new ArrayList<>();

        try {
            final File folder = new File(Constans.FOLDER_PATH);
            listFilesForFolder(folder);
            imagePathToMatrix();
            long startTime;
            long stopTime;
            long elapsedTime;
            long calc1 = 0;

            /**
             * Now i only take one matrix
             */

            for (ArrayList<ImageMatrix> list : listOfMatrixData){
                for (ImageMatrix m : list){
                    m.setStartHeight(0);
                    m.setStartWidth(0);
                    GTDM gdtmNowe = new GTDM(m);
                    gdtmNowe.startFirstCalcualtions(true, false);
                   // System.out.println(gdtmNowe.getP());
                    gdtmNowe.saveToCSV("");
                    //gdtmNowe.writeAllValuesOfImage();
                    TexturalProperties texturalProperties = new TexturalProperties(gdtmNowe);
                    System.out.println(texturalProperties);
                    //gdtmNowe.printfP();
                    //gdtmNowe.printfS();
                }
            }

            Transformer.compareTwoFiles();

            Consumer<? super ArrayList<ImageMatrix>> consumer = (array) -> createTask2(array) ;

            startTime = System.currentTimeMillis();

            listOfMatrixData.parallelStream()
                    .forEach(consumer);

            stopTime = System.currentTimeMillis();
            elapsedTime = stopTime - startTime;
            calc1 += elapsedTime;

            System.out.println("CALC DONE");
            System.out.println("CALC1: " + calc1);
            Constans.NUMBER_OF_COLORS = 4;
            progress.clear();
            progressMax.clear();

        } catch (Exception ex) {

        }
    }

    /**
     * Transform path to new Matrix of data
     */
    private void imagePathToMatrix() {
        for (String pathToImagePlusName : listOfPathsToImagePlusName) {
            File img = new File(pathToImagePlusName);
            ArrayList<ImageMatrix> listOfSingleColorImage = new ArrayList<>();
            BufferedImage buffImage;
            try {
                buffImage = ImageIO.read(img);
                System.out.println( pathToImagePlusName+ "  TYPE: "+buffImage.getType());
                progressMax.put(pathToImagePlusName,buffImage.getHeight()-Constans.QUADRATIC_SIZE);

                if (buffImage.getType()==BufferedImage.TYPE_BYTE_GRAY) {
                    listOfSingleColorImage.add(new ImageMatrix(buffImage, ImageMatrix.COLOR.GREY, pathToImagePlusName));
                } else {
                    listOfSingleColorImage.add(new ImageMatrix(buffImage, ImageMatrix.COLOR.BLUE, pathToImagePlusName));
                    listOfSingleColorImage.add(new ImageMatrix(buffImage, ImageMatrix.COLOR.RED, pathToImagePlusName));
                    listOfSingleColorImage.add(new ImageMatrix(buffImage, ImageMatrix.COLOR.GREEN, pathToImagePlusName));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

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
//            if (fileEntry.isDirectory()) {
//                listFilesForFolder(fileEntry);
//            } else {
                String fullPathWithNameOfImage = Constans.FOLDER_PATH + fileEntry.getName();
                if (fullPathWithNameOfImage.endsWith(".jpg") || fullPathWithNameOfImage.endsWith(".tif") || fullPathWithNameOfImage.endsWith(".tiff")
                        || fullPathWithNameOfImage.endsWith(".png") || fullPathWithNameOfImage.endsWith(".gif")) {
                    listOfPathsToImagePlusName.add(Constans.FOLDER_PATH + fileEntry.getName());
                   // System.out.println(Constans.FOLDER_PATH + fileEntry.getName());
//                }
            }
        }
    }


public Long createTask( ArrayList<ImageMatrix> list) {

        ArrayList<TexturalProperties> tex = new ArrayList<>();
        ArrayList<Map<String, Double>> properties = new ArrayList<>();//final props all to write image
        ArrayList<MatrixCommon> matrixesA = new ArrayList<>();
        GTDM gdtmNext = null;
        GTDM gdtmNowe = null;
        ArrayList<GTDM> listaGDTMOWNext = new ArrayList<>();
        TexturalProperties texturalPropertiesNew = null;

        int w = list.get(0).getWidth();
        int h = list.get(0).getHeight();
        int q = Constans.QUADRATIC_SIZE;
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

                        for (int k = 0; k < Constans.NUMBER_OF_COLORS; k++) {
                            listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), false));
                            listaGDTMOWNext.get(k).startNextRowCalcualtions(true, false);
                        }
                        gdtmNext = new GTDM(listaGDTMOWNext.get(0), listaGDTMOWNext.get(1), listaGDTMOWNext.get(2));

                        texturalPropertiesNew = new TexturalProperties(gdtmNext);
                        properties.add(texturalPropertiesNew.getProps());
                        tex.clear();

                    } else {
                        for (int k = 0; k < Constans.NUMBER_OF_COLORS; k++) {
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

                        for (int k = 0; k < Constans.NUMBER_OF_COLORS; k++) {
                            listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), false));
                            listaGDTMOWNext.get(k).startNextRowCalcualtions(true, false);
                            tex.add(new TexturalProperties(listaGDTMOWNext.get(k)));
                        }

                        texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                        properties.add(texturalPropertiesNew.getProps());
                        tex.clear();
                    } else {

                        for (int k = 0; k < Constans.NUMBER_OF_COLORS; k++) {
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
        if (list.get(0).getBufferedImage().getType()==BufferedImage.TYPE_BYTE_GRAY)
            ImagesCreator.createGreyPixelImage(properties, h, w, list.get(0).getImageName());
        else
            ImagesCreator.createRGBPixelImage(properties, h, w, list.get(0).getImageName());
        properties.clear();
        listaGDTMOWNext.clear();
        matrixesA.clear();
        return null;
    }
    public Long createTask2( ArrayList<ImageMatrix> list) {

        ArrayList<Map<String, Double>> properties = new ArrayList<>();//final props all to write image
        ArrayList<MatrixCommon> matrixesA = new ArrayList<>();
        GTDM gdtmNowe = null;
        ArrayList<GTDM> listaGDTMOWNext = new ArrayList<>();
        ArrayList<Callable<ArrayList<Map<String, Double>>>> callables = new ArrayList<>();
        ArrayList<ArrayList<Map<String, Double>>>properties2 = new ArrayList<>();//final props all to write image

        int w = list.get(0).getWidth();
        int h = list.get(0).getHeight();
        int q = Constans.QUADRATIC_SIZE;
        for (ImageMatrix l : list) {
            l.setStartHeight(0);
            l.setStartWidth(0);
            gdtmNowe = new GTDM(l);
            gdtmNowe.setD(Constans.D);
            //gdtmNowe.startFirstCalcualtions(true, false);
            matrixesA.add(gdtmNowe.getMatrixA());
            l.setHeight(Constans.QUADRATIC_SIZE);
            l.setWidth(Constans.QUADRATIC_SIZE);
        }

        int numberOfThreads = Runtime.getRuntime().availableProcessors()-2;
//        int numberOfThreads = 1;
        ArrayList< ArrayList<ImageMatrix>> listParts = new ArrayList<>();
        int pla = (h - q) / numberOfThreads;
        for (int i =0 ; i< numberOfThreads ; i++) {
            ArrayList<ImageMatrix> list2 = new ArrayList<>();
            for (ImageMatrix imageMatrix : list) {
                ImageMatrix im = new ImageMatrix(imageMatrix);
                im.setStartHeight(i*pla);
                im.setStartWidth(0);
                im.setHeight(Constans.QUADRATIC_SIZE);
                im.setWidth(Constans.QUADRATIC_SIZE);
                list2.add(im);
            }
            listParts.add(list2);
        }

        int rest = (h - q) % numberOfThreads;

        ExecutorService executor = Executors.newWorkStealingPool();
        for (int i = 0 ; i<listParts.size() -1; i++){
            callables.add(createCallable(i*pla, (i+1)*pla, q, w, new ArrayList<>(listParts.get(i)), new ArrayList<>(matrixesA)));
        }
        callables.add(createCallable((listParts.size() -1)*pla, (listParts.size())*pla + rest, q, w, new ArrayList<>(listParts.get(listParts.size() -1)), new ArrayList<>(matrixesA)));

        try {
            List<Future<ArrayList<Map<String, Double>>>> futures = executor.invokeAll(callables);
            for (Future<ArrayList<Map<String, Double>>> future : futures) {
                properties2.add(future.get());
            }

            for (List<Map<String, Double>> p : properties2) {
                for (Map<String, Double> map : p) {
                    properties.add(map);
                }
            }

            if (list.get(0).getBufferedImage().getType()==BufferedImage.TYPE_BYTE_GRAY)
                ImagesCreator.createGreyPixelImage(properties, h, w, list.get(0).getImageName());
            else
                ImagesCreator.createGreyPixelImage(properties, h, w, list.get(0).getImageName());
            properties.clear();
            listaGDTMOWNext.clear();
            matrixesA.clear();
        }
        catch (Exception ex){
            System.out.println(ex.toString());
        }
        return null;
    }



    public Callable<ArrayList<Map<String,Double>>> createCallable(int startRow, int endRow, int q, int w,
            ArrayList<ImageMatrix> list, ArrayList<MatrixCommon> matrixesA) {

        Callable<ArrayList<Map<String, Double>>> task = () -> {
            ArrayList<Map<String, Double>> properties = new ArrayList<>();
            GTDM gdtmNext = null;
            TexturalProperties texturalPropertiesNew = null;
            ArrayList<TexturalProperties> tex = new ArrayList<>();
            ArrayList<GTDM> listaGDTMOWNext = new ArrayList<>();

            if (Constans.AVERAGE_MATRIXES) {
                for (int i = startRow; i < endRow; i++) {
                    for (int j = q / 2; j < w - q / 2; j++) {
                        try {
                            if (i == startRow && j == q / 2) {
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
                                if (!(list.get(0).getBufferedImage().getType()==BufferedImage.TYPE_BYTE_GRAY))
                                    gdtmNext = new GTDM(listaGDTMOWNext.get(0), listaGDTMOWNext.get(1), listaGDTMOWNext.get(2));
                                else
                                    gdtmNext = listaGDTMOWNext.get(0);
                                texturalPropertiesNew = new TexturalProperties(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();

                            } else if (j == q / 2) {

                                if ((list.get(0).getBufferedImage().getType()==BufferedImage.TYPE_BYTE_GRAY)){
                                    listaGDTMOWNext.set(0, new GTDM(listaGDTMOWNext.get(0), false));
                                    listaGDTMOWNext.get(0).startNextRowCalcualtions(true, false);
                                    gdtmNext = listaGDTMOWNext.get(0);
                                }
                                else {
                                    for (int k = 0; k < Constans.NUMBER_OF_COLORS; k++) {
                                        listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), false));
                                        listaGDTMOWNext.get(k).startNextRowCalcualtions(true, false);
                                    }
                                    gdtmNext = new GTDM(listaGDTMOWNext.get(0), listaGDTMOWNext.get(1), listaGDTMOWNext.get(2));
                                }
                                texturalPropertiesNew = new TexturalProperties(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();

                            } else {
                                if ((list.get(0).getBufferedImage().getType()==BufferedImage.TYPE_BYTE_GRAY)){
                                    listaGDTMOWNext.set(0, new GTDM(listaGDTMOWNext.get(0), true));
                                    listaGDTMOWNext.get(0).startNextColumnCalcualtions(true, false);
                                    gdtmNext = listaGDTMOWNext.get(0);
                                }else{
                                    for (int k = 0; k < Constans.NUMBER_OF_COLORS; k++) {
                                        listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), true));
                                        listaGDTMOWNext.get(k).startNextColumnCalcualtions(true, false);
                                    }

                                    gdtmNext = new GTDM(listaGDTMOWNext.get(0), listaGDTMOWNext.get(1), listaGDTMOWNext.get(2));
                                }
                                texturalPropertiesNew = new TexturalProperties(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();
                            }
                           // System.out.println("i:" + i + " j:" + j);
                        } catch (ArrayIndexOutOfBoundsException ex) {
                          //  System.out.println("i:" + i + " j:" + j);
                        }
                    }
                    if (progress.get(gdtmNext.getInputDataMatrix().getImageName())!=null)
                        progress.put(gdtmNext.getInputDataMatrix().getImageName(),(int) progress.get(gdtmNext.getInputDataMatrix().getImageName())+1);
                    else
                        progress.put(gdtmNext.getInputDataMatrix().getImageName(),1);
                    //System.out.println(progress);
                }
            } else {

                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(Constans.FOLDER_PATH + "\\" + "Coarnesrgb" + ".txt", "UTF-8");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                for (int i = startRow; i < endRow; i++) {
                    for (int j = q / 2; j < w - q / 2; j++) {
                        if (i == startRow && j == q / 2) {
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
                            writer.println(tex.get(0).getCoarness());
                            texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                            properties.add(texturalPropertiesNew.getProps());
                            tex.clear();
                        } else if (j == q / 2) {
                            if ((list.get(0).getBufferedImage().getType()==BufferedImage.TYPE_BYTE_GRAY)){
                                listaGDTMOWNext.set(0, new GTDM(listaGDTMOWNext.get(0), false));
                                listaGDTMOWNext.get(0).startNextRowCalcualtions(true, false);
                                texturalPropertiesNew = new TexturalProperties(listaGDTMOWNext.get(0));
                                writer.println(texturalPropertiesNew.getCoarness());
                            } else {
                                for (int k = 0; k < Constans.NUMBER_OF_COLORS; k++) {
                                    listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), false));
                                    listaGDTMOWNext.get(k).startNextRowCalcualtions(true, false);
                                    tex.add(new TexturalProperties(listaGDTMOWNext.get(k)));
                                }
                                writer.println(tex.get(0).getCoarness());
                                texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                            }
                            properties.add(texturalPropertiesNew.getProps());
                            tex.clear();
                        } else {
                            if ((list.get(0).getBufferedImage().getType()==BufferedImage.TYPE_BYTE_GRAY)){
                                listaGDTMOWNext.set(0, new GTDM(listaGDTMOWNext.get(0), true));
                                Constans.a=99;
                                listaGDTMOWNext.get(0).startNextColumnCalcualtions(true, false);
                                texturalPropertiesNew = new TexturalProperties(listaGDTMOWNext.get(0));
                                writer.println(texturalPropertiesNew.getCoarness());
                            }else {
                                for (int k = 0; k < Constans.NUMBER_OF_COLORS; k++) {
                                    listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), true));
                                    listaGDTMOWNext.get(k).startNextColumnCalcualtions(true, false);
                                    tex.add(new TexturalProperties(listaGDTMOWNext.get(k)));
                                }
                                writer.println(tex.get(0).getCoarness());
                                texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                            }
                            properties.add(texturalPropertiesNew.getProps());
                            tex.clear();
                        }
                       // System.out.println("i:" + i + " j:" + j);
                    }
                    if (progress.get(gdtmNext.getInputDataMatrix().getImageName())!=null)
                        progress.put(gdtmNext.getInputDataMatrix().getImageName(),(int) progress.get(gdtmNext.getInputDataMatrix().getImageName())+1);
                    else
                        progress.put(gdtmNext.getInputDataMatrix().getImageName(),1);
                    System.out.println(progress);
                }
            }
            return properties;
        };
        return task;
    }
    public  void pla(){}

}
