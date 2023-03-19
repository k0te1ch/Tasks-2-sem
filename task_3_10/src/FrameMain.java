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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FrameMain extends JFrame {
    private JTable tableInputArray;
    private JButton buttonLoadFromFile;
    private JButton buttonCreateRandomArray;
    private JButton buttonOutputArray;
    private JPanel panelMain;
    private JLabel labelStatus;
    private JButton buttonOutputArray2;
    private static List<Integer> start = new ArrayList<>();
    private static List<Integer> finish = new ArrayList<>();
    private static List<List<Integer>> routes = new ArrayList<>();

    private final JFileChooser fileChooserOpen;
    private static final Color[] COLORS = {
            Color.BLACK,
            Color.WHITE,
            Color.RED,
            Color.BLUE,
            Color.GREEN,
    };

    public static void addRoute(int x, int y){
        if ((start.size() == 2 && x == start.get(0) && y == start.get(1)) || (finish.size() == 2 && x == finish.get(0) && y == finish.get(1))) return;
        routes.add(Arrays.asList(x, y));
    }

    public void paintCell(int row, int column, Graphics2D g2d, int cellWidth, int cellHeight) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int colorID = 1;
        if (row % 2 == 0 && column % 2 == 1 || row % 2 == 1 && column % 2 == 0) colorID = 0;
        Color color = COLORS[colorID];
        int size = Math.min(cellWidth, cellHeight);
        g2d.setColor(color);
        g2d.fillRect(0, 0, size, size);

        if (start.size() != 0 || finish.size() != 0){
            if (start.size() != 0 && start.get(1) == row && start.get(0) == column) colorID = 3;
            if (finish.size() != 0 && finish.get(1) == row && finish.get(0) == column) colorID = 4;
            if (colorID == 3 || colorID == 4) {
                color = COLORS[colorID];
                size = Math.min(cellWidth, cellHeight);
                int bound = (int) Math.round(size * 0.3);
                g2d.setColor(color);
                g2d.fillOval(bound, bound, size - 2 * bound, size - 2 * bound);
                g2d.setColor(Color.DARK_GRAY);
                g2d.drawOval(bound, bound, size - 2 * bound, size - 2 * bound);
                g2d.setColor(DrawUtils.getContrastColor(color));
            }
        }

        if (routes.size() != 0) {
            int cnt = -1;
            for (int i = 0; i < routes.size(); i++){
                List<Integer> tmp = routes.get(i);
                if (tmp.get(1) == row && tmp.get(0) == column) {
                    cnt = i;
                    break;
                }
            }
            if (cnt == -1) return;
            color = COLORS[2];
            size = Math.min(cellWidth, cellHeight);
            int bound = (int) Math.round(size * 0.3);
            g2d.setColor(color);
            g2d.fillOval(bound, bound, size - 2 * bound, size - 2 * bound);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawOval(bound, bound, size - 2 * bound, size - 2 * bound);
            g2d.setColor(DrawUtils.getContrastColor(color));
        }
    }


    public FrameMain() {
        this.setTitle("Task 3 (10)");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        JTableUtils.initJTableForArray(tableInputArray, 40, false, false, false, false);
        tableInputArray.setRowHeight(40);

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

        JTableUtils.writeArrayToJTable(tableInputArray, new int[8][8]);
        SwingUtils.setFixedSize(
                this,
                tableInputArray.getWidth() + 120,
                tableInputArray.getHeight() + 200
        );
        this.setMaximumSize(null);
        this.setMinimumSize(null);
        this.setLocationRelativeTo(null);

        tableInputArray.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = tableInputArray.rowAtPoint(e.getPoint());
                int col = tableInputArray.columnAtPoint(e.getPoint());
                if (SwingUtilities.isLeftMouseButton(e)) {
                    start = Arrays.asList(col, row);
                    routes = new ArrayList<>();
                    tableInputArray.repaint();
                    String text;
                    if (finish.size() == 0) text = "Добавьте конечную точку маршрута";
                    else text = "Вы можете решить задачу!";
                    labelStatus.setText(text);
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    finish = Arrays.asList(col, row);
                    tableInputArray.repaint();
                    routes = new ArrayList<>();
                    String text;
                    if (start.size() == 0) text = "Добавьте начальную точку маршрута";
                    else text = "Вы можете решить задачу!";
                    labelStatus.setText(text);
                }
            }
        });

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
                    int[] tmp = ArrayUtils.createRandomIntArray(2, 0, 7);
                    start.clear();
                    for (int i: tmp) start.add(i);
                    tmp = ArrayUtils.createRandomIntArray(2, 0, 7);
                    finish.clear();
                    for (int i: tmp) finish.add(i);
                    tableInputArray.repaint();
                } catch (Exception e) {
                    SwingUtils.showErrorMessageBox(e);
                }
            }
        });

        buttonOutputArray.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    String text = "";
                    if (start.size() == 2 && finish.size() == 2){
                        text = String.format("Минимум шагов: %s (способ 1)", Main.findShortestDistance(new Node(start.get(0), start.get(1)), new Node(finish.get(0), finish.get(1)), 8));
                        tableInputArray.repaint();
                    } else if (start.size() == 0 && finish.size() == 0) text = "Укажите стартовую и конечную точки маршрута";
                    else if (start.size() == 0 && finish.size() == 2) text = "Укажите стартовую точку маршрута";
                    else if (start.size() == 2 && finish.size() == 0) text = "Укажите конечную точку маршрута";
                    labelStatus.setText(text);
                } catch (Exception e) {
                    SwingUtils.showErrorMessageBox(e);
                }
            }
        });

        buttonOutputArray2.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    String text = "";
                    if (start.size() == 2 && finish.size() == 2){
                        text = String.format("Минимум шагов: %s (способ 2)", Main.findShortestDistanceCustom(new Node(start.get(0), start.get(1)), new Node(finish.get(0), finish.get(1)), 8));
                        tableInputArray.repaint();
                    } else if (start.size() == 0 && finish.size() == 0) text = "Укажите стартовую и конечную точки маршрута";
                    else if (start.size() == 0 && finish.size() == 2) text = "Укажите стартовую точку маршрута";
                    else if (start.size() == 2 && finish.size() == 0) text = "Укажите конечную точку маршрута";
                    labelStatus.setText(text);
                } catch (Exception e) {
                    SwingUtils.showErrorMessageBox(e);
                }
            }
        });
    }
}

