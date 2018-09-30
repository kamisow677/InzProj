import java.awt.image.BufferedImage;

public class ImageMatrix implements Matrix {
    BufferedImage bufferedImage;
    String imageNameWithPathName;
    int width = 0;
    int height = 0;
    int startWidth = 0;
    int startHeight = 0;
    int d;

    public enum COLOR {RED, GREEN, BLUE, GREY, ALL}

    private COLOR color;

    public ImageMatrix(BufferedImage bufferedImage, COLOR color, String imageNameWithPathName) {
        this.bufferedImage = bufferedImage;
        this.color = color;
        this.imageNameWithPathName = imageNameWithPathName;
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
        startWidth = 0;
        startHeight = 0;
    }
    public ImageMatrix(ImageMatrix m) {
        this.bufferedImage = m.bufferedImage;
        this.color = m.color;
        this.imageNameWithPathName = m.imageNameWithPathName;
        width = m.bufferedImage.getWidth();
        height = m.bufferedImage.getHeight();
        startWidth = 0;
        startHeight = 0;
    }

    public String getImageName() {
        return imageNameWithPathName;
    }

    @Override
    public double get(int j, int i) {
        int argb = bufferedImage.getRGB(i+startWidth, j+startHeight);
        switch (color) {
            case GREY: {
                return ((argb) & 0xFF + (argb >> 8) & 0xFF + (argb >> 16) & 0xFF)/3.0;
            }
            case BLUE: {
                return (argb) & 0xFF;
            }
            case GREEN: {
                return (argb >> 8) & 0xFF;
            }
            case RED: {
                return (argb >> 16) & 0xFF;
            }
            case ALL:{
                return ((argb) & 0xFF + (argb >> 8) & 0xFF + (argb >> 16) & 0xFF)/3.0;
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
            case ALL: {
                return "ALL";
            }
        }
        return "???";
    }

    public void setColor(COLOR color) {
        this.color = color;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
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

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public void setImageName(String imageName) {
        this.imageNameWithPathName = imageName;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setStartWidth(int startWidth) {
        this.startWidth = startWidth;
    }

    public void setStartHeight(int startHeight) {
        this.startHeight = startHeight;
    }

    public int getStartWidth() {
        return startWidth;
    }

    public int getStartHeight() {
        return startHeight;
    }


}
