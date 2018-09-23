import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Tester {

    ArrayList<String> listOfPathsToImagePlusName = new ArrayList<>();
    ArrayList<ArrayList<MatrixData>> listOfMatrixData = new ArrayList<>();
    String FOLDER_PATH = "C:\\Users\\Kamil Sowa\\Desktop\\obrazki2\\";
    int MATRIX_SIZE = 5;
    int QUADRATIC_SIZE = 100;
    int D = 2;


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
            long startTime;
            boolean concurrent = true;
            if (!concurrent) {

                 startTime = System.currentTimeMillis();
                for (ArrayList<MatrixData> list : listOfMatrixData) {
                    tex = new ArrayList<>();
                    for (MatrixData matrix : list) {

                        TexturalPropertiesNew texturalPropertiesNew = calculationsBasedOnSquareSize(matrix, QUADRATIC_SIZE);
//                    GTDMNew gdtmNowe = new GTDMNew(matrix);
//                    gdtmNowe.startCalcualtions(true);
//                    TexturalPropertiesNew texturalPropertiesNew = new TexturalPropertiesNew(gdtmNowe);
                        //texturalPropertiesNew.saveToCsv("partNONE");
                        tex.add(texturalPropertiesNew);
                    }
                    Transformer.averageProperties(tex.get(0), tex.get(1), tex.get(2));
                }
                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                System.out.println("Elapsed time" + elapsedTime);

            }
            else {
                startTime = System.currentTimeMillis();
                Consumer<? super ArrayList<MatrixData>> consumer = (array) -> {
                    final ArrayList<TexturalPropertiesNew> finalTex = new ArrayList<>();
                    for (MatrixData matrix : array) {
                        TexturalPropertiesNew texturalPropertiesNew = calculationsBasedOnSquareSize(matrix, QUADRATIC_SIZE);
                        finalTex.add(texturalPropertiesNew);
                    }
                    Transformer.averageProperties(finalTex.get(0), finalTex.get(1), finalTex.get(2));
                };

                listOfMatrixData
                        .parallelStream()
                        .forEach(consumer);

                long stopTime = System.currentTimeMillis();
                long elapsedTime = stopTime - startTime;
                System.out.println(elapsedTime);
            }
        }
        catch (Exception ex){

        }
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
        for (String pathToImagePlusName : listOfPathsToImagePlusName){
            File img = new File(pathToImagePlusName);
            BufferedImage buffImage = null;
            try { buffImage = ImageIO.read(img); }
            catch (IOException e) { e.printStackTrace(); }
            ArrayList<MatrixData> listOfSingleColorImage = new ArrayList<>();
            listOfSingleColorImage.add(new MatrixData(buffImage, MatrixData.COLOR.BLUE, pathToImagePlusName));
            listOfSingleColorImage.add(new MatrixData(buffImage, MatrixData.COLOR.RED, pathToImagePlusName));
            listOfSingleColorImage.add(new MatrixData(buffImage, MatrixData.COLOR.GREEN, pathToImagePlusName));
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
                String fullPathWithNameOfImage = FOLDER_PATH+fileEntry.getName();
                if (fullPathWithNameOfImage.endsWith(".jpg")){
                    listOfPathsToImagePlusName.add(FOLDER_PATH+fileEntry.getName());
                    System.out.println(FOLDER_PATH+fileEntry.getName());
                }
            }
        }
    }
}
