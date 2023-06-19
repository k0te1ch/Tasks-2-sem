import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Утилиты работы с графами
 */
public class GraphUtils {

    /**
     * Преобразовываем String в граф
     *
     * @return FriendshipGraph
     */
    public static FriendshipGraph fromStr(String str) {
        FriendshipGraph FriendshipGraph = new FriendshipGraph();
        if (Pattern.compile("^\\s*\\d+").matcher(str).find()) {
            Scanner scanner = new Scanner(str);
            FriendshipGraph = new FriendshipGraph();
            int edgeCount = scanner.nextInt();
            for (int i = 0; i < edgeCount; i++) {
                FriendshipGraph.addFriend(scanner.next(), scanner.next(), scanner.nextDouble());
            }
        }
        return FriendshipGraph;
    }


    /**
     * Получение dot-описяния графа (для GraphViz)
     *
     * @return String
     */
    public static String toDot(FriendshipGraph friendshipGraph) {
        StringBuilder sb = new StringBuilder();
        String nl = System.getProperty("line.separator");
        sb.append("strict graph").append(" {").append(nl);
        Map<String, List<List<String>>> graphMap = friendshipGraph.getGraph();
        for (String v1 : graphMap.keySet()) {
            int count = 0;
            for (List<String> item : friendshipGraph.getGraph().get(v1)) {
                sb.append(String.format("  %s %s %s [label=\"%s\"]", v1, "--", item.get(0), item.get(1))).append(nl);
                count++;
            }
            if (count == 0) {
                sb.append(v1).append(nl);
            }
        }
        sb.append("}").append(nl);

        return sb.toString();
    }
}
