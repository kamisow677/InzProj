
import java.io.*;
import java.util.Map;
import net.imagej.ImageJ;
import org.scijava.app.StatusService;
import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import org.scijava.ui.UIService;

/**
 * @author Kamil Sowa
 * @version 1.0
 * Klasa ta służy do fabrykacji specyficznego zestawu cech tekstur zbudowanego na podstawie innego zestawu w opraciu o klasy TexturalProperties
 *
 */
@Plugin(type = Command.class, menuPath = "Plugins>Generowanie map cech")
public class View implements Command {

    @Parameter
    private StatusService sts;

    @Parameter
    UIService ui;

    @Parameter (label="Ścieżka do folderu z obrazami")
    private File pathLoad;

    @Parameter(label="Rozmiar okna obliczeń", min = "1" , max = "10")
    private int neighbourhood = 2;

    @Parameter(label="Wielkość kwadratowego regionu", min = "5" , max = "101")
    private int quadraticRegionSize = 150;

    @Parameter(label="Ścieżka do folderu, do którego będą zapisane obrazy")
    private File pathSave;

    @Parameter(label="Współczynnik kwantyzacji", min = "1" , max = "8")
    private int quantization;

    @Parameter(label="Dla obrazów kolorowych, uśredniać gtdm czy obliczone cechy tekstur ?",choices={"macierze","cechy"})
    private String averageMatrixes = "macierze";

    /**
     * Początek pracy wtyczki
     */
    @Override
    public void run() {
        Controller controller = new Controller();
        if (averageMatrixes.equals("macierze")) {
            Constans.AVERAGE_MATRIXES = true;
        }
        else {
            Constans.AVERAGE_MATRIXES = false;
        }
        Constans.D = neighbourhood;
        Constans.QUADRATIC_SIZE=quadraticRegionSize*2 +1;
        Constans.NUMBER_OF_COLORS = 3;
        Constans.QUANTIZATION = quantization;
        Constans.PIXEL_NUMBER = 255;
        Constans.PIXEL_NUMBER_PLUS_1 = 256;
        Constans.FOLDER_PATH = pathLoad.getPath()+"\\";
        Constans.SAVE_PATH = pathSave.getPath()+"\\";
        Constans.validationEnd = false;
        Constans.validInputData=true;

        new Thread(new Runnable() {
            public void run() {
                controller.run();
            }
        }).start();

        try {
            while (!Constans.validationEnd){
                Thread.sleep(100);
            }
        }catch (InterruptedException ex){
            sts.showStatus("This should not show");
        }
        if (Constans.QUADRATIC_SIZE/2  <= neighbourhood){
            Constans.validationMessage = "Please increase quadratic region or decrease neighbourhood dimension";
            Constans.validInputData = false;
        }

        if (!Constans.validInputData) {
            ui.showDialog(Constans.validationMessage);
        }else {
            Thread t1 = new Thread(new Runnable() {
                public void run() {
                    try {
                        while (Constans.NUMBER_OF_COLORS != 4) {
                            int percentage = 0;
                            for (Map.Entry<String, Integer> progress : controller.progress.entrySet()) {
                                if (!(progress.getValue() == null && controller.progressMax.get(progress.getKey()) != 0)) {
                                    percentage += (progress.getValue() * 100) / controller.progressMax.get(progress.getKey());
                                }
                            }
                            if (controller.progress.entrySet().size() != 0 && !String.valueOf(percentage / controller.progress.entrySet().size()).equals("null"))
                                sts.showStatus("" + String.valueOf(percentage / controller.getListOfMatrixData().size()) + "%  " + controller.progress.entrySet().size() + "/" + controller.getListOfMatrixData().size());
                            Thread.sleep(100);
                        }
                        sts.showStatus("GOTOWE");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            t1.start();
        }
    }

    /**
     * Metoda main wtyczki
     * @param args
     * @throws Exception
     */
    public static void main(final String... args) throws Exception {
        // Launch ImageJ as usual.
        final ImageJ ij = new ImageJ();
        ij.launch(args);
        ij.command().run(View.class, true);
    }

}

