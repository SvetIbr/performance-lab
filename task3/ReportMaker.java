import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

public class ReportMaker {
    public static void main(String[] args) {
        System.out.println("Введите 3 аргумента, каждый с новой строки: " +
                "файл с результатами прохождения тестов, " +
                "файл со структурой для построения отчета, " +
                "файл для записи результата");
        Scanner scanner = new Scanner(System.in);
        String valuesPath = scanner.nextLine();
        String testsPath = scanner.nextLine();
        String reportPath = scanner.nextLine();

        try {
            // Загрузка данных из values.json
            Map<Integer, String> valuesMap = loadValues(valuesPath);

            // Загрузка данных из tests.json
            Map<Integer, Result> testsMap = loadTests(testsPath);

            // Заполнение полей "value" в tests.json
            writeValues(valuesMap, testsMap);

            // Сохранение результата в report.json
            saveJsonToFile(testsMap, reportPath);

            System.out.println("Отчет успешно сформирован в файле: " + reportPath);

        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлами: " + e.getMessage());
        }
    }

    private static Map<Integer, String> loadValues(String filePath) throws IOException {
        Map<Integer, String> valuesMap = new HashMap<>();
        JsonElement jsonElement = JsonParser.parseReader(new FileReader(filePath));
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray valuesArray = jsonObject.getAsJsonArray("values");

        for (JsonElement cur : valuesArray) {
            JsonObject valueObject = cur.getAsJsonObject();
            int id = valueObject.get("id").getAsInt();
            String value = valueObject.get("value").getAsString();
            valuesMap.put(id, value);
        }
        return valuesMap;
    }

    // Загрузка данных из tests.json
    private static Map<Integer, Result> loadTests(String filePath) throws IOException {
        Map<Integer, Result> valuesMap = new HashMap<>();
        JsonElement jsonElement = JsonParser.parseReader(new FileReader(filePath));
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonArray testsArray = jsonObject.getAsJsonArray("tests");

        for (JsonElement testElement : testsArray) {
            JsonObject testObject = testElement.getAsJsonObject();
            int id = testObject.get("id").getAsInt();
            String title = testObject.get("title").getAsString();
            String value = testObject.get("value").getAsString();

            List<Result> values = null;
            if (testObject.has("values")) {
                JsonArray valuesArray = testObject.getAsJsonArray("values");
                Type resultType = new TypeToken<List<Result>>() {
                }.getType();
                values = new Gson().fromJson(valuesArray, resultType);
            }

            Result result = new Result(id, title, value, values);
            valuesMap.put(id, result);
        }
        return valuesMap;
    }

    // Заполнение полей "value" в tests.json
    private static void writeValues(Map<Integer, String> valuesMap,
                                    Map<Integer, Result> testsMap) {
        for (Result cur : testsMap.values()) {
            if (valuesMap.containsKey(cur.id)) {
                loadValue(cur, valuesMap.get(cur.id));
            }
            if (cur.values != null && !cur.values.isEmpty()) {
                cur.values.forEach(Result -> loadValue(Result, valuesMap.get(Result.id)));
            }
        }
    }

    // Сохранение данных в report.json
    private static void saveJsonToFile(Map<Integer, Result> map, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Result> list = new ArrayList<>(map.size());
            list.addAll(map.values());
            gson.toJson(list, writer);
        } catch (IOException ex) {
            System.out.println("Ошибка записи файла: " + ex.getMessage());
        }
    }

    private static void loadValue(Result cur, String value) {
        cur.value = value;
    }
}

class Result {
    int id;
    String title;
    String value;
    List<Result> values;

    public Result(int id, String title, String value, List<Result> values) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.values = values;
    }
}
