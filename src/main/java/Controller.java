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

/**
 * Główna klasa programu będąca kontrolerem. Sprawuje łączność między View, a Model
 * @author Kamil Sowa
 * @version 1.0
 *
 */
public class Controller {

    /**
     * Lista ścieżek do obrazów z ich nazwami
     */
    public ArrayList<String> listOfPathsToImagePlusName = new ArrayList<>();
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

    public ArrayList<ArrayList<ImageMatrix>> getListOfMatrixData() {
        return listOfMatrixData;
    }

    /**
     * Metoda rozpoczynająca pracę procesu tworzenia map cech
     */
    public void run() {

        System.out.println("D: " + Constans.D);
        System.out.println("Quadratic: " + Constans.QUADRATIC_SIZE);
        System.out.println("Average matrixes?: " + Constans.AVERAGE_MATRIXES);
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
        Model model = new Model(listOfMatrixData,progress,progressMax);
        model.run();
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
                progressMax.put(pathToImagePlusName,buffImage.getHeight());


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
     * Generuje listę ścieżek do obrazów znajdujących się w folderze
     * @param folder folder z obrazami
     * @return zwraca true jeśli w folderze są obrazy
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

}
