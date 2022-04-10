import utils.StdDraw;

import java.awt.*;
import java.io.Serializable;
import java.util.concurrent.*;

public class Display implements Serializable {
    private final int CANVAS_WIDTH = 966;
    private final int CANVAS_HEIGHT = 650;
    private int xScale;
    private int yScale;
    private Universe universe;
    private BlockingQueue<int[][]> framesQueue = new LinkedBlockingQueue<>();

    public Display(Universe universe) {
        this.universe = universe;
        xScale = CANVAS_WIDTH / universe.getMatrix()[0].length;
        yScale = CANVAS_HEIGHT / universe.getMatrix().length;
        StdDraw.setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        StdDraw.setXscale(0, CANVAS_WIDTH - 1);
        StdDraw.setYscale(0, CANVAS_HEIGHT - 1);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.001 * xScale);
        StdDraw.setPenColor(Color.BLACK);

    }

    public void show(int[][] matrix) {
        long start = System.currentTimeMillis();
        StdDraw.clear();
        for (int i = 1; i <= matrix.length; i++) {
            for (int j = 1; j <= matrix[0].length; j++) {
                if (matrix[i - 1][j - 1] == 1) {
                    StdDraw.point(i * xScale, j * yScale);
                }

            }

        }
        StdDraw.show();
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public void showForever() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        executor.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(1000/25);
                    int[][] nextFrame = framesQueue.poll();
                    CompletableFuture.runAsync(() -> show(nextFrame));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void push(int[][] nextFrame) {
        framesQueue.add(nextFrame);
    }
}
