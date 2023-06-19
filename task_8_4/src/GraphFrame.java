import util.JTableUtils;
import util.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class GraphFrame extends JFrame {
    private final JFileChooser fileChooserTxtOpen;
    private final JFileChooser fileChooserTxtSave;
    private JPanel panelMain;
    private JTextArea textAreaSystemOut;
    private JButton buttonLoadGraphFromFile;
    private JTextArea textAreaGraphFile;
    private JButton buttonCreateGraph;
    private JSplitPane splitPaneGraphTab1;
    private JSplitPane splitPaneGraphTab2;
    private JButton buttonSaveGraphToFile;
    private JButton buttonCheckV;
    private JButton buttonAdd;
    private JButton buttonCheckG;
    private JButton buttonChechD;
    private JTextField textField1;
    private JTextField textField2;
    private JButton buttonProcess;
    private JTable table;
    private JButton buttonDelete;
    private TimeGraph timeGraph = null;

    public GraphFrame() {
        this.setTitle("Графы (8-4)");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        JTableUtils.initJTableForArray(table, 60, true, true, false, false);
        table.setRowHeight(45);

        splitPaneGraphTab1.setBorder(null);
        splitPaneGraphTab2.setBorder(null);


        fileChooserTxtOpen = new JFileChooser();
        fileChooserTxtSave = new JFileChooser();
        fileChooserTxtOpen.setCurrentDirectory(new File("./task_8_4/src/tests"));
        fileChooserTxtSave.setCurrentDirectory(new File("./task_8_4/src/tests"));
        FileFilter txtFilter = new FileNameExtensionFilter("Text files (*.txt)", "txt");

        fileChooserTxtOpen.addChoosableFileFilter(txtFilter);
        fileChooserTxtSave.addChoosableFileFilter(txtFilter);

        fileChooserTxtSave.setAcceptAllFileFilterUsed(false);
        fileChooserTxtSave.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooserTxtSave.setApproveButtonText("Save");


        buttonLoadGraphFromFile.addActionListener(e -> {
            if (fileChooserTxtOpen.showOpenDialog(GraphFrame.this) == JFileChooser.APPROVE_OPTION) {
                try (Scanner sc = new Scanner(fileChooserTxtOpen.getSelectedFile())) {
                    sc.useDelimiter("\\Z");
                    textAreaGraphFile.setText(sc.next());
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonSaveGraphToFile.addActionListener(e -> {
            if (fileChooserTxtSave.showSaveDialog(GraphFrame.this) == JFileChooser.APPROVE_OPTION) {
                String filename = fileChooserTxtSave.getSelectedFile().getPath();
                if (!filename.toLowerCase().endsWith(".txt")) {
                    filename += ".txt";
                }
                try (FileWriter wr = new FileWriter(filename)) {
                    wr.write(textAreaGraphFile.getText());
                } catch (Exception exc) {
                    SwingUtils.showErrorMessageBox(exc);
                }
            }
        });
        buttonCreateGraph.addActionListener(e -> {
            try {
                GraphFrame.this.timeGraph = GraphUtils.fromStr(textAreaGraphFile.getText());
                repaintTable();
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonAdd.addActionListener(e -> {
            try {
                if (timeGraph == null) {
                    return;
                }
                showSystemOut(() -> {
                    int from = Integer.parseInt(textField1.getText());
                    int to = Integer.parseInt(textField2.getText());
                    timeGraph.addGuard(from, to);
                    repaintTable();
                    textAreaGraphFile.setText(timeGraph.fromGraph());
                });
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonDelete.addActionListener(e -> {
            try {
                if (timeGraph == null) {
                    return;
                }
                showSystemOut(() -> {
                    int from = Integer.parseInt(textField1.getText());
                    int to = Integer.parseInt(textField2.getText());
                    timeGraph.removeGuard(from, to);
                    repaintTable();
                    textAreaGraphFile.setText(timeGraph.fromGraph());
                });
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonChechD.addActionListener(e -> {
            try {
                if (timeGraph == null) {
                    return;
                }
                showSystemOut(() -> {
                    List<List<Integer>> resD = timeGraph.checkG();
                    if (resD.size() == 0) System.out.println("(Д): -");
                    else System.out.printf("(Д): %s охраника (-ов), их сдвиги: %s", resD.size(), resD);
                });
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonCheckG.addActionListener(e -> {
            try {
                if (timeGraph == null) {
                    return;
                }
                showSystemOut(() -> {
                    List<List<Integer>> resG = timeGraph.checkG();
                    if (resG.size() == 0) System.out.println("(Г): Нет");
                    else System.out.printf("(Г): Да, вот сдвиги: %s", resG);
                });
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonCheckV.addActionListener(e -> {
            try {
                if (timeGraph == null) {
                    return;
                }
                showSystemOut(() -> {
                    List<List<Integer>> resV = timeGraph.checkV();
                    System.out.printf("(В): Добавляем %s охраника (-ов): %s", resV.size(), resV);
                });
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
        buttonProcess.addActionListener(e -> {
            try {
                if (timeGraph == null) {
                    return;
                }
                showSystemOut(() -> {

                    // А
                    boolean resA = timeGraph.checkA();
                    if (resA) System.out.println("(А): Всё в безопасности (true)");
                    else {
                        System.out.println("(А): Галерея в опасности! (false)");

                        // Б
                        List<List<Integer>> resB = timeGraph.checkB();
                        System.out.printf("(Б): Бреши в безопасности (меньше 2 охраников): %s\n", resB);

                        // В
                        List<List<Integer>> resV = timeGraph.checkV();
                        System.out.printf("(В): Добавляем %s охраника (-ов): %s\n", resV.size(), resV);

                        // Г
                        List<List<Integer>> resG = timeGraph.checkG();
                        if (resG.size() == 0) System.out.println("(Г): Нет");
                        else System.out.printf("(Г): Да, вот сдвиги: %s\n", resG);

                        // Д
                        List<List<Integer>> resD = timeGraph.checkG();
                        if (resD.size() == 0) System.out.println("(Д): -");
                        else System.out.printf("(Д): %s охраника (-ов), их сдвиги: %s\n", resD.size(), resD);
                    }
                });
            } catch (Exception exc) {
                SwingUtils.showErrorMessageBox(exc);
            }
        });
    }

    /**
     * Выполнение действия с выводом стандартного вывода в окне (textAreaSystemOut)
     *
     * @param action Выполняемое действие
     */
    private void showSystemOut(Runnable action) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os, true, StandardCharsets.UTF_8));

        action.run();

        textAreaSystemOut.setText(os.toString(StandardCharsets.UTF_8));
        System.setOut(oldOut);
    }

    private void repaintTable() {
        String[][] toTable = timeGraph.toTable();
        JTableUtils.writeArrayToJTable(table, toTable);
    }

    private record ComboItem(String key, String value) {

        @Override
        public String toString() {
            return key;
        }
    }
}