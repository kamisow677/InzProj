import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class MatrixData implements Matrix {
    BufferedImage bufferedImage;

    public enum COLOR {RED, GREEN, BLUE, GREY}

    ;
    private COLOR color;

    public MatrixData(BufferedImage bufferedImage, COLOR color) {
        this.bufferedImage = bufferedImage;
        this.color = color;
    }

    @Override
    public double get(int j, int i) {
        int argb = bufferedImage.getRGB(i, j);
        switch (color) {
            case GREY:
            case BLUE: {
                return (argb) & 0xFF;
            }
            case GREEN: {
                return (argb >> 8) & 0xFF;
            }
            case RED: {
                return (argb >> 16) & 0xFF;
            }

        }
        return (argb) & 0xFF;
    }

    public String getColor() {
        switch (color) {
            case GREY: {
                return "GREY";
            }
            case BLUE: {
                return  "BLUE";
            }
            case GREEN: {
                return "GREEN";
            }
            case RED: {
                return "RED";
            }
        }
        return "???";
    }

    public void setColor(COLOR color) {
        this.color = color;
    }

    @Override
    public int getHeight() {
        return bufferedImage.getHeight();
    }

    @Override
    public int getWidth() {
        return bufferedImage.getWidth();
    }

    public void printf() {
        for (int i = 0; i < bufferedImage.getHeight(); i++) {
            for (int j = 0; j < bufferedImage.getWidth(); j++) {
                System.out.print(get(i, j) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
