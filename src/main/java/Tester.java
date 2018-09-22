import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

public class Tester {

    ArrayList<String> listOfPathsToImage = new ArrayList<>();
    ArrayList<ArrayList<MatrixData>> listOfMatrixData = new ArrayList<>();
    String FOLDER_PATH = "C:\\Users\\Kamil Sowa\\Desktop\\obrazki21\\";
    int WIELKSC_MACIRZY = 5;


    public void run(){
        System.out.println("TESTOWA KLASA DO BUFOROW");

        File img = new File("ork.jpg");
        MatrixData matrixData = null;
        BufferedImage buffImage = null;
//        try { buffImage = ImageIO.read(img); }
//        catch (IOException e) { e.printStackTrace(); }
//        matrixData = new MatrixData(buffImage, MatrixData.COLOR.GREY);
//
//        Random rand = new Random();
//        double[][] dane = new double[WIELKSC_MACIRZY][WIELKSC_MACIRZY];
//        for (int i = 0; i < WIELKSC_MACIRZY; i++) {
//            for (int j = 0; j < WIELKSC_MACIRZY; j++) {
//                dane[i][j] = Math.abs(rand.nextInt() % 5);
//            }
//        }
//
//        Matrix daneTestowe = new MatrixCommon(dane, WIELKSC_MACIRZY, WIELKSC_MACIRZY);

        try {

            final File folder = new File(FOLDER_PATH);
            listFilesForFolder(folder);
            imagePathToMatrix();
            ArrayList<TexturalPropertiesNew> tex = new ArrayList<>();

            long startTime = System.currentTimeMillis();
            for (ArrayList<MatrixData> list : listOfMatrixData){
                tex = new ArrayList<>();
                for (MatrixData matrix : list) {

                    TexturalPropertiesNew texturalPropertiesNew = calculationsBasedOnSquareSize(matrix,100);
//                    GTDMNew gdtmNowe = new GTDMNew(matrix);
//                    gdtmNowe.startCalcualtions(true);
//                    TexturalPropertiesNew texturalPropertiesNew = new TexturalPropertiesNew(gdtmNowe);
                    //texturalPropertiesNew.saveToCsv("partNONE");
                    tex.add(texturalPropertiesNew);
                }
                Transformer.averageProperties(tex.get(0), tex.get(1), tex.get(2));
            }

//            System.out.println(tex.get(0));
//            System.out.println(tex.get(1));
//            System.out.println(tex.get(2));
//            System.out.println(tex.get(3));
           // Transformer.averageProperties(tex.get(0), tex.get(1), tex.get(2));

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            System.out.println("Elapsed time" + elapsedTime);


        //    calculationsBasedOnSquareSize(listOfMatrixData.get(0), 200);


//            startTime = System.currentTimeMillis();
//            Consumer<? super MatrixData> consumer = (m) -> {
//                try {
//                    GTDMNew gdtmNowe = new GTDMNew(m);
//                    TexturalPropertiesNew texturalPropertiesNew = new TexturalPropertiesNew(gdtmNowe);
//                }
//                catch (Exception ex){
//
//                }
//            };
//            listOfMatrixData
//                    .parallelStream()
//                    .forEach(consumer);
//
//            stopTime = System.currentTimeMillis();
//            elapsedTime = stopTime - startTime;
//            System.out.println(elapsedTime);








//            GTDM gdtm = new GTDM(listOfMatrixData.get(0));
//            TexturalProperties texturalProperties = new TexturalProperties(gdtm);

//            GTDMNew gdtmNowe = new GTDMNew(listOfMatrixData.get(0));
//            TexturalPropertiesNew texturalPropertiesNew = new TexturalPropertiesNew(gdtmNowe);


//            matrixData.setColor(MatrixData.COLOR.RED);
//            gdtm = new GTDM(matrixData);
//            texturalProperties = new TexturalProperties(gdtm);
//
//            matrixData.setColor(MatrixData.COLOR.BLUE);
//            gdtm = new GTDM(matrixData);
//            texturalProperties = new TexturalProperties(gdtm);
//
//            GTDMNew gdtmnew = new GTDMNew(daneTestowe);
//            TexturalProperties texturalProperties = new TexturalProperties(gdtm);
        }
        catch (Exception ex){

        }
//        catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }

    private TexturalPropertiesNew calculationsBasedOnSquareSize(MatrixData matrixData, int measure) {
        int height = matrixData.getHeight();
        int weight = matrixData.getWidth();
        ArrayList<TexturalPropertiesNew> tex = new ArrayList<>();
        for (int i=0; i< height/measure + 1 ; i++){
            for (int j=0; j< weight/measure + 1; j++){
                MatrixData m = new MatrixData(matrixData);
                m.setStartHeight(measure*i);
                m.setStartWidth(measure*j);
                if (i == height/measure)
                    m.setHeight(height - (height/measure)*measure);
                else
                    m.setHeight(measure);
                if (j == weight/measure)
                    m.setWidth(weight - (weight/measure)*measure);
                else
                    m.setWidth(measure);
                GTDMNew gdtmNowe = new GTDMNew(m);
                gdtmNowe.startCalcualtions(true);
                TexturalPropertiesNew texturalPropertiesNew = new TexturalPropertiesNew(gdtmNowe);
                String str = Integer.toString (i*(weight/measure +1)+j);
                texturalPropertiesNew.saveToCsv(str);
                tex.add(texturalPropertiesNew);
            }
        }
        return Transformer.averageProperties(tex,tex.get(0).getColor());
    }

    private TexturalPropertiesNew calculationsBasedOnSquareSize2(MatrixData matrixData, int measure) {
        int height = matrixData.getHeight();
        int weight = matrixData.getWidth();
        ArrayList<GTDMNew> tex = new ArrayList<>();
        for (int i=0; i< height/measure + 1 ; i++){
            for (int j=0; j< weight/measure + 1; j++){
                MatrixData m = new MatrixData(matrixData);
                m.setStartHeight(measure*i);
                m.setStartWidth(measure*j);
                if (i == height/measure)
                    m.setHeight(height - (height/measure)*measure);
                else
                    m.setHeight(measure);
                if (j == weight/measure)
                    m.setWidth(weight - (weight/measure)*measure);
                else
                    m.setWidth(measure);
                GTDMNew gdtmNowe = new GTDMNew(m);
                gdtmNowe.startCalcualtions(false);
                tex.add(gdtmNowe);
            }
        }
        return new TexturalPropertiesNew(new GTDMNew(tex,matrixData.getHeight(),matrixData.getWidth()));
    }

    private void imagePathToMatrix() {
        for (String path : listOfPathsToImage){
            File img = new File(path);
            BufferedImage buffImage = null;
            try { buffImage = ImageIO.read(img); }
            catch (IOException e) { e.printStackTrace(); }
            ArrayList<MatrixData> listOfSingleColorImage = new ArrayList<>();
            listOfSingleColorImage.add(new MatrixData(buffImage, MatrixData.COLOR.BLUE, path));
            listOfSingleColorImage.add(new MatrixData(buffImage, MatrixData.COLOR.RED, path));
            listOfSingleColorImage.add(new MatrixData(buffImage, MatrixData.COLOR.GREEN, path));
            listOfMatrixData.add(listOfSingleColorImage);
        }
    }

    public static void newPicture(){
        File img = new File("ork.jpg");
        MatrixData matrixData = null;
        try {
            BufferedImage buffImage = ImageIO.read(img);
            buffImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    int red = 255;
                    int green = 255;
                    int blue = 0;
                    int alpha = 255;
                    int rgb = alpha;
                    rgb = (rgb << 8) + red;
                    rgb = (rgb << 8) + green;
                    rgb = (rgb << 8) + blue;
                    buffImage.setRGB(i, j, rgb);
                }
            }
            File f = new File("MyFile.png");
            ImageIO.write(buffImage, "PNG", f);

            matrixData = new MatrixData(buffImage, MatrixData.COLOR.GREY, "ImageName");
            System.out.println("Height: " + matrixData.getHeight());
            System.out.println("Width: " + matrixData.getWidth());


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public  void listFilesForFolder(final File folder) {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                String path = FOLDER_PATH+fileEntry.getName();
                if (path.endsWith(".jpg")){
                    listOfPathsToImage.add(FOLDER_PATH+fileEntry.getName());
                    System.out.println(FOLDER_PATH+fileEntry.getName());
                }
            }
        }
    }
}
