
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

import javax.imageio.ImageIO;

@Plugin(type = Command.class, menuPath = "Tutorials>ZADANIE")
public class ZADANIE implements Command {

    /*
     * This first @Parameter is a core ImageJ service (and thus @Plugin). The
     * context will provide it automatically when this command is created.
     */

    @Parameter
    private DatasetIOService datasetIOService;

    @Parameter
    private UIService ui;
    /*
     * In this command, we will be using functions that can throw exceptions.
     * Best practice is to log these exceptions to let the user know what went
     * wrong. By using the LogService to do this, we let the framework decide
     * the best place to display thrown errors.
     */
    @Parameter
    private LogService logService;


    @Parameter
    private StatusService sts;


    @Parameter
    private ConvertService convertService;

    /*
     * We need to know what image to open. So, the framework will ask the user
     * via the active user interface to select a file to open. This command is
     * "UI agnostic": it does not need to know the specific user interface
     * currently active.
     */

    @Parameter (label="Path to folder with images")
    private File imageF;

    @Parameter(min = "1" , max = "5")
    private int neighbourhood = 2;

    @Parameter(min = "16" , max = "200")
    private int quadraticRegionSize = 150;

    @Parameter(label="Choose folder to which everything should be saved")
    private File imageFile;

    @Parameter(label="Quantization factor")
    private int quantization;

    @Parameter(label="Average matrixes or average properties?",choices={"YES","NO"})
    private String averageMatrixes = "YES";

    /*
     * This command will produce an image that will automatically be shown by
     * the framework. Again, this command is "UI agnostic": how the image is
     * shown is not specified here.
     */
//    @Parameter(type = ItemIO.OUTPUT)
//    private Dataset image;
//
    @Parameter//(type = ItemIO.OUTPUT )
    private String greeting;


    List<RealLUTConverter<? extends RealType>> converters;

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
                        Thread.sleep(1000);
                    }
                    sts.showStatus("GOTOWE");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();


//        ui.showUI("asd");
//        UserInterface defaultUI = ui.getDefaultUI();
//        defaultUI.


        // image = datasetIOService.open(imageFile.getAbsolutePath());
        //ImagePlus ij=new ImagePlus(imageFile.getAbsolutePath());

        //ImagePlus imagePlus = convertService.convert(image, ImagePlus.class);

//        ImagePlus imagePlus = new ImagePlus(imageFile.getAbsolutePath());
//        BufferedImage buffImage = imagePlus.getBufferedImage();
        greeting = "neighbourhoodSize: " + neighbourhood;

//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                createAndShowGUI();
//            }
//        });


        //ImageProcessor improc =imagePlus.getProcessor();
        //imagePlus.show();

//            File img = new File("jedi.png");
//            buffImage= ImageIO.read(img );

//        FolderOpener folderOpener = new FolderOpener();
//        ImagePlus imagePlus2 = folderOpener.openFolder(imageFile.getAbsolutePath());
//        imagePlus2.show();


//        PrintWriter writer = null;
//        try {
//            writer = new PrintWriter("RGB.txt", "UTF-8");
//            writer.printf("Number og bytes per pixel %d \n", imagePlus.getBytesPerPixel());
//            writer.printf("Height %d \n", buffImage.getHeight());
//            writer.printf("Height %d \n", buffImage.getWidth());
//            //writer.printf("BLUE PARAM %d \n", blueParam);
//
//            for (int i = 0; i < buffImage.getHeight(); i++) {
//                for (int j = 0; j < buffImage.getWidth(); j++) {
//                    int argb = buffImage.getRGB(j, i);
//
//                    Color c = new Color(buffImage.getRGB(j, i), true);
//
//                    // writer.printf("%d", buffImage.getRGB(i,j));
//                    int b = (argb) & 0xFF;
//                    int g = (argb >> 8) & 0xFF;
//                    int r = (argb >> 16) & 0xFF;
//                    int a = (argb >> 24) & 0xFF;
//
//                    writer.printf(" red:%d", r);
//                    writer.printf(" green:%d", g);
//                    writer.printf(" blue:%d", b);
//                    writer.printf(" alfa:%d", a);
//
//                    int red = r;
//                    int green = g;
//                    int blue = b;
//                    int alpha = 255;
//                    int rgb = alpha;
//                    rgb = (rgb << 8) + red;
//                    rgb = (rgb << 8) + green;
//                    rgb = (rgb << 8) + blue;
//
//                    buffImage.setRGB(j, i, rgb);
//
//                    writer.printf("!!\n");
//                    writer.printf("\n\r");
//                }
//                writer.printf("\n\r");
//            }
//            //writer.printf("Numasdasdasd");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } finally {
//            writer.close();
//        }

//        imagePlus = new ImagePlus("tytul", buffImage);
//        imagePlus.show();

        //  Main.start();

    }

    /*
     * This main method is for convenience - so you can run this command
     * directly from Eclipse (or any other IDE).
     *
     * It will launch ImageJ and then run this command using the CommandService.
     * This is equivalent to clicking "Tutorials>Open Image" in the UI.
     */
    public static void main(final String... args) throws Exception {
        // Launch ImageJ as usual.
        final ImageJ ij = new ImageJ();
        ij.launch(args);
//        ij.status().showStatus("It's nine o'clock and all is well.");
//        IJ.showProgress(1, 2);
//
//
//        ImagePlus imp = IJ.getImage();
//        ImageStack stack = imp.getImageStack();
//
//        for (int i = 0; i<stack.getSize()+1;i++)
//        {
//            IJ.showProgress(i, stack.getSize()+1);
//        }
//
//                IJ.showProgress(1);


        // Launch the "OpenImage" command.
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

