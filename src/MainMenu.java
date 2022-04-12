import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainMenu {
    private JButton startButton;
    private JButton exitButton;
    private JTextField rowField;
    private JTextField columnField;
    private JPanel rootPanel;
    private JComboBox<String> choiceBox;
    private JButton showButton;
    private JPanel menuPanel;
    private JPanel drawingPanel;
    private String chosenConfiguration;
    private Universe universe;
    private int x;
    private int y;
    private Map<String, Runnable> actionsMap;

    public MainMenu() {
        actionsMap = new HashMap<>();
        setGridSize();
        setChoiceComboBox();
        setShowButton();
        setStartButton();
        setExitButton();
    }

    private void setGridSize() {
        rowField.setText("100");
        columnField.setText("100");
        x = y = 100;
        rowField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                var temp = rowField.getText();
                x = temp.isBlank() ? 0 : Integer.parseInt(temp);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                insertUpdate(e);
            }
        });
        columnField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                var temp = columnField.getText();
                y = temp.isBlank() ? 0 : Integer.parseInt(temp);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                insertUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                insertUpdate(e);
            }
        });
    }

    private void setChoiceComboBox() {
        var file = new File("src/resources");
        for (File item : Objects.requireNonNull(file.listFiles())) {
            choiceBox.addItem(item.getName());
            actionsMap.put(item.getName(), () -> universe.readFromFile(item.getAbsolutePath()));
        }
        choiceBox.addItem("случайное расселение");
        actionsMap.put("случайное расселение", () -> universe.initializeRandom());
    }

    private void setShowButton() {
        showButton.addActionListener(e -> {
            chosenConfiguration = (String) choiceBox.getSelectedItem();
            universe = new Universe(y, x);
            var action = actionsMap.get(chosenConfiguration);
            action.run();
            new Thread(() -> universe.show()).start();
        });
    }

    private void setStartButton() {
        startButton.addActionListener(e -> {
            switch (startButton.getText()) {
                case "Запустить" -> {
                    if (universe == null) {
                        chosenConfiguration = (String) choiceBox.getSelectedItem();
                        universe = new Universe(y, x);
                        var action = actionsMap.get(chosenConfiguration);
                        action.run();
                    }
                    new Thread(() -> universe.start()).start();
                    startButton.setText("Остановить");
                    startButton.setBackground(Color.PINK);
                }
                case "Остановить" -> {
                    universe.stop();
                    startButton.setText("Запустить");
                    startButton.setBackground(Color.GREEN);
                }
            }
        });
    }

    private void setExitButton() {
        exitButton.addActionListener(e -> System.exit(0));
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }
}
