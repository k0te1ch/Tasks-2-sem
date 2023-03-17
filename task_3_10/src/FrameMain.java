import util.ArrayUtils;
import util.DrawUtils;
import util.JTableUtils;
import util.SwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

class Colors {
    Colo
}

public class FrameMain extends JFrame {
    private JTable tableInputArray;
    private JButton buttonLoadFromFile;
    private JButton buttonCreateRandomArray;
    private JButton buttonOutputArray;
    private JPanel panelMain;
    private JScrollPane scrollPaneTableInput;
    private JLabel labelStatus;
    private JButton buttonOutputArray2;

    private final JFileChooser fileChooserOpen;
    private JMenuBar menuBarMain;
    private JMenu menuLookAndFeel;

    public FrameMain() {
        this.setTitle("Task 3 (10)");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        JTableUtils.initJTableForArray(tableInputArray, 30, false, false, false, false);
        tableInputArray.setRowHeight(30);

        fileChooserOpen = new JFileChooser();
        fileChooserOpen.setCurrentDirectory(new File("."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Текстовые файлы", "txt");
        fileChooserOpen.addChoosableFileFilter(filter);

        tableInputArray.setIntercellSpacing(new Dimension(0, 0));
        tableInputArray.setEnabled(false);

        tableInputArray.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            final class DrawComponent extends Component {
                private int row = 0, column = 0;

                @Override
                public void paint(Graphics gr) {
                    Graphics2D g2d = (Graphics2D) gr;
                    int width = getWidth() - 2;
                    int height = getHeight() - 2;
                    paintCell(row, column, g2d, width, height);
                }
            }

            final DrawComponent comp = new DrawComponent();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                comp.row = row;
                comp.column = column;
                return comp;
            }
        });

        JTableUtils.writeArrayToJTable(tableInputArray, new int[]{0, 1, 2, 3});

        /*tableGameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = tableGameField.rowAtPoint(e.getPoint());
                int col = tableGameField.columnAtPoint(e.getPoint());
                if (SwingUtilities.isLeftMouseButton(e)) {
                    try {
                        game.leftMouseClick(row, col);
                    } catch (FileNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (time == 0) {
                        timer.start();
                        msgTimer.start();
                    }
                    updateView();
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    game.rightMouseClick(row, col);
                    updateView();
                }
            }
        });*/

        private void paintCell(int row, int column, Graphics2D g2d, int cellWidth, int cellHeight) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (cellValue == null) return;
            Color color = COLORS[cellValue.getColorID()];

            int size = Math.min(cellWidth, cellHeight);
            int bound;
            if (cellValue.getGain() == 1) bound = (int) Math.round(size * 0.1);
            else bound = (int) Math.round(size * 0.4);

            if (game.hasSelect() && !selectTimer.isRunning()) selectTimer.start();
            else if (!game.hasSelect() && selectTimer.isRunning()) selectTimer.stop();

            if (game.hasDeleted() && !deleteTimer.isRunning()) {
                deleteTime = 1;
                deleteTimer.start();
            }
            else if (deleteTimer.isRunning() && deleteTime == 0) deleteTimer.stop();

            if (game.hasSelect() && game.getSelect() == cellValue && !game.getDeletedLines().contains(Arrays.asList(row, column))) {
                g2d.setColor(Color.GRAY);
                g2d.fillRect(0, 0, cellWidth+1, cellHeight+1);
                g2d.setColor(color);
                g2d.fillOval(bound, bound-offset, size - 2 * bound, size - 2 * bound);
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawOval(bound, bound-offset, size - 2 * bound, size - 2 * bound);
                g2d.setColor(DrawUtils.getContrastColor(color));
            } else if (game.getDeletedLines().contains(Arrays.asList(row, column))) {
                g2d.setColor(color);
                g2d.fillOval(bound, bound-offset, size - 2 * bound, size - 2 * bound);
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawOval(bound, bound-offset, size - 2 * bound, size - 2 * bound);
                g2d.setColor(DrawUtils.getContrastColor(color));
            } else {
                g2d.setColor(color);
                g2d.fillOval(bound, bound, size - 2 * bound, size - 2 * bound);
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawOval(bound, bound, size - 2 * bound, size - 2 * bound);
                g2d.setColor(DrawUtils.getContrastColor(color));
            }
        }*/

        buttonLoadFromFile.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    if (fileChooserOpen.showOpenDialog(panelMain) == JFileChooser.APPROVE_OPTION) {
                        int[][] arr = ArrayUtils.readIntArray2FromFile(fileChooserOpen.getSelectedFile().getPath());
                        JTableUtils.writeArrayToJTable(tableInputArray, arr);
                    }
                } catch (Exception e) {
                    SwingUtils.showErrorMessageBox(e);
                }
            }
        });

        buttonCreateRandomArray.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    int[][] arr = ArrayUtils.createRandomIntMatrix(tableInputArray.getRowCount(), tableInputArray.getColumnCount(), -100, 100);
                    JTableUtils.writeArrayToJTable(tableInputArray, arr);
                } catch (Exception e) {
                    SwingUtils.showErrorMessageBox(e);
                }
            }
        });

        buttonOutputArray.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    List<Integer> list = ConsoleMain.solution(Arrays.asList(ArrayUtils.toObject(JTableUtils.readIntArrayFromJTable(tableInputArray))));
                } catch (Exception e) {
                    SwingUtils.showErrorMessageBox(e);
                }
            }
        });
    }
}

