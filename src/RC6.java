import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RC6 {
    /*
     * w - размер слова
     */
    private static final int w = 32;

    /*
     * r - кол-во раундов
     */
    private static final int r = 20;

    /*
     * константы для ключа при w = 32
     */
    private final static int Pw = 0xb7e15163, Qw = 0x9e3779b9;

    // CODE TO CONVERT HEXADECIMAL NUMBERS IN STRING TO BYTE ARRAY
    /*
     * конвертация
     * ключ в массив байтов
     */
    private static byte[] hexStringToByteArray(String s) {
        int string_len = s.length();
        byte[] data = new byte[string_len / 2];
        for (int i = 0; i < string_len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /*
     * конвертация массива байтов в 16-ый формат
     */
    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    /*
     * алгоритм подготовки ключа
     */
    private static int[] keySchedule(byte[] key) {

        int[] S = new int[2 * r + 4];
        S[0] = Pw;

        int c = key.length / (w / 8); // определение длины массива L
        int[] L = bytesToWords(key,  c);

        for (int i = 1; i < (2 * r + 4); i++){
            S[i] = S[i - 1] + Qw;
        }

        // перемешивание ключа
        int A, B, i, j;
        A = B = i = j = 0;

        int v = 3 * Math.max(c, (2 * r + 4));

        for (int s = 0; s < v; s++) {
            A = S[i] = rotateLeft((S[i] + A + B), 3);
            B = L[j] = rotateLeft(L[j] + A + B, A + B);
            i = (i + 1) % (2 * r + 4);
            j = (j + 1) % c;
        }

        return S;
    }

    /*
     * алгоритм шифрования данных с регистрами ABCD
     */
    private static byte[] encryption(byte[] keySchArray, int[] S){

        int temp, t, u;

        int[] temp_data = new int[keySchArray.length / 4];

        temp_data = convertByteToInt(keySchArray,temp_data.length);

        int A = temp_data[0];
        int B = temp_data[1];
        int C = temp_data[2];
        int D = temp_data[3];

        B = B + S[0];
        D = D + S[1];

        int lgw = 5;

        for(int i = 1; i <= r; i++){

            t = rotateLeft(B * (2 * B + 1),lgw);
            u = rotateLeft(D * (2 * D + 1),lgw);
            A = rotateLeft(A ^ t, u) + S[2 * i];
            C = rotateLeft(C ^ u, t) + S[2 * i + 1];

            temp = A;
            A = B;
            B = C;
            C = D;
            D = temp;
        }

        A = A + S[2 * r + 2];
        C = C + S[2 * r + 3];

        temp_data[0] = A;
        temp_data[1] = B;
        temp_data[2] = C;
        temp_data[3] = D;

        return convertIntToByte(temp_data,keySchArray.length);
    }

    /*
     * алгоритм дешифрования данных с регистрами ABCD
     */
    public static byte[] decryption(byte[] keySchArray, int[] S){

        int temp, t, u;
        int A, B, C, D;

        int[] temp_data_decryption = new int[keySchArray.length / 4];

        temp_data_decryption = convertByteToInt(keySchArray, temp_data_decryption.length);

        A = temp_data_decryption[0];
        B = temp_data_decryption[1];
        C = temp_data_decryption[2];
        D = temp_data_decryption[3];

        C = C - S[2 * r + 3];
        A = A - S[2 * r + 2];

        int lgw = 5;

        for(int i = r; i >= 1; i--){
            temp = D;
            D = C;
            C = B;
            B = A;
            A = temp;

            u = rotateLeft(D * (2 * D + 1), lgw);
            t = rotateLeft(B * (2 * B + 1), lgw);
            C= rotateRight(C - S[2 * i + 1],t) ^ u;
            A= rotateRight(A - S[2 * i], u) ^ t;
        }

        D = D - S[1];
        B = B - S[0];

        temp_data_decryption[0] = A;
        temp_data_decryption[1] = B;
        temp_data_decryption[2] = C;
        temp_data_decryption[3] = D;

        return convertIntToByte(temp_data_decryption,keySchArray.length);
    }

    /*
     * преобразование массива int в массив байт
     */
    private static byte[] convertIntToByte(int[] integerArray,int length){
        byte[] int_to_byte = new byte[length];

        for(int i = 0; i<length; i++){
            int_to_byte[i] = (byte)((integerArray[i/4] >>> (i % 4) * 8) & 0xff);
        }

        return int_to_byte;
    }

    /*
     * преобразование массива байт в массив int
     */
    private static int[] convertByteToInt(byte[] arr, int length){
        int[]  byte_to_int = new int[length];

        int counter = 0;
        for(int i = 0; i < byte_to_int.length; i++){
            byte_to_int[i] = ((arr[counter++] & 0xff))|
                    ((arr[counter++] & 0xff) << 8) |
                    ((arr[counter++] & 0xff) << 16) |
                    ((arr[counter++] & 0xff) << 24);
        }

        return byte_to_int;
    }

    /*
     * преобразоване массива байтов, в массив слов по 32 бита (w = 32)
     */
    private static int[] bytesToWords(byte[] userKey, int c) {
        int[] bytes_to_words = new int[c];

        int off = 0;
        for (int i = 0; i < c; i++) {
            bytes_to_words[i] = ((userKey[off++] & 0xFF)) | ((userKey[off++] & 0xFF) << 8)
                    | ((userKey[off++] & 0xFF) << 16) | ((userKey[off++] & 0xFF) << 24);
        }

        return bytes_to_words;
    }

    /*
     * сдвиг влево
     */
    private static int rotateLeft(int val, int pas) {
        return (val << pas) | (val >>> (32 - pas));
    }

    /*
     * сдвиг вправо
     */
    private static int rotateRight(int val, int pas) {
        return (val >>> pas) | (val << (32-pas));
    }

    private static int xorFunction() {

        return 0;
    }

    /*
     * алгоритм шифрования 32 шестнадцатиричных цифр (128 бит)
     */
    public static void main(String[] args) {
        BufferedWriter output_to_text_file = null;

        try {
            FileReader input_file = new FileReader("input2.txt"); // данные для шифрования/расшифровки в шестнадцатиричном виде
            FileWriter output_file = new FileWriter("output.txt",false); // зашифрованный/расшифрованный текст

            BufferedReader bf = new BufferedReader(input_file);
            String choice = bf.readLine();

            String given_text = bf.readLine();
            String[] input_text_val = given_text.split(":");
            String text_data = input_text_val[1];
            String key_value = bf.readLine();
            String[] input_key_val = key_value.split(":");
            String tmpString = input_key_val[1];

            tmpString = tmpString.replace(" ", "");
            text_data = text_data.replace(" ", "");

            // read vector
            FileReader vector_file = new FileReader("vector.txt"); // вектор инициализации в шестнадцатиричном виде
            BufferedReader bVector = new BufferedReader(vector_file);
            String vector = bVector.readLine();
            byte[] vectorByte = hexStringToByteArray(vector);


            byte[] key = hexStringToByteArray(tmpString);
            byte[] text = hexStringToByteArray(text_data);
            int[] keySchedule = keySchedule(key);
            if (choice.equals("Encryption")){

                byte[] encrypt = encryption(text, keySchedule);
                String encrypted_text = byteArrayToHex(encrypt);
                encrypted_text = encrypted_text.replaceAll("..", "$0 ");
                output_to_text_file = new BufferedWriter(output_file);
                output_to_text_file.write("ciphertext: " + encrypted_text);
            }
            else if (choice.equals("Decryption")) {

                byte[] decrypt = decryption(text, keySchedule);
                String decrypted_text = byteArrayToHex(decrypt);
                decrypted_text = decrypted_text.replaceAll("..", "$0 ");
                output_to_text_file = new BufferedWriter(output_file);
                output_to_text_file.write("plaintext: "+ decrypted_text);

            } else {
                System.out.println("Invalid option");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found exception");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO exception");
            e.printStackTrace();
        }
        finally {
            if (output_to_text_file != null) {
                try {
                    output_to_text_file.close();
                } catch (IOException e) {
                    System.out.println("File cannot be closed");
                    e.printStackTrace();
                }
            }
        }
    }
}
