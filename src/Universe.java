import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Universe implements Serializable {
    @Serial
    private final long id = 111;
    private final int DIMENSION_X;
    private final int DIMENSION_Y;
    private int[][] matrix;
    private Display display;

    public Universe(int dimx, int dimy) {
        DIMENSION_X = dimx;
        DIMENSION_Y = dimy;
        matrix = new int[DIMENSION_Y][DIMENSION_X];
        display = new Display (this);



    }

    public boolean isAlive(int i, int j) {
        int count = 0;
        for (int k = -1; k <= 1; k++) {
            for (int l = -1; l <= 1; l++) {
                if (k == 0 && l == 0) {
                    continue;
                }
                int rowIndex = i + k;
                int columnIndex = j + l;
                if (rowIndex < 0) {
                    rowIndex = matrix.length + rowIndex;
                }
                if (rowIndex >= matrix.length) {
                    rowIndex = matrix.length - rowIndex;
                }
                if (columnIndex < 0) {
                    columnIndex = matrix[0].length + columnIndex;
                }
                if (columnIndex >= matrix.length) {
                    columnIndex = matrix[0].length - columnIndex;
                }


                count += matrix[rowIndex][columnIndex];

            }
        }
        return matrix[i][j] == 1 ? count == 2 || count == 3 : count == 3;


    }

    public int[][] calculateNextGeneration() {
        long start = System.currentTimeMillis();
        int[][] result = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if (isAlive(i, j)) {
                        result[i][j] = 1;
                    } else {
                        result[i][j] = 0;
                    }
                }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return result;
    }

    public void initializeRandom() {
        Random random = new Random();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                int generatedValue = random.nextInt(2);
                matrix[i][j] = generatedValue;
            }
        }

    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void start() {
        while (true) {
            display.show();
            matrix = calculateNextGeneration();
        }
    }

    public void show() {
        display.show();
    }


    public void readFromFile(String s) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(s));
        for (String line : lines) {
            String[] split = line.split(",");
            int i = Integer.parseInt(split[0].trim());
            int j = Integer.parseInt(split[1].trim());
            matrix[i][j] = 1;
        }
    }
}
