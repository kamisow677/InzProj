import org.junit.jupiter.api.Test;

public class GTDMSpeedTest {

    @Test
    public void test() {
        Tester3 tester = new Tester3();
        System.out.println("D: " + Constans.getD());
        System.out.println("Quadratic: " + Constans.getQuadraticSize());
        System.out.println("Average matrixes?: " + Constans.isAverageMatrixes());
        long startTime;
        long stopTime;
        long elapsedTime1;
        long elapsedTime2;

        startTime = System.currentTimeMillis();
        Constans.parallel=true;
        Constans.slowGTDMcalc=true;
        tester.run();
        stopTime = System.currentTimeMillis();
        elapsedTime1 = stopTime - startTime;

        startTime = System.currentTimeMillis();
        Constans.slowGTDMcalc=false;
        tester.run();
        stopTime = System.currentTimeMillis();
        elapsedTime2 = stopTime - startTime;
        System.out.println("SLOW GTDM: " + elapsedTime1);
        System.out.println("SPEED GTDM: " + elapsedTime2);
    }
}
