import org.junit.jupiter.api.Test;

public class GTDMSpeedTest {

    @Test
    public void test() {
        Controller controller = new Controller();
        System.out.println("D: " + Constans.D);
        System.out.println("Quadratic: " + Constans.QUADRATIC_SIZE);
        System.out.println("Average matrixes?: " + Constans.AVERAGE_MATRIXES);
        long startTime;
        long stopTime;
        long elapsedTime1;
        long elapsedTime2;

        startTime = System.currentTimeMillis();
        Constans.parallel=true;
        Constans.slowGTDMcalc=true;
        controller.run();
        stopTime = System.currentTimeMillis();
        elapsedTime1 = stopTime - startTime;

        startTime = System.currentTimeMillis();
        Constans.slowGTDMcalc=false;
        controller.run();
        stopTime = System.currentTimeMillis();
        elapsedTime2 = stopTime - startTime;
        System.out.println("SLOW GTDM: " + elapsedTime1);
        System.out.println("SPEED GTDM: " + elapsedTime2);
    }
}
