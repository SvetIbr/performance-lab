import java.util.Arrays;
import java.util.Scanner;

public class CircularArray {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите два числа больше единицы через пробел (например, \"2 3\", " +
                "где 2 - конечное число кругового массива, а 3 - длина обхода.");

        try {
            int n = scanner.nextInt();
            int m = scanner.nextInt();
            int[] ar = new int[n];

            Arrays.setAll(ar, i -> ++i);

            int cur = 0;

            System.out.print("Путь: ");

            do {
                System.out.print(ar[cur]);
                cur = (cur + m - 1) % n;
            } while (cur != 0);

        } catch (Exception exception) {
            System.out.println("Введены неверные данные.");
        }
    }
}

