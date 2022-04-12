import javax.swing.*;
import java.util.concurrent.TimeUnit;

public class Application {

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainMenu");
        frame.setContentPane(new MainMenu().getRootPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    public static String getHumanReadableTime(long nanos) {
        TimeUnit unitToPrint = null;
        StringBuilder result = new StringBuilder();
        long rest = nanos;
        for (TimeUnit t : TimeUnit.values()) {
            if (unitToPrint == null) {
                unitToPrint = t;
                continue;
            }
            // convert 1 of "t" to "unitToPrint", to get the conversion factor
            long factor = unitToPrint.convert(1, t);
            long value = rest % factor;
            rest /= factor;

            result.insert(0, value + " " + unitToPrint + " ");

            unitToPrint = t;
            if (rest == 0) {
                break;
            }
        }
        if (rest != 0) {
            result.insert(0, rest + " " + unitToPrint + " ");
        }

        return result.toString().trim();
    }
}
