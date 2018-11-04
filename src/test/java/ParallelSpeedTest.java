import org.junit.jupiter.api.Test;

public class ParallelSpeedTest {

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
        tester.run();
        stopTime = System.currentTimeMillis();
        elapsedTime1 = stopTime - startTime;

        startTime = System.currentTimeMillis();
        Constans.parallel=false;
        tester.run();
        stopTime = System.currentTimeMillis();
        elapsedTime2 = stopTime - startTime;
        System.out.println("PARALELL: " + elapsedTime1);
        System.out.println("NON PARALLEL: " + elapsedTime2);
    }
}
