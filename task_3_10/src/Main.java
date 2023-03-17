/*
3-10
Найти минимальное кол-во шагов, за которое шахматная фигура конь может из одной
заданной клетки шахматного поля попасть в другую клетку шахматного поля, а также
последовательность таких шагов (если их несколько, то любую).
Подсказка: Использовать очередь, в которую поместить начальное положение коня.
Далее, пока не попадем в требуемую клетку, извлекаем из очереди клетку, для которой
перебираем все возможные ходы конем (клетки), которые помещаем в очередь. Для
хранения последовательности ходов используем стек или просто массив/список.
*/

import java.util.*;

class Main
{
    private static final int[] row = { 2, 2, -2, -2, 1, 1, -1, -1 };
    private static final int[] col = { -1, 1, 1, -1, 2, -2, 2, -2 };
    private static boolean isValid(int x, int y, int N) {
        return (x >= 0 && x < N) && (y >= 0 && y < N);
    }

    private static void route(Node node){
        Node prevNode = node;
        node = node.prev;
        while (node != null) {
            System.out.printf("(%s, %s) -> (%s, %s)\n", prevNode.x, prevNode.y, node.x, node.y);
            prevNode = node;
            node = node.prev;
        }
    }
    public static int findShortestDistance(Node src, Node dest, int N)
    {
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

    public static int findShortestDistanceCustom(Node src, Node dest, int N)
    {
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

    public static void main(String[] args)
    {
        Node src = new Node(0, 7);
        Node to = new Node(7, 0);
        System.out.println("Минимум шагов: " + findShortestDistance(src, to, 8));
        System.out.println();
        System.out.println("Минимум шагов 2: " + findShortestDistanceCustom(src, to, 8));

        System.out.println();
        src = new Node(7, 7);
        to = new Node(0, 7);
        System.out.println("Минимум шагов: " + findShortestDistanceCustom(src, to, 8));
    }
}