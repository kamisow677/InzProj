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
        System.out.println("TESTOWA KLASA DO BUFOROW");

        File img = new File("ork.jpg");
        ImageMatrix imageMatrix = null;
        BufferedImage buffImage = null;
//        try { buffImage = ImageIO.read(img); }
//        catch (IOException e) { e.printStackTrace(); }
//        imageMatrix = new ImageMatrix(buffImage, ImageMatrix.COLOR.GREY);
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

        System.out.println("D: "+Constans.getD());
        System.out.println("Quadratic: "+Constans.getQuadraticSize());
        System.out.println("average MAtrixes?: "+Constans.isAverageMatrixes());

        try {
            final File folder = new File(FOLDER_PATH);
            listFilesForFolder(folder);
            imagePathToMatrix();
            ArrayList<TexturalPropertiesNew> tex = new ArrayList<>();
            long startTime;
            boolean concurrent = false;


            /**
             * Now i only take one matrix
             */
            ImageMatrix matrix = listOfMatrixData.get(0).get(0);
            int q = Constans.QUADRATIC_SIZE;
            int w = matrix.getWidth();
            int h = matrix.getHeight();
            /**
             * Tu jest wylicznie punktu startowego czyli pierwszego srodka z kwadratwego regionu
             */
            int startPointX=q/2;
            int startPointY=q/2;
            matrix.setStartHeight(0);
            matrix.setStartWidth(0);



            GTDMNew gdtmNowe = new GTDMNew(matrix);
            MatrixCommon matrixA = gdtmNowe.getMatrixA();

            GTDMNew gdtmNext = null;
            matrix.setHeight(Constans.QUADRATIC_SIZE);
            matrix.setWidth(Constans.QUADRATIC_SIZE);


//            for (int i=q/2; i<h-q/2;i++ ) {
//                for (int j = q / 2; j < w - q / 2; j++) {
//                    gdtmNext = new GTDMNew(matrix);
//                    gdtmNext.startCalcualtions(true);
//                    TexturalPropertiesNew texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext);
//                    System.out.println(texturalPropertiesNew);
//                    System.out.println("i:" + i + " j:"+j);
//                }
//            }

            TexturalPropertiesNew texturalPropertiesNew;
            ArrayList<Map<String,Double>> properties = new ArrayList<>();

            for (int i=q/2; i<h-q/2;i++ ){
                for (int j=q/2; j<w-q/2;j++ ){
                    if (i==q/2 && j==q/2){
                        /**
                         * TO JEST TEN FIRST Z TESTU
                         */
                        gdtmNext = new GTDMNew(matrix,matrixA);
                        gdtmNext.startFirstCalcualtions(true, false);
                    }else if (j==q/2){
                        gdtmNext = new GTDMNew(gdtmNext, false);
                        gdtmNext.startNextRowCalcualtions(true, false);


                    } else {
                        gdtmNext = new GTDMNew(gdtmNext, true);
                        gdtmNext.startNextColumnCalcualtions(true, false);
                    }
                    texturalPropertiesNew = new TexturalPropertiesNew(gdtmNext);
                    //System.out.println(texturalPropertiesNew);
                    properties.add(texturalPropertiesNew.getProps());
                    //System.out.println("i:" + i + " j:"+j);
                }
            }
            System.out.println("CALC DONE");
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

    private void calculationOfSquareRegion() {
       //for (int i=0; i<)
    }

    private TexturalPropertiesNew calculationsBasedOnSquareSize(ImageMatrix imageMatrix, int measure) {
        int height = imageMatrix.getHeight();
        int weight = imageMatrix.getWidth();
        ArrayList<TexturalPropertiesNew> tex = new ArrayList<>();
        for (int i=0; i< height/measure + 1 ; i++){
            for (int j=0; j< weight/measure + 1; j++){
                ImageMatrix m = new ImageMatrix(imageMatrix);
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

    private TexturalPropertiesNew calculationsBasedOnSquareSize2(ImageMatrix imageMatrix, int measure) {
        int height = imageMatrix.getHeight();
        int weight = imageMatrix.getWidth();
        ArrayList<GTDMNew> tex = new ArrayList<>();
        for (int i=0; i< height/measure + 1 ; i++){
            for (int j=0; j< weight/measure + 1; j++){
                ImageMatrix m = new ImageMatrix(imageMatrix);
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
        return new TexturalPropertiesNew(new GTDMNew(tex, imageMatrix.getHeight(), imageMatrix.getWidth()));
    }

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
