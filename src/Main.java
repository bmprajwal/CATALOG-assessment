import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String filePath = "src/test_1.json";
        try {
            Map<Integer, BigInteger> points = readJsonFile(filePath);
            BigInteger c = findConstant(points);
            System.out.println("c: " + c);
        } catch (IOException | JSONException e) {
            System.out.println("Error reading the JSON file: " + e.getMessage());
        }
    }

    private static Map<Integer, BigInteger> readJsonFile(String filePath) throws IOException, JSONException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject root = new JSONObject(content);

        Map<Integer, BigInteger> points = new HashMap<>();

        for (String key : root.keySet()) {
            if (key.equals("keys")) continue;

            JSONObject point = root.getJSONObject(key);
            int x = Integer.parseInt(key);
            int base = Integer.parseInt(point.getString("base"));
            BigInteger y = new BigInteger(point.getString("value"), base);

            points.put(x, y);
        }
        return points;
    }

    private static BigInteger findConstant(Map<Integer, BigInteger> points) {
        int n = points.size();
        int[] x = new int[n];
        BigInteger[] y = new BigInteger[n];
        int index = 0;

        for (Map.Entry<Integer, BigInteger> entry : points.entrySet()) {
            x[index] = entry.getKey();
            y[index] = entry.getValue();
            index++;
        }

        BigInteger c = BigInteger.ZERO;
        for (int i = 0; i < n; i++) {
            BigInteger term = y[i];
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    term = term.multiply(BigInteger.valueOf(0 - x[j]))
                            .divide(BigInteger.valueOf(x[i] - x[j]));
                }
            }
            c = c.add(term);
        }

        return c;
    }
}
