
public class Lab5 {
    /*
     * Заданные параментры для 15 варианта
     */

    // кривая E751(-1,1), т.е. y = x^3 - x + 1 (mod 751)
    // генерирующая точка G = (0, 1)

    // что мы хотим закодировать и отправить
    final static String text = "отставной";

    final int[][] letters_encoding = {{240, 309}, {247, 266}, {243, 664}, {247, 266},
            {228, 271}, {229, 151}, {238, 576}, {240, 309}, {236, 712}};

    // открытый ключ Pb
    final int[] keyPb = {286, 136};

    // значения случайных чисел k
    final int[] k_values = {5, 3, 3, 2, 4, 19, 2, 4, 10};

    public static void main(String[] args) {
        for (int i = 0; i < text.length(); i++) {
            
        }
    }

    /*
     * Шифрованный текст имеет вид Cm = {kG, Pm + kPb}
     * k - рандомное значение, которое выбирает отправитель
     * Pm - стандартная кодировка для каждого символа (66, 552) для лат. буквы "A"
     */
    public static void calculateCm() {

    }

    /*
     * формула lambda = (3 * x1^2 + a) / 2 * y1
     * x1 = 0, y1 = 1, т.к. G = (0, 1)
     * a = -1, т.к. y = x^3 - x + 1
     */
    public static int[] calculate2G() {
        double lambda = -1.0 / 2.0; // == 375mod751

        // x3 = lambda^2 - x1 - x2 (x1 = 0, x2 =0)
        int x3 = 140625; // 188mod751
        // y3 = lambda * (x1 - x3) - y1 (x1 = 0, x3 = 188, y1 = 1)
        int y3 = -70501; // 93mod751

        return new int[]{188, 93};
    }

    /*
     * формула lambda = (y2 - y1) / (x2 - x1)
     */
    public static int[] calculate3G() {
        double lambda = 188.0 / 92.0; // == 368mod751
        int x3 = 135236; // 56mod751
        int y3 = 20607; // 419mod751

        return new int[]{56, 419};
    }
}
