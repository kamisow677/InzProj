import java.awt.image.DataBuffer;

public interface Matrix {

    public double get(int j, int i);

    public int getHeight();

    public int getWidth();

    public void printf();

    public String getColor();

    public String getImageName();

    public int getStartWidth();

    public int getStartHeight();

    public void setStartWidth(int width);

    public void setStartHeight(int height);

    public void setWidth(int width);

    public void setHeight(int height);

}
