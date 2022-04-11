import utils.StdDraw;

import java.awt.*;
import java.io.Serializable;

public class Display implements Serializable {
    private final int xScale;
    private final int yScale;
    private static final int CANVAS_WIDTH = 966;
    private static final int CANVAS_HEIGHT = 650;

    public Display(Universe universe) {
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
        long start = System.nanoTime();
        StdDraw.clear();
        for (int i = 1; i <= matrix.length; i++) {
            for (int j = 1; j <= matrix[0].length; j++) {
                if (matrix[i - 1][j - 1] == 1) {
                    StdDraw.point(i * xScale, j * yScale);
                }

            }

        }
        long end = System.nanoTime();
        System.out.println(Application.getHumanReadableTime(end - start));
    }

}
