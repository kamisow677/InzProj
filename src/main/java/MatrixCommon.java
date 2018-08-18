public class MatrixCommon implements Matrix {
    private double[][] data;
    private int height;
    private int width;

    public MatrixCommon(double[][] data, int height, int width) {
        this.data = data;
        this.height = height;
        this.width = width;
    }

    public MatrixCommon(int height, int width) {
        this.data = new double[height][width];
        this.height = height;
        this.width = width;
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
        data[i][j] = value;
    }

    @Override
    public double get(int i, int j) {
        return data[i][j];
    }

    public void printf() {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
