import java.util.*;

class Main
{
    private static final int[] row = { 2, 2, -2, -2, 1, 1, -1, -1 };
    private static final int[] col = { -1, 1, 1, -1, 2, -2, 2, -2 };
    private static boolean isValid(int x, int y, int N) {
        return (x >= 0 && x < N) && (y >= 0 && y < N);
    }

    private static void route(Node node){
        node = node.prev;
        while (node != null) {
            FrameMain.addRoute(node.x, node.y);
            node = node.prev;
        }
    }

    public static int findShortestDistance(Node src, Node dest, int N){
        Set<Node> visited = new HashSet<>();

        Queue<Node> q = new ArrayDeque<>();
        q.add(src);

        while (!q.isEmpty())
        {
            Node nodeLib = q.poll();

            int x = nodeLib.x;
            int y = nodeLib.y;
            int dist = nodeLib.dist;

            if (x == dest.x && y == dest.y) {
                route(nodeLib);
                return dist;
            }

            if (!visited.contains(nodeLib))
            {
                visited.add(nodeLib);
                for (int i = 0; i < row.length; i++)
                {
                    int x1 = x + row[i];
                    int y1 = y + col[i];

                    if (isValid(x1, y1, N)) {
                        q.add(new Node(x1, y1, dist + 1, nodeLib));
                    }
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    public static int findShortestDistanceCustom(Node src, Node dest, int N) {
        Set<Node> visited = new HashSet<>();

        ListNode q = new ListNode();
        q.append(src);

        while (!q.isEmpty())
        {
            Node nodeLib = q.remove();

            int x = nodeLib.x;
            int y = nodeLib.y;
            int dist = nodeLib.dist;

            if (x == dest.x && y == dest.y) {
                route(nodeLib);
                return dist;
            }

            if (!visited.contains(nodeLib))
            {
                visited.add(nodeLib);
                for (int i = 0; i < row.length; i++)
                {
                    int x1 = x + row[i];
                    int y1 = y + col[i];

                    if (isValid(x1, y1, N)) {
                        q.append(new Node(x1, y1, dist + 1, nodeLib));
                    }
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new FrameMain().setVisible(true));
    }
}