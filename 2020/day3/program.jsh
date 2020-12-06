import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;

try (Scanner sc = new Scanner(new FileInputStream("C:\\data.txt"))) {
    String line = sc.nextLine();
    final int length = line.length();
    int initialTreeCount = line.charAt(0) == '#' ? 1 : 0;
    int[] index = {0, 0, 0, 0, 0};
    int[][] slopes = {{1, 1}, {3, 1}, {5, 1}, {7, 1}, {1, 2}};
    long[] counts = {initialTreeCount, initialTreeCount, initialTreeCount, initialTreeCount, initialTreeCount};
    int row = 1;

    while (sc.hasNextLine()) {
        line = sc.nextLine();
        for (int i = 0; i < index.length; i++) {
            if (row % slopes[i][1] == 0) {
                index[i] = (index[i] + slopes[i][0]) % length;
                if (line.charAt(index[i]) == '#') {
                    counts[i]++;
                }
            }
        }
        row++;
    }

    long result = Arrays.stream(counts).reduce(1, (a, b) -> a * b);

    System.out.println(counts[1]);
    System.out.println(result);
}
