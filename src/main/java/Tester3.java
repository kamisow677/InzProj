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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * @author Kamil Sowa
 * @version 1.0
 * Główna klasa programu zarządzająca działąniem całego procesu tworzenia map cech
 *
 */
public class Tester3 {

    /**
     * lista ścieżek do obrazów z ich nazwami
     */
    public ArrayList<String> listOfPathsToImagePlusName = new ArrayList<>();
    /**
     * lista macierzy z danymi o obrazach
     */
    ArrayList<ArrayList<ImageMatrix>> listOfMatrixData = new ArrayList<>();
    /**
     * dane o stopniu wykonania pracy
     */
    public Map<String,Integer> progress = new HashMap();
    /**
     * całkowita ilość pracy do wykonania
     */
    public Map<String,Integer> progressMax = new HashMap();

    public ArrayList<ArrayList<ImageMatrix>> getListOfMatrixData() {
        return listOfMatrixData;
    }

    /**
     * metoda rozpoczynająca pracę procesu tworzenia map cech
     */
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
            Constans.validInputData = listFilesForFolder(folder);
            imagePathToMatrix();
            if (listOfPathsToImagePlusName.size()==0 && Constans.validInputData==true){
                Constans.validationMessage = "There are no images in selected path. Please correct selected path.";
                Constans.validInputData = false;
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        Constans.validationEnd = true;

        for (ArrayList<ImageMatrix> list : listOfMatrixData){
            ArrayList<GTDM> arrTemp= new ArrayList<>();
            ArrayList<TexturalProperties> propsTemp= new ArrayList<>();
            for (ImageMatrix m : list){
                m.setStartHeight(0);
                m.setStartWidth(0);
                GTDM gdtmNowe = new GTDM(m);
                gdtmNowe.startFirstCalcualtions(true, false);
                gdtmNowe.saveToCSV("");
                TexturalProperties texturalProperties = new TexturalProperties(gdtmNowe);
                arrTemp.add(gdtmNowe);
                propsTemp.add(texturalProperties);
                System.out.println(texturalProperties);
            }
            if (list.size()==3){
                if (Constans.AVERAGE_MATRIXES) {
                    GTDM gdtmNowe = new GTDM(arrTemp.get(0), arrTemp.get(1), arrTemp.get(2));
                    ((ImageMatrix) gdtmNowe.getInputDataMatrix()).setColor(ImageMatrix.COLOR.ALL);
                    gdtmNowe.saveToCSV("");
                    TexturalProperties texturalProperties = new TexturalProperties(gdtmNowe);
                    System.out.println(texturalProperties);
                } else{
                    TexturalProperties texturalProperties = Transformer.averageProperties(propsTemp,"ALL");
                    System.out.println(texturalProperties);
                }
            }
        }
        Consumer<? super ArrayList<ImageMatrix>> consumer;

        consumer = (array) -> createTask2(array);

        listOfMatrixData.stream()
            .forEach(consumer);


        Constans.NUMBER_OF_COLORS = 4;
        progress.clear();
        progressMax.clear();

    }

    /**
     * Zmienia listę ścieżek do obrazów na obiekty klasy ImageMatrix i zapisuje je w kolekcji
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


                if (isGrey(buffImage)) {
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
     * Sprawdzenie czy obraz jest w skali szarości
     * @return true jeśli obraz jest w skali szarości
     */
    private boolean isGrey(BufferedImage buffImage){
        if ( buffImage.getType() == BufferedImage.TYPE_BYTE_GRAY || buffImage.getRaster().getNumBands() == 1)
            return true;
        else
            return false;
    }

