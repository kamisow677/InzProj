import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 *
 * Klasa będąca modelem. Odpowiedzialna za sterowanie algorytmem wyliczającym cechy struktury oraz mapy cech
 * @author Kamil Sowa
 * @version 1.0
 *
 */
public class Model {

    /**
     * Lista macierzy z danymi o obrazach
     */
    ArrayList<ArrayList<ImageMatrix>> listOfMatrixData = new ArrayList<>();
    /**
     * Dane o stopniu wykonania pracy
     */
    public Map<String,Integer> progress = new HashMap();
    /**
     * Całkowita ilość pracy do wykonania
     */
    public Map<String,Integer> progressMax = new HashMap();

    /**
     * @param listOfMatrixData Lista macierzy z danymi o obrazach
     * @param progress Dane o stopniu wykonania pracy
     * @param progressMax Całkowita ilość pracy do wykonania
     */
    public Model(ArrayList<ArrayList<ImageMatrix>> listOfMatrixData, Map<String, Integer> progress, Map<String, Integer> progressMax) {
        this.listOfMatrixData = listOfMatrixData;
        this.progress = progress;
        this.progressMax = progressMax;
    }

    /**
     * Metoda rozpoczynająca pracę procesu tworzenia map cech
     */
    public void run() {

        for (ArrayList<ImageMatrix> list : listOfMatrixData){
            ArrayList<GTDM> arrTemp= new ArrayList<>();
            ArrayList<TexturalProperties> propsTemp= new ArrayList<>();
            for (ImageMatrix m : list){
                m.setStartHeight(0);
                m.setStartWidth(0);
                GTDM gdtmNowe = new GTDM(m);
                gdtmNowe.startFirstCalcualtions();
                gdtmNowe.saveToCSV("");
                TexturalProperties texturalProperties = new TexturalProperties(gdtmNowe);
                arrTemp.add(gdtmNowe);
                propsTemp.add(texturalProperties);
                System.out.println(texturalProperties);
                texturalProperties.saveToTXT();
            }
            if (list.size()==3){
                if (Constans.AVERAGE_MATRIXES) {
                    GTDM gdtmNowe = new GTDM(arrTemp.get(0), arrTemp.get(1), arrTemp.get(2));
                    ((ImageMatrix) gdtmNowe.getInputDataMatrix()).setColor(ImageMatrix.COLOR.ALL);
                    gdtmNowe.saveToCSV("");
                    TexturalProperties texturalProperties = new TexturalProperties(gdtmNowe);
                    System.out.println(texturalProperties);
                    texturalProperties.saveToTXT();
                } else{
                    TexturalProperties texturalProperties = Transformer.averageProperties(propsTemp,"ALL");
                    System.out.println(texturalProperties);
                }
            }
        }
        Consumer<? super ArrayList<ImageMatrix>> consumer;

        consumer = (array) -> createTask(array);

        listOfMatrixData.stream()
                .forEach(consumer);


        Constans.NUMBER_OF_COLORS = 4;
        progress.clear();
        progressMax.clear();

    }

    /**
     * Sprawdzenie czy obraz jest w skali szarości
     * @param buffImage dane obrazu
     * @return true jeśli obraz jest w skali szarości
     */
    private boolean isGrey(BufferedImage buffImage){
        if ( buffImage.getType() == BufferedImage.TYPE_BYTE_GRAY || buffImage.getRaster().getNumBands() == 1)
            return true;
        else
            return false;
    }

    /**
     * Tworzy zadania do wykonania. Dzieli obraz na części i rozdziela je do przetwarzania
     * @param list lista macierzy obrazów do przetworzenia
     */
    public void createTask(ArrayList<ImageMatrix> list) {

        ArrayList<Map<String, Double>> properties = new ArrayList<>();
        ArrayList<MatrixCommon> matrixesA = new ArrayList<>();
        GTDM gdtmNowe = null;
        ArrayList<GTDM> listaGDTMOWNext = new ArrayList<>();
        ArrayList<Callable<ArrayList<Map<String, Double>>>> callables = new ArrayList<>();
        ArrayList<ArrayList<Map<String, Double>>>listOfproperties = new ArrayList<>();

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
            if (numberOfThreads<1)
                numberOfThreads =1;
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
            callables.add(createCallable(i*partheight, (i+1)*partheight, w, listParts.get(i), matrixesA));
        }

