
import java.awt.*;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import ij.IJ;
import ij.ImageStack;
import ij.gui.ProgressBar;
import ij.plugin.FolderOpener;
import ij.process.ImageProcessor;
import net.imagej.Dataset;
import net.imagej.DatasetService;
import net.imagej.ImageJ;

import java.awt.image.BufferedImage;

import ij.ImagePlus;
import net.imagej.ImgPlus;
import net.imagej.axis.Axes;
import net.imagej.axis.AxisType;
import net.imagej.ops.math.PrimitiveMath;
import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.converter.ChannelARGBConverter;
import net.imglib2.converter.RealLUTConverter;
import net.imglib2.display.ColorTable;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;
import net.imglib2.type.numeric.real.FloatType;
import org.scijava.ItemIO;
import org.scijava.app.StatusService;
import org.scijava.command.Command;
import org.scijava.convert.ConvertService;
import org.scijava.display.DisplayService;
import org.scijava.log.LogService;
import org.scijava.plugin.Parameter;
import net.imglib2.type.numeric.ARGBType;
import org.scijava.plugin.Plugin;
import io.scif.services.DatasetIOService;
import org.scijava.ui.UIService;
import org.scijava.ui.UserInterface;
import sun.misc.Signal;

import javax.swing.*;


@Plugin(type = Command.class, menuPath = "Tutorials>ZADANIE")
public class ZADANIE implements Command {

    @Parameter
    private StatusService sts;

    @Parameter (label="Path to folder with images")
    private File pathLoad;

    @Parameter(min = "1" , max = "5")
    private int neighbourhood = 2;

    @Parameter(min = "8" , max = "200")
    private int quadraticRegionSize = 150;

    @Parameter(label="Choose folder to which everything should be saved")
    private File pathSave;

    @Parameter(label="Quantization factor", min = "1" , max = "8")
    private int quantization;

    @Parameter(label="Average matrixes or average properties?",choices={"YES","NO"})
    private String averageMatrixes = "YES";


    /*
     * The run() method is where we do the actual 'work' of the command. In this
     * case, it is fairly trivial because we are simply calling ImageJ Services.
     */
    @Override
    public void run() {
        Tester3 tester = new Tester3();
        if (averageMatrixes.equals("YES"))
            Constans.setAverageMatrixes(true);
        else
            Constans.setAverageMatrixes(false);
        Constans.setD(neighbourhood);
        Constans.setQuadraticSize(quadraticRegionSize);
        Constans.NUMBER_OF_COLORS = 3;
        Constans.QUANTIZATION = quantization;
        Constans.PIXEL_NUMBER = 255;
        Constans.PIXEL_NUMBER_PLUS_1 = 256;
        Constans.FOLDER_PATH = pathLoad.getPath()+"\\";
        Constans.SAVE_PATH = pathSave.getPath()+"\\";

        new Thread(new Runnable() {
            public void run() {
                tester.run();
            }
        }).start();

        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {

                    while (Constans.NUMBER_OF_COLORS !=4) {
                        int percentage =0;
                        for (Map.Entry<String,Integer> progress : tester.progress.entrySet()) {
                            if (!(progress.getValue()==null && tester.progressMax.get(progress.getKey())!=0)) {
                                percentage += (progress.getValue() * 100) / tester.progressMax.get(progress.getKey());
                            }
                        }
                        if (tester.progress.entrySet().size()!=0 &&  !String.valueOf(percentage/ tester.progress.entrySet().size()).equals("null") )
                            sts.showStatus("" + String.valueOf(percentage/ tester.progress.entrySet().size()) + "%  " + tester.progress.entrySet().size());
                        Thread.sleep(100);
                    }
                    //sts.showStatus("GOTOWE");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();

    }

    public static void main(final String... args) throws Exception {
        // Launch ImageJ as usual.
        final ImageJ ij = new ImageJ();
        ij.launch(args);
        ij.command().run(ZADANIE.class, true);
    }
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        //Add the ubiquitous "Hello World" label.
        JLabel label = new JLabel("Hello World");
        frame.getContentPane().add(label);

        //Display the window.
        frame.show();
        frame.pack();
        frame.setVisible(true);
    }


}

