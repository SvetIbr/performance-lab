import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите путь к файлу с элементами массива");
        Scanner scan = new Scanner(System.in);
        String path = scan.nextLine();
        scan.close();

        try (Scanner scanner = new Scanner(new File(path))) {
            int[] nums = readNumbersFromFile(scanner);
            int minMoves = calculateMinMoves(nums);
            System.out.println(minMoves);

        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + e.getMessage());
        }
    }

    private static int[] readNumbersFromFile(Scanner scanner) {
        List<Integer> nums = new ArrayList<>();
        while (scanner.hasNextInt()) {
            int number = scanner.nextInt();
            nums.add(number);
        }
        scanner.reset();
        Collections.sort(nums);
        return nums.stream().mapToInt(i -> i).toArray();
    }

    private static int calculateMinMoves(int[] nums) {
        int target = findMedian(nums);
        int minMoves = 0;
        for (int num : nums) {
            minMoves += Math.abs(num - target);
        }
        return minMoves;
    }

    private static int findMedian(int[] nums) {
        int n = nums.length;
        if (n % 2 == 0) {
            return (nums[n / 2 - 1] + nums[n / 2]) / 2; // Среднее двух средних элементов
        } else {
            return nums[n / 2]; // Средний элемент
        }
    }
}
