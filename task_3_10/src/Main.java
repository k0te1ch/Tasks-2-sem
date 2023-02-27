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

// Узел queue, используемый в BFS
class Node
{
    // (x, y) представляет координаты шахматной доски
    // `dist` представляет минимальное расстояние от источника
    int x, y, dist;
    Node previus;

    public Node(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Node(int x, int y, int dist)
    {
        this.x = x;
        this.y = y;
        this.dist = dist;
    }

    public Node(int x, int y, int dist, Node previus)
    {
        this.x = x;
        this.y = y;
        this.dist = dist;
        this.previus = previus;
    }

    // Поскольку мы используем объект класса в качестве ключа в `HashMap`,
    // нам нужно реализовать `hashCode()` и `equals()`

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x &&
                y == node.y &&
                dist == node.dist;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, dist);
    }
}

class Main
{
    // В массивах ниже представлены все восемь возможных движений коня
    private static int[] row = { 2, 2, -2, -2, 1, 1, -1, -1 };
    private static int[] col = { -1, 1, 1, -1, 2, -2, 2, -2 };

    // Проверяем, являются ли (x, y) действительными координатами шахматной доски.
    // Обратите внимание, что конь не может выйти за пределы доски
    private static boolean isValid(int x, int y, int N) {
        return (x >= 0 && x < N) && (y >= 0 && y < N);
    }

    // Находим минимальное количество шагов, которое сделал конь
    // из источника в пункт назначения с помощью BFS
    public static int findShortestDistance(Node src, Node dest, int N)
    {
        // ставим проверять, была ли ячейка матрицы посещена раньше или нет
        Set<Node> visited = new HashSet<>();

        // создаем queue и ставим в queue первый узел
        Queue<Node> q = new ArrayDeque<>();
        q.add(src);

        // цикл до тех пор, пока queue не станет пустой
        while (!q.isEmpty())
        {
            // удалить передний узел из очереди и обработать его
            Node node = q.poll();

            int x = node.x;
            int y = node.y;
            int dist = node.dist;

            // если пункт назначения достигнут, возвращаем расстояние
            if (x == dest.x && y == dest.y) {
                return dist;
            }

            // пропустить, если место было посещено раньше
            if (!visited.contains(node))
            {
                // отметить текущий узел как посещенный
                visited.add(node);

                // проверка всех восьми возможных движений коня
                // и ставим в queue каждое допустимое движение
                for (int i = 0; i < row.length; i++)
                {
                    // получаем действительную позицию коня из текущей позиции на
                    // шахматная доска и поставить ее в queue с расстоянием +1
                    int x1 = x + row[i];
                    int y1 = y + col[i];

                    if (isValid(x1, y1, N)) {
                        q.add(new Node(x1, y1, dist + 1, node));
                    }
                }
            }
        }

        // возвращаем бесконечность, если путь невозможен
        return Integer.MAX_VALUE;
    }

    public static void main(String[] args)
    {
        // Матрица N x N
        int N = 8;

        // исходные координаты
        Node src = new Node(0, 7);

        // координаты пункта назначения
        Node dest = new Node(7, 0);

        System.out.println("The minimum number of steps required is " +
                findShortestDistance(src, dest, N));
    }
}