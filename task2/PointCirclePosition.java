import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class PointCirclePosition {
    public static void main(String[] args) {
        System.out.println("Введите 2 аргумента, каждый с новой строки: " +
                "файл с координатами и радиусом окружности, " +
                "файл с координатами точек.");
        Scanner scanner = new Scanner(System.in);
        String firstFile = scanner.nextLine();
        String secondFile = scanner.nextLine();

        try (BufferedReader circleReader = new BufferedReader(new FileReader(firstFile))) {
            // Считываю координаты центра окружности и радиуса
            String[] circleData = circleReader.readLine().split(" ");
            double circleCenterX = Double.parseDouble(circleData[0]);
            double circleCenterY = Double.parseDouble(circleData[1]);
            double radius = Double.parseDouble(circleReader.readLine());

            // Считываю координаты точек и проверяю их количество и положение
            List<String> reader = Files.readAllLines(Path.of(secondFile));

            if (reader.isEmpty() || reader.size() > 100) {
                System.out.println("Количество точек должно быть от 1 до 100.");
                return;
            }

            for (String element : reader) {
                String[] pointData = element.split(" ");
                double pointX = Double.parseDouble(pointData[0]);
                double pointY = Double.parseDouble(pointData[1]);
                int position = calculatePointPosition(circleCenterX, circleCenterY, radius, pointX, pointY);
                System.out.println(position);
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
    }

    private static int calculatePointPosition(double circleCenterX, double circleCenterY, double radius,
                                              double pointX, double pointY) {
        double distance = Math.sqrt(Math.pow(pointX - circleCenterX, 2) + Math.pow(pointY - circleCenterY, 2));
        if (distance == radius) {
            return 0; // Точка на окружности
        } else if (distance < radius) {
            return 1; // Точка внутри
        } else {
            return 2; // Точка снаружи
        }
    }
}


