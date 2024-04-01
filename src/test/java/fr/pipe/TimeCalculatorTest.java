package fr.pipe;

import fr.lacaleche.core.utils.logger.Logger;

public class TimeCalculatorTest {

    public static int calculateDelay(int total, int from, int to) {
        int directDistance = to - from;
        return Math.abs((directDistance >= 0) ? directDistance : total + directDistance);
    }

    public static void main(String[] args) {
        Logger.info("calculateDelay(24000, 18000, 0) == 6000 = %s", calculateDelay(24000, 18000, 0) == 6000);
        Logger.info("calculateDelay(24000, 17000, 18000) == 1000 = %s", calculateDelay(24000, 17000, 18000) == 1000);
        Logger.info("calculateDelay(24000, 18000, 17000) == 23000 = %s", calculateDelay(24000, 18000, 17000) == 23000);
    }

    private static boolean expect(int a, int b) {
        return a == b;
    }

}
