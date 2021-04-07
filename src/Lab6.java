import java.util.ArrayList;

public class Lab6 {
    /*
     * Заданные параментры для 15 варианта
     */

    // кривая E751(-1,1), т.е. y^2 = x^3 - x + 1 (mod 751):
    private final static int a = -1;
    // генерирующая точка G = (0, 1):
    // x = 0, y = 0
    private final static int[] pointG = {0, 1};

    // секретный ключ
    // nb = 27
    // открытый ключ Pb:
    // pointPb = nb * pointG

    // шифрованный текст, который необходимо декодировать
    private static final ArrayList<Cm> arrayListCm = new ArrayList<>();

    private final static int[] data = {
            618, 206, 99, 456,
            425, 663, 31, 136,
            377, 456, 688, 741,
            425, 663, 636, 747,
            16, 416, 298, 526,
            188, 93, 356, 175,
            489, 468, 147, 390,
            346, 242, 546, 670,
            72, 254, 114, 144,
            377, 456, 25, 147};



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
        initializeListAndPb();

        int[][] resultText = new int[arrayListCm.size()][2];


        // Шифрованный текст имеет вид Cm = {kG, Pm + kPb}
        // (Pm + kPb) - nb(kG) = расшифрованная буква
        // nb = 27

        for (int i = 0; i < arrayListCm.size(); i++) {
            int[] nbMultiplykG = calculateNbMultiplyPoint(arrayListCm.get(i).kGX, arrayListCm.get(i).kGY);

            // point = (x, y) -> -point = (x, -y)
            resultText[i] = calculateSumPQ(arrayListCm.get(i).sumPmkPbX, arrayListCm.get(i).sumPmkPbY, nbMultiplykG[0], -nbMultiplykG[1]);

            System.out.println("(" + resultText[i][0] + ", " + resultText[i][1] +")");
        }
    }

    private static void initializeListAndPb() {
        for (int i = 0; i < data.length; i += 4) {
            arrayListCm.add(new Cm(data[i], data[i + 1], data[i + 2], data[i + 3]));
        }
    }


    private static int[] calculateNbMultiplyPoint(int x, int y) {

        int[] result2G = calculateSumPP(x, y);
        int[] result3G = calculateSumPQ(x, y, result2G[0], result2G[1]);
        int[] result4G = calculateSumPP(result2G[0], result2G[1]);
        int[] result8G = calculateSumPP(result4G[0], result4G[1]);
        int[] result16G = calculateSumPP(result8G[0], result8G[1]);
        int[] result32G = calculateSumPP(result16G[0], result16G[1]);
        int[] result24G = calculateSumPQ(result16G[0], result16G[1] , result8G[0], result8G[1]);

        return calculateSumPQ(result24G[0], result24G[1] , result3G[0], result3G[1]);
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
