import util.DrawUtils;
import util.JTableUtils;
import util.SwingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class MainForm extends JFrame {
    private JPanel panelMain;
    private JTable tableGameField;
    private JLabel labelStatus;
    private JLabel labelScore;
    private JLabel labelCountBalls;
    private JTable tableNextBalls;
    private JLabel labelRecord;
    private JLabel labelError;

    private static final String VERSION = "v1.0.0";

    private static final int DEFAULT_GAP = 8;
    private static final int DEFAULT_CELL_SIZE = 70;
    private int offset = 7;
    private boolean reverse = false;

    private static final Color[] COLORS = {
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.MAGENTA,
            Color.CYAN,
            Color.PINK,
    };

    private final Game game = new Game();

    public int time = 0;
    public final Timer timer = new Timer(1000, e -> {
        time++;
        this.labelStatus.setText("Прошло времени (секунд): "+time);
        this.labelCountBalls.setText("Количество шаров на поле: "+game.getCountBalls());
        this.labelScore.setText("Ваш счёт в очках: "+game.getScore());
        this.labelRecord.setText("Ваш рекорд в очках: "+game.getRecord());
    });

    private final Timer selectTimer = new Timer(20, e -> {
        if (reverse) offset--;
        else offset++;
        if (offset >= 7) reverse = true;
        else if (offset <= -7) reverse = false;
        updateView();
    });

    private int deleteTime = 1;
    private final Timer deleteTimer = new Timer(20, e -> {
        if (deleteTime != 0) {
            deleteTime++;
            if (reverse) offset -= 2;
            else offset += 2;
            if (offset >= 7) reverse = true;
            else if (offset <= -7) reverse = false;
            if (deleteTime >= 53) {
                deleteTime = 0;
                game.clearDeletedLines();
            }
            updateView();
        }
    });

    private int msgTime = 0;
    public static String msg = "";
    private final Timer msgTimer = new Timer(100, e -> {
        if (!msg.equals("")){
            msgTime++;
            if (msgTime == 1) this.labelError.setText(msg);
            if (msgTime == 30){
                msg = "";
                this.labelError.setText(msg);
                msgTime = 0;
            }
        }
    });

    private final ParamsDialog dialogParams;


    public MainForm(){
        this.setTitle("Линии 98 (Lines 98)");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        setJMenuBar(createMenuBar());
        this.pack();

        this.setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    if (game.getScore() > game.getRecord()){
                        game.setRecord(game.getScore());
                        game.writeRecord();
                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                System.exit(0);
            }
        });

        SwingUtils.setShowMessageDefaultErrorHandler();

        tableGameField.setRowHeight(DEFAULT_CELL_SIZE);
        JTableUtils.initJTableForArray(tableGameField, DEFAULT_CELL_SIZE, false, false, false, false);
        tableGameField.setIntercellSpacing(new Dimension(0, 0));
        tableGameField.setEnabled(false);

        tableGameField.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        tableNextBalls.setRowHeight(40);
        JTableUtils.initJTableForArray(tableNextBalls, 40, false, false, false, false);
        tableNextBalls.setIntercellSpacing(new Dimension(0, 0));
        tableNextBalls.setEnabled(false);

        tableNextBalls.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            final class DrawComponent extends Component {
                private int column = 0;

                @Override
                public void paint(Graphics gr) {
                    Graphics2D g2d = (Graphics2D) gr;
                    int width = getWidth() - 2;
                    int height = getHeight() - 2;
                    paintCellNextBall(column, g2d, width, height);
                }
            }

            final DrawComponent comp = new DrawComponent();

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {

                comp.column = column;
                return comp;
            }
        });

        newGame();

        updateWindowSize();
        updateView();

        GameParams params = new GameParams();
        dialogParams = new ParamsDialog(params, tableGameField);

        tableGameField.addMouseListener(new MouseAdapter() {
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
        });
    }

    private JMenuItem createMenuItem(String text, String shortcut, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(listener);
        if (shortcut != null) {
            menuItem.setAccelerator(KeyStroke.getKeyStroke(shortcut.replace('+', ' ')));
        }
        return menuItem;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBarMain = new JMenuBar();

        JMenu menuGame = new JMenu("Игра");
        menuBarMain.add(menuGame);
        menuGame.add(createMenuItem("Новая", "ctrl+N", e -> {
            try {
                restartGame();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }));
        menuGame.add(createMenuItem("Параметры", "ctrl+P", e -> {
            dialogParams.updateView();
            dialogParams.setVisible(true);
        }));
        menuGame.addSeparator();
        menuGame.add(createMenuItem("Выход", "ctrl+X", e -> System.exit(0)));

        JMenu menuView = new JMenu("Вид");
        menuBarMain.add(menuView);
        menuView.add(createMenuItem("Подогнать размер окна", null, e -> updateWindowSize()));
        menuView.addSeparator();
        SwingUtils.initLookAndFeelMenu(menuView);

        JMenu menuHelp = new JMenu("Справка");
        menuBarMain.add(menuHelp);
        menuHelp.add(createMenuItem("Правила", "ctrl+R", e -> SwingUtils.showInfoMessageBox("""
                Игра происходит на квадратном поле в 9×9 клеток и представляет собой серию ходов.
                На каждом ходу сначала компьютер в случайных клетках выставляет три шарика случайных
                цветов, последних всего семь. Далее делает ход игрок, он может передвинуть любой
                шарик в другую свободную клетку, но при этом между начальной и конечной клетками
                должен существовать недиагональный путь из свободных клеток. Если после перемещения
                получается так, что собирается пять или более шариков одного цвета в линию по горизонтали,
                вертикали или диагонали, то все такие шарики исчезают и игроку даётся возможность сделать
                ещё одно перемещение шарика. Если после перемещения линии не выстраивается, то ход
                заканчивается, и начинается новый с появлением новых шариков. Если при появлении новых
                шариков собирается линия, то она исчезает, игрок получает очки, но дополнительного
                перемещения не даётся. Игра продолжается до тех пор, пока всё поле не будет заполнено
                шариками и игрок не потеряет возможность сделать ход. Цель игры состоит в наборе максимального
                количества очков. Счёт устроен таким образом, что игрок при удалении за одно перемещение
                большего числа шариков, чем пять, получает существенно больше очков. Если игрок очищает поле,
                то получает 1000 очков. Во время игры на экране оказывается три цвета шариков, которые будут
                выброшены на поле на следующем ходу.""", "Правила")));
        menuHelp.add(createMenuItem("Управление", "ctrl+P", e -> SwingUtils.showInfoMessageBox("""
                ЛКМ - выделить шар.
                ПКМ - снять выделение с шара.
                """, "Правила")));
        menuHelp.add(createMenuItem("О программе", "ctrl+A", e -> SwingUtils.showInfoMessageBox(
                "Линии 98\n" +
                        VERSION +
                        "\n\nАвтор: Хвостов С. Г (k0te1ch)" +
                        "\nE-mail: khvostov40@gmail.com"+
                        "\nGitHub: github.com/k0te1ch",
                "О программе"
        )));

        return menuBarMain;
    }

    private void updateWindowSize() {
        int menuSize = this.getJMenuBar() != null ? this.getJMenuBar().getHeight() : 0;
        SwingUtils.setFixedSize(
                this,
                tableGameField.getWidth() + 2 * DEFAULT_GAP + 60,
                tableGameField.getHeight() + tableNextBalls.getHeight() + panelMain.getY() + labelStatus.getHeight() +
                        menuSize + 7 * DEFAULT_GAP + 60
        );
        this.setMaximumSize(null);
        this.setMinimumSize(null);
    }

    private void updateView() {
        tableNextBalls.repaint();
        tableGameField.repaint();
    }

    private void paintCellNextBall(int i, Graphics2D g2d, int cellWidth, int cellHeight){
        if (game.getNextBallSize() <= i) return;
        Ball cellValue = game.getNextBall(i);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (cellValue == null) return;

        Color color = COLORS[cellValue.getColorID()];

        int size = Math.min(cellWidth, cellHeight);
        int bound = (int) Math.round(size * 0.1);
        g2d.setColor(color);
        g2d.fillOval(bound, bound, size - 2 * bound, size - 2 * bound);
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawOval(bound, bound, size - 2 * bound, size - 2 * bound);
        g2d.setColor(DrawUtils.getContrastColor(color));
    }

    private void paintCell(int row, int column, Graphics2D g2d, int cellWidth, int cellHeight) {
        Ball cellValue;
        if (game.getDeletedLines().contains(Arrays.asList(row, column))) {
            cellValue = game.deletedBall;
            if (cellValue.getGain() == 0) cellValue.grow();
        }
        else cellValue = game.getCell(row, column);


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
    }

    private void newGame(){
        game.newGame(this);
        JTableUtils.resizeJTable(tableNextBalls,
                1, 3,
                tableNextBalls.getRowHeight(), tableNextBalls.getRowHeight()
        );
        JTableUtils.resizeJTable(tableGameField,
                game.getHeight(), game.getHeight(),
                tableGameField.getRowHeight(), tableGameField.getRowHeight()
        );
        time = 0;
        this.labelStatus.setText("Прошло времени (секунд): "+time);
        this.labelCountBalls.setText("Количество шаров на поле: "+game.getCountBalls());
        this.labelScore.setText("Ваш счёт в очках: "+game.getScore());
        this.labelRecord.setText("Ваш рекорд в очках: "+game.getRecord());
        this.labelError.setText("Удачной игры!");
        msg = "Удачной игры!";
        updateView();
    }

    private void restartGame() throws FileNotFoundException {
        game.restartGame();
        time = 0;
        this.labelStatus.setText("Прошло времени (секунд): "+time);
        this.labelCountBalls.setText("Количество шаров на поле: "+game.getCountBalls());
        this.labelScore.setText("Ваш счёт в очках: "+game.getScore());
        this.labelRecord.setText("Ваш рекорд в очках: "+game.getRecord());
        this.labelError.setText("Удачной игры!");
        msg = "Удачной игры!";
        updateView();
    }
}
