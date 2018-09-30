public class MatrixCommon implements Matrix {
    private double[][] data;
    private int height;
    private int width;
    private int startHeight;
    private int startWidth;

    public MatrixCommon(double[][] data, int height, int width ) {
        this.data = data;
        this.height = height;
        this.width = width;
        this.startWidth = 0;
        this.startHeight = 0;
    }

    public MatrixCommon(double[][] data, int height, int width, int startHeight, int startWidth) {
        this(data,height,width);
        this.startHeight = startHeight;
        this.startWidth = startWidth;

    }

    public MatrixCommon(int height, int width) {
        this.data = new double[height][width];
        this.height = height;
        this.width = width;
        this.startHeight = 0;
        this.startWidth = 0;
    }

    public double[][] getData() {
        return data;
    }

    public void setData(double[][] data) {
        this.data = data;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void set(int i, int j, double value) {
        data[i+startHeight][j+startWidth] = value;
    }

    @Override
    public double get(int i, int j) {
        return data[i+startHeight][j+startWidth];
    }

    public void printf() {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(data[i+startHeight][j+getStartWidth()] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public String toString() {
        String str = new String();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                str += (data[i+startWidth][j+getStartHeight()] + " ");
            }
            str += "\n";
        }
        str += "\n";
        return  str;
    }

    @Override
    public String getColor() {
        return "GREY";
    }

    @Override
    public String getImageName() {
        return null;
    }

    @Override
    public int getStartHeight() {
        return startHeight;
    }

    @Override
    public void setStartHeight(int startHeight) {
        this.startHeight = startHeight;
    }

    @Override
    public int getStartWidth() {
        return startWidth;
    }

    @Override
    public void setStartWidth(int startWidth) {
        this.startWidth = startWidth;
    }
}
