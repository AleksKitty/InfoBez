import java.util.ArrayList;

public class Lab5 {
    /*
     * Заданные параментры для 15 варианта
     */

    // кривая E751(-1,1), т.е. y^2 = x^3 - x + 1 (mod 751):
    private final static int a = -1;
    // генерирующая точка G = (0, 1):
    // x = 0, y = 0
    private final static int[] pointG = {0, 1};

    // что мы хотим закодировать и отправить
    private final static String text = "отставной";

    // точки Pm
    private final static int[][] letters_encoding = {{240, 309}, {247, 266}, {243, 664}, {247, 266},
            {228, 271}, {229, 151}, {238, 576}, {240, 309}, {236, 712}};

    // открытый ключ Pb
    final static int[] pointPb = {286, 136};

    /*
     * значения случайных чисел k:
     * k_values = {5, 3, 3, 2, 4, 19, 2, 4, 10};
     */


    /*
     * Шифрованный текст имеет вид Cm = {kG, Pm + kPb}
     * k - рандомное значение, которое выбирает отправитель
     * Pm - стандартная кодировка для каждого символа
     */
    private static class Cm {
        int kGX;
        int kGY;
        int sumPmkPbX;
        int sumPmkPbY;

        public Cm(int kGX, int kGY, int sumPmkPbX, int sumPmkPbY) {
            this.kGX = kGX;
            this.kGY = kGY;
            this.sumPmkPbX = sumPmkPbX;
            this.sumPmkPbY = sumPmkPbY;
        }

        @Override
        public String toString() {
            return "\nCm{" +
                    "kGX=" + kGX +
                    ", kGY=" + kGY +
                    ", sumPmkPbX=" + sumPmkPbX +
                    ", sumPmkPbY=" + sumPmkPbY +
                    "}";
        }
    }

    public static void main(String[] args) {

        ArrayList<Cm> arrayListCm = new ArrayList<>();

        int[][] kG = calculateK(pointG);
        int[][] kPb = calculateK(pointPb);

        for (int i = 0; i < text.length(); i++) {
            int[] sumPQ = calculateSumPQ(letters_encoding[i][0], letters_encoding[i][1], kPb[i][0], kPb[i][1]);

            arrayListCm.add(new Cm(kG[i][0], kG[i][1], sumPQ[0], sumPQ[1]));
        }
        System.out.println(arrayListCm);

    }

    /*
     * k_values = {5, 3, 3, 2, 4, 19, 2, 4, 10};
     */
    private static int[][] calculateK(int[] point) {

        int[][] resultFinal = new int[9][2];

        int[] result2 = calculateSumPP(point[0], point[1]);
        resultFinal[6] = result2;

        int[] result3 = calculateSumPQ(point[0], point[1] , result2[0], result2[1]);
        resultFinal[1] = result3;
        resultFinal[2] = result3;

        int[] result4 = calculateSumPP(result2[0], result2[1]);
        resultFinal[4] = result4;
        resultFinal[7] = result4;

        int[] result5 = calculateSumPQ(point[0], point[1], result4[0], result4[1]);
        resultFinal[0] = result5;

        int[] result10 = calculateSumPP(result5[0], result5[1]);
        resultFinal[8] = result10;

        int[] result8 = calculateSumPP(result4[0], result4[1]);

        int[] result16 = calculateSumPP(result8[0], result8[1]);

        int[] result19 = calculateSumPQ(result16[0], result16[1], result3[0], result3[1]);
        resultFinal[5] = result19;

        return resultFinal;
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
