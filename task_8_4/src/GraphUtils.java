import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Утилиты работы с графами
 */
public class GraphUtils {

    /**
     * Преобразовываем String в граф
     *
     * @return timeGraph
     */
    public static TimeGraph fromStr(String str) {
        TimeGraph timeGraph = null;
        if (Pattern.compile("^\\s*\\d+").matcher(str).find()) {
            Scanner scanner = new Scanner(str);
            int endTime = scanner.nextInt();
            int N = scanner.nextInt();

            List<Integer> T1 = new ArrayList<>(), T2 = new ArrayList<>();
            for (int i = 0; i < N; i++) {
                T1.add(scanner.nextInt());
                T2.add(scanner.nextInt());
            }
            int length = scanner.nextInt();
            timeGraph = new TimeGraph(N, endTime, length);
            timeGraph.addGuards(T1, T2);
        }
        return timeGraph;
    }
}
