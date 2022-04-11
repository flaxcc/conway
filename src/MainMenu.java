import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MainMenu {
    private JButton startButton;
    private JButton exitButton;
    private JTextField rowField;
    private JTextField columnField;
    private JPanel rootPanel;
    private JComboBox<String> comboBox;
    private JButton choiseButton;
    private JPanel menuPanel;
    private JPanel drawingPanel;
    private String chosenConfiguration;
    private Universe universe;

    public MainMenu() {
        rowField.setText("100");
        columnField.setText("100");
        var file = new File("src/resources");
        for (String item : Objects.requireNonNull(file.list())) {
            comboBox.addItem(item);
        }
        comboBox.addItem("случайное расселение");
        startButton.addActionListener(e -> {
            if (universe == null) {
                var s1 = rowField.getText();
                int x = Integer.parseInt(s1);
                var s2 = columnField.getText();
                int y = Integer.parseInt(s2);
                universe = new Universe(y, x);
                chosenConfiguration = (String) comboBox.getSelectedItem();
                if (chosenConfiguration.equals("случайное расселение"))
                {
                    universe.initializeRandom();
                }
                else {
                    try {
                        String filename = "src/resources/" + chosenConfiguration;
                        universe.readFromFile(filename);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            new Thread(() -> universe.start()).start();
        });
        exitButton.addActionListener(e -> System.exit(0));
        comboBox.addActionListener(e -> chosenConfiguration = (String) comboBox.getSelectedItem());
        choiseButton.addActionListener(e -> {
            if (chosenConfiguration == null) {
                chosenConfiguration = (String) comboBox.getSelectedItem();
            }
            var s1 = rowField.getText();
            int x = Integer.parseInt(s1);
            var s2 = columnField.getText();
            int y = Integer.parseInt(s2);
            universe = new Universe(y, x);
            if (chosenConfiguration.equals("случайное расселение"))
            {
                universe.initializeRandom();
            }
            else {
                try {
                    String filename = "src/resources/" + chosenConfiguration;
                    universe.readFromFile(filename);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            new Thread(() -> universe.show()).start();

        });

    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