        callables.add(createCallable((listParts.size() -1)*partheight, (listParts.size())*partheight + rest,  w, listParts.get(listParts.size() -1), matrixesA));
        try {
            List<Future<ArrayList<Map<String, Double>>>> futures = executorService.invokeAll(callables);

            for (Future<ArrayList<Map<String, Double>>> future : futures) {
                listOfproperties.add(future.get());
            }

            for (List<Map<String, Double>> p : listOfproperties) {
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
    }

    /**
     * Metoda tworzy nowy obiekt Callable, który zajmie się przetwarzaniem części obrazu
     * @param startRow początkowy wiersz
     * @param endRow końcowy wiersz
     * @param w szerokość całego obrazu
     * @param list lista macierzy z danymi o obrazie
     * @param matrixesA macierz lub macierze średniej wartości pikseli
     * @return obiekt Callable
     */
    public Callable<ArrayList<Map<String,Double>>> createCallable(int startRow, int endRow, int w,
                                                                  ArrayList<ImageMatrix> list, ArrayList<MatrixCommon> matrixesA) {

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
                                    gdtmNext.startFirstCalcualtions();
                                    listaGDTMOWNext.add(gdtmNext);
                                    k++;
                                }
                                progress.put(gdtmNext.getInputDataMatrix().getImageName(),(int) 0);
                                if (!isGrey(list.get(0).getBufferedImage()))
                                    gdtmNext = new GTDM(listaGDTMOWNext.get(0), listaGDTMOWNext.get(1), listaGDTMOWNext.get(2));
                                else
                                    gdtmNext = listaGDTMOWNext.get(0);
                                texturalPropertiesNew = new TexturalProperties(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();

                            } else if (j == 0) {

                                if (isGrey(list.get(0).getBufferedImage())){
                                    listaGDTMOWNext.set(0, new GTDM(listaGDTMOWNext.get(0), false));
                                    listaGDTMOWNext.get(0).startNextRowCalcualtions();
                                    gdtmNext = listaGDTMOWNext.get(0);
                                }
                                else {
                                    for (int k = 0; k < Constans.NUMBER_OF_COLORS; k++) {
                                        listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), false));
                                        listaGDTMOWNext.get(k).startNextRowCalcualtions();
                                    }
                                    gdtmNext = new GTDM(listaGDTMOWNext.get(0), listaGDTMOWNext.get(1), listaGDTMOWNext.get(2));
                                }
                                texturalPropertiesNew = new TexturalProperties(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();

                            } else {
                                if (isGrey(list.get(0).getBufferedImage())){
                                    listaGDTMOWNext.set(0, new GTDM(listaGDTMOWNext.get(0), true));

                                    listaGDTMOWNext.get(0).startNextColumnCalcualtions();
                                    gdtmNext = listaGDTMOWNext.get(0);
                                }else{
                                    for (int k = 0; k < Constans.NUMBER_OF_COLORS; k++) {
                                        listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), true));
                                        listaGDTMOWNext.get(k).startNextColumnCalcualtions();
                                    }

                                    gdtmNext = new GTDM(listaGDTMOWNext.get(0), listaGDTMOWNext.get(1), listaGDTMOWNext.get(2));
                                }
                                texturalPropertiesNew = new TexturalProperties(gdtmNext);
                                properties.add(texturalPropertiesNew.getProps());
                                tex.clear();
                            }
                        } catch (ArrayIndexOutOfBoundsException ex) {
                        }
                    }
                    if (progress.get(gdtmNext.getInputDataMatrix().getImageName())!=null)
                        progress.put(gdtmNext.getInputDataMatrix().getImageName(),(int) progress.get(gdtmNext.getInputDataMatrix().getImageName())+1);
                    else
                        progress.put(gdtmNext.getInputDataMatrix().getImageName(),1);
                    System.out.println(progress);

                }
            } else {



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
                                gdtmNext.startFirstCalcualtions();
                                listaGDTMOWNext.add(gdtmNext);
                                tex.add(new TexturalProperties(gdtmNext));
                                k++;
                            }
                            texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                            properties.add(texturalPropertiesNew.getProps());
                            tex.clear();
                        } else if (j == 0) {
                            if (isGrey(list.get(0).getBufferedImage())){
                                listaGDTMOWNext.set(0, new GTDM(listaGDTMOWNext.get(0), false));
                                listaGDTMOWNext.get(0).startNextRowCalcualtions();
                                texturalPropertiesNew = new TexturalProperties(listaGDTMOWNext.get(0));
                            } else {
                                for (int k = 0; k < Constans.NUMBER_OF_COLORS; k++) {
                                    listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), false));
                                    listaGDTMOWNext.get(k).startNextRowCalcualtions();
                                    tex.add(new TexturalProperties(listaGDTMOWNext.get(k)));
                                }
                                texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                            }
                            properties.add(texturalPropertiesNew.getProps());
                            tex.clear();
                        } else {
                            if (isGrey(list.get(0).getBufferedImage())){
                                listaGDTMOWNext.set(0, new GTDM(listaGDTMOWNext.get(0), true));
                                listaGDTMOWNext.get(0).startNextColumnCalcualtions();
                                texturalPropertiesNew = new TexturalProperties(listaGDTMOWNext.get(0));
                            }else {
                                for (int k = 0; k < Constans.NUMBER_OF_COLORS; k++) {
                                    listaGDTMOWNext.set(k, new GTDM(listaGDTMOWNext.get(k), true));
                                    listaGDTMOWNext.get(k).startNextColumnCalcualtions();
                                    tex.add(new TexturalProperties(listaGDTMOWNext.get(k)));
                                }
                                texturalPropertiesNew = Transformer.averageProperties(tex, list.get(0).getColor());
                            }
                            properties.add(texturalPropertiesNew.getProps());
                            tex.clear();
                        }
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
}
