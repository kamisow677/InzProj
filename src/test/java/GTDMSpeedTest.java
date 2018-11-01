import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GTDMSpeedTest {

    @Test
    public void test() {
        Tester3 tester = new Tester3();
        System.out.println("D: " + Constans.getD());
        System.out.println("Quadratic: " + Constans.getQuadraticSize());
        System.out.println("Average matrixes?: " + Constans.isAverageMatrixes());

        long startTime;
        long stopTime;
        long elapsedTime;
        long calc1 = 0;
        startTime = System.currentTimeMillis();

        tester.run();

        stopTime = System.currentTimeMillis();
        elapsedTime = stopTime - startTime;
        calc1 += elapsedTime;

        System.out.println("ZEWNATRZ: " + calc1);
    }
}