    /**
     * Generuje listę ścieżek do obrazów znajdujących się w folderze
     * @param folder folder z obrazami
     */
    public boolean listFilesForFolder(final File folder) {
        if (folder.listFiles() == null){
            Constans.validationMessage = "Path you selected is incorrect. Please correct selected path.";
            return  false;
        }else {
            for (final File fileEntry : folder.listFiles()) {
                String fullPathWithNameOfImage = Constans.FOLDER_PATH + fileEntry.getName();
                if (fullPathWithNameOfImage.endsWith(".jpg") || fullPathWithNameOfImage.endsWith(".tif") || fullPathWithNameOfImage.endsWith(".tiff")
                        || fullPathWithNameOfImage.endsWith(".png") || fullPathWithNameOfImage.endsWith(".gif")) {
                    listOfPathsToImagePlusName.add(Constans.FOLDER_PATH + fileEntry.getName());
                }
            }
            return true;
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
                   // System.out.println("i:" + i + " j:" + j);
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
               //     System.out.println("i:" + i + " j:" + j);
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


    /**
     * Tworzy zadania do wykonania. Dzieli obraz na części i rozdziela je do przetwarzania
     * @param list lista macierzy obrazów do przetworzenia
     * @return
     */
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
            matrixesA.add(gdtmNowe.getMatrixA());
            l.setHeight(Constans.QUADRATIC_SIZE);
            l.setWidth(Constans.QUADRATIC_SIZE);
        }
        int numberOfThreads;
        if (Constans.parallel==true) {
            numberOfThreads = Runtime.getRuntime().availableProcessors() - 2;
        }else {
            numberOfThreads = 1;
        }
        ArrayList< ArrayList<ImageMatrix>> listParts = new ArrayList<>();
        int partheight = (h) / numberOfThreads;
        for (int i =0 ; i< numberOfThreads ; i++) {
            ArrayList<ImageMatrix> list2 = new ArrayList<>();
            for (ImageMatrix imageMatrix : list) {
                ImageMatrix im = new ImageMatrix(imageMatrix);
                im.setStartHeight(i*partheight);
                im.setStartWidth(0);
                im.setHeight(Constans.QUADRATIC_SIZE);
                im.setWidth(Constans.QUADRATIC_SIZE);
                list2.add(im);
            }
            listParts.add(list2);
        }

        int rest = (h) % numberOfThreads;

        ExecutorService executorService = Executors.newWorkStealingPool(numberOfThreads);
        for (int i = 0 ; i<listParts.size() -1; i++){
            callables.add(createCallable(i*partheight, (i+1)*partheight, q, w, new ArrayList<>(listParts.get(i)), new ArrayList<>(matrixesA)));
        }
        callables.add(createCallable((listParts.size() -1)*partheight, (listParts.size())*partheight + rest, q, w, new ArrayList<>(listParts.get(listParts.size() -1)), new ArrayList<>(matrixesA)));

        try {
            List<Future<ArrayList<Map<String, Double>>>> futures = executorService.invokeAll(callables);
//            while (!checkIfAllAreDone(futures)){
//                Thread.sleep(1000);
//            }
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

    private boolean checkIfAllAreDone(List<Future<ArrayList<Map<String,Double>>>> futures) {
        boolean done = true;
        for (Future<ArrayList<Map<String, Double>>> future : futures) {
            if (!future.isDone()) {
                done = false;
                break;
            }
        }
        return  done;
    }

    /**
     * Metoda tworzy nowy obiekt Callable, który zajmie się przetwarzaniem części obrazu
     * @param startRow początkowy wiersz
     * @param endRow końcowy wiersz
     * @param q wielkość kwadratowego regionu
     * @param w szerokość całego obrazu
     * @param list lista macierzy z danymi o obrazie
     * @param matrixesA macierz lub macierze średniej wartości pikseli
     * @return obiekt Callable
     */
    public Callable<ArrayList<Map<String,Double>>> createCallable(int startRow, int endRow, int q, int w,
            ArrayList<ImageMatrix> list, ArrayList<MatrixCommon> matrixesA) {
       // AtomicInteger numer = new AtomicInteger();


        Callable<ArrayList<Map<String, Double>>> task = () -> {
            ArrayList<Map<String, Double>> properties = new ArrayList<>();
            GTDM gdtmNext = null;
            TexturalProperties texturalPropertiesNew = null;
            ArrayList<TexturalProperties> tex = new ArrayList<>();
            ArrayList<GTDM> listaGDTMOWNext = new ArrayList<>();

            if (Constans.AVERAGE_MATRIXES) {
                for (int i = startRow; i < endRow; i++) {
                    for (int j = 0; j < w  ; j++) {
                        try {
                            if (i == startRow && j == 0) {
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
                                progress.put(gdtmNext.getInputDataMatrix().getImageName(),(int) 0);
                                //if (!(list.get(0).getBufferedImage().getType()==BufferedImage.TYPE_BYTE_GRAY))
                                if (!isGrey(list.get(0).getBufferedImage()))
                                    gdtmNext = new GTDM(listaGDTMOWNext.get(0), listaGDTMOWNext.get(1), listaGDTMOWNext.get(2));
                                else
                                    gdtmNext = listaGDTMOWNext.get(0);
                                texturalPropertiesNew = new TexturalProperties(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();

                            } else if (j == 0) {

                                //if ((list.get(0).getBufferedImage().getType()==BufferedImage.TYPE_BYTE_GRAY)){
                                if (isGrey(list.get(0).getBufferedImage())){
                                    if (progress.get(gdtmNext.getInputDataMatrix().getImageName())==4){
                                        int asddas=12;

                                    }
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
                                //if ((list.get(0).getBufferedImage().getType()==BufferedImage.TYPE_BYTE_GRAY)){
                                 if (isGrey(list.get(0).getBufferedImage())){
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
                          //  System.out.println("i:" + i + " j:" + j);
                        } catch (ArrayIndexOutOfBoundsException ex) {
                           // System.out.println("i:" + i + " j:" + j);
                        }
                    }
                    if (progress.get(gdtmNext.getInputDataMatrix().getImageName())!=null)
                        progress.put(gdtmNext.getInputDataMatrix().getImageName(),(int) progress.get(gdtmNext.getInputDataMatrix().getImageName())+1);
                    else
                        progress.put(gdtmNext.getInputDataMatrix().getImageName(),1);
                    System.out.println(progress);

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
                    for (int j = 0; j < w ; j++) {
                        if (i == startRow && j == 0) {
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
                        } else if (j == 0) {
                            //if ((list.get(0).getBufferedImage().getType()==BufferedImage.TYPE_BYTE_GRAY)){
                            if (isGrey(list.get(0).getBufferedImage())){
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
                            //if ((list.get(0).getBufferedImage().getType()==BufferedImage.TYPE_BYTE_GRAY)){
                            if (isGrey(list.get(0).getBufferedImage())){
                                listaGDTMOWNext.set(0, new GTDM(listaGDTMOWNext.get(0), true));
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
                  //  numer.getAndIncrement();
                   // System.out.println(numer.get());
                }
            }
            return properties;
        };
        return task;
    }
    private void showOld(ImageMatrix matrix) {
        long startTime = System.currentTimeMillis();
        int q = Constans.QUADRATIC_SIZE;
        int h = matrix.height;
        int w = matrix.width;
        matrix.height= Constans.QUADRATIC_SIZE;
        matrix.width= Constans.QUADRATIC_SIZE;
        GTDM gtdmNowe = null;
        gtdmNowe = new GTDM(matrix);
        MatrixCommon matrixA = gtdmNowe.getMatrixA();
        for (int i = q / 2; i < h - q / 2; i++) {
            for (int j = q / 2; j < w - q / 2; j++) {
                gtdmNowe = new GTDM(matrix, matrixA);
                gtdmNowe.startFirstCalcualtions(true, false);
                TexturalProperties texturalProperties = new TexturalProperties(gtdmNowe);
            }
            System.out.println(" old: "+i);
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("WEWNATRZ" + elapsedTime);
    }
}
