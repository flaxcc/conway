import java.awt.*;
import java.io.Serializable;

public class Display implements Serializable {
    private int xScale;
    private int yScale;
    private static final int CANVAS_WIDTH = 966;
    private static final int CANVAS_HEIGHT = 650;
    private final Universe universe;
    private static Color color = Color.BLACK;

    public Display(Universe universe) {
        this.universe = universe;
    }

    public void show(int[][] matrix) {
        StdDraw.clear();
        for (int i = 1; i <= matrix.length; i++) {
            for (int j = 1; j <= matrix[0].length; j++) {
                if (matrix[i - 1][j - 1] == 1) {
                    StdDraw.point(i * xScale, j * yScale);
                }
            }
        }
        StdDraw.show();
    }

    public void close() {
        StdDraw.frame.dispose();
    }

    public void open() {
        xScale = CANVAS_WIDTH / universe.getMatrix()[0].length;
        yScale = CANVAS_HEIGHT / universe.getMatrix().length;
        StdDraw.setCanvasSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        StdDraw.setXscale(0, CANVAS_WIDTH - 1);
        StdDraw.setYscale(0, CANVAS_HEIGHT - 1);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenRadius(0.001 * xScale);
        StdDraw.setPenColor(color);
    }

    public static void setColor(Color color) {
        Display.color = color;
    }
}
