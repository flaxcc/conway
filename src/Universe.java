import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

public class Universe implements Serializable {
    private int[][] matrix;
    private final Display display;
    public transient boolean isStopped = true;

    public Universe(int dimx, int dimy) {
        matrix = new int[dimy][dimx];
        display = new Display(this);
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
        return result;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void start() {
        isStopped = false;
        display.open();
        CompletableFuture<Void> future = CompletableFuture.runAsync(this::show, ForkJoinPool.commonPool());
        while (!isStopped) {
            matrix = calculateNextGeneration();
            future.thenRun(this::show);
        }
    }

    public void stop() {
        isStopped = true;
        display.close();
    }

    public void show() {
        if (isStopped) {
            display.open();
        }
        display.show(matrix);
    }

    /**
     * Этот метод нужен для заполнения поля случайным расселением
     */
    public void initializeRandom() {
        Random random = new Random();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                int generatedValue = random.nextInt(2);
                matrix[i][j] = generatedValue;
            }
        }

    }


    public void readFromFile(String s) {
        Optional<List<String>> lines = Optional.empty();
        try {
            lines = Optional.of(Files.readAllLines(Paths.get(s)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        lines.ifPresent(value -> {
            for (String line : value) {
                String[] split = line.split(",");
                int i = Integer.parseInt(split[0].trim());
                int j = Integer.parseInt(split[1].trim());
                matrix[i][j] = 1;
            }
        });

    }
}
