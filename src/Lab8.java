
public class Lab8 {
    /*
     * Заданные параментры для 15 варианта
     */

    // кривая E751(-1,1), т.е. y^2 = x^3 - x + 1 (mod 751):
    private final static int a = -1;

    // искомое: 126 * P
    private final static int[] P = {49, 183};


    public static void main(String[] args) {
        int[] result2P = calculateSumPP(P[0], P[1]);
        int[] result4P = calculateSumPP(result2P[0], result2P[1]);
        int[] result8P = calculateSumPP(result4P[0], result4P[1]);
        int[] result16P = calculateSumPP(result8P[0], result8P[1]);
        int[] result32P = calculateSumPP(result16P[0], result16P[1]);
        int[] result64P = calculateSumPP(result32P[0], result32P[1]);
        int[] result128P = calculateSumPP(result64P[0], result64P[1]);
        int[] result126P = calculateSumPQ(result128P[0], result128P[1], result2P[0], result2P[1]);

        System.out.println(result126P[0] + " " + result126P[1]);
    }


    /*
     * формула lambda = (3 * x1^2 + a) / 2 * y1
     * сумма двух одинаковых (P + P)
     */
    private static int[] calculateSumPP (int x1, int y1) {

        int lambdaUp = 3 * x1 * x1 + a;
        int lambdaDown = 2 * y1;

        int lambda = calculateMod(lambdaUp, lambdaDown);

        // x1 == x2
        // y1 == y2
        return calculateX3Y3(lambda, x1, y1, x1);
    }

    /*
     * формула lambda = (y2 - y1) / (x2 - x1)
     * сумма двух разных (P + Q)
     */
    private static int[] calculateSumPQ (int x1, int y1, int x2, int y2) {
        int lambdaUp = (y2 - y1);
        int lambdaDown = (x2 - x1);

        int lambda = calculateMod(lambdaUp, lambdaDown);

        return calculateX3Y3(lambda, x1, y1, x2);
    }

    /*
     * x3 = lambda^2 - x1 - x2
     * y3 = lambda * (x1 - x3) - y1
     */
    private static int[] calculateX3Y3(int lambda, int x1, int y1, int x2) {
        int x3 = lambda * lambda - x1 - x2;
        int x3Lambda = calculateMod(x3, 1);

        int y3 = lambda * (x1 - x3) - y1;
        int y3Lambda = calculateMod(y3, 1);

        return new int[]{x3Lambda, y3Lambda};
    }

    private static int calculateMod(int up, int down) {
        while (up % down != 0) {
            up += 751;
        }

        int lambda = (up / down) % 751;

        if (lambda < 0) {
            lambda += 751;
        }

        return lambda;
    }
}
