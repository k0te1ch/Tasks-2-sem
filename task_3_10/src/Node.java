import java.util.Objects;

class Node
{
    int x, y, dist;
    Node prev;

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

    public Node(int x, int y, int dist, Node prev)
    {
        this.x = x;
        this.y = y;
        this.dist = dist;
        this.prev = prev;
    }

    // Поскольку мы используем объект класса в качестве ключа в "HashMap",
    // нам нужно реализовать "hashCode()" и "equals()"

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node nodeLib = (Node) o;
        return x == nodeLib.x &&
                y == nodeLib.y &&
                dist == nodeLib.dist;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, dist);
    }
}