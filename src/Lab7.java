
public class Lab7 {
    /*
     * Заданные параментры для 15 варианта
     */

    // кривая E751(-1,1), т.е. y = x^3 - x + 1 (mod 751):
    private final static int a = -1;

    // 2P + 3Q – R
    private final static int[] P = {67, 84};
    private final static int[] Q = {69, 241};
    // -R: {66, -199}
    private final static int[] R = {66, 199};


    public static void main(String[] args) {
        int[] result2P = calculateSumPP(P[0], P[1]);

        int[] result2Q = calculateSumPP(Q[0], Q[1]);
        int[] result3Q = calculateSumPQ(Q[0], Q[1], result2Q[0], result2Q[1]);

        int[] resultSum2P3Q = calculateSumPQ(result2P[0], result2P[1], result3Q[0], result3Q[1]);

        int[] resultSum2P3QMinusR = calculateSumPQ(resultSum2P3Q[0], resultSum2P3Q[1], R[0], -R[1]);

        System.out.println(resultSum2P3QMinusR[0] + " " + resultSum2P3QMinusR[1]);
    }


    /*
     * формула lambda = (3 * x1^2 + a) / 2 * y1
     * сумма двух одинаковых (P + P)
     */
    public static int[] calculateSumPP (int x1, int y1) {

        int lambdaDown = 2 * y1;

        // lambdaUp = a
        int lambda = calculateMod(a, lambdaDown);

        // x1 == x2
        // y1 == y2
        return calculateX3Y3(lambda, x1, y1, x1);
    }

    /*
     * формула lambda = (y2 - y1) / (x2 - x1)
     * сумма двух разных (P + Q)
     */
    public static int[] calculateSumPQ (int x1, int y1, int x2, int y2) {
        int lambdaUp = (y2 - y1);
        int lambdaDown = (x2 - x1);

        int lambda = calculateMod(lambdaUp, lambdaDown);

        return calculateX3Y3(lambda, x1, y1, x2);
    }

    /*
     * x3 = lambda^2 - x1 - x2
     * y3 = lambda * (x1 - x3) - y1
     */
    public static int[] calculateX3Y3(int lambda, int x1, int y1, int x2) {
        int x3 = lambda * lambda - x1 - x2;
        int x3Lambda = calculateMod(x3, 1);

        int y3 = lambda * (x1 - x3) - y1;
        int y3Lambda = calculateMod(y3, 1);

        return new int[]{x3Lambda, y3Lambda};
    }

    public static int calculateMod(int up, int down) {
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
