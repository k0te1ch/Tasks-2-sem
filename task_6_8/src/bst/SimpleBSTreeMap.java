package bst;

/**
 * Реализация словаря на базе простого (наивного) дерева поиска
 *
 * @param <K>
 * @param <V>
 */
public class SimpleBSTreeMap<K extends Comparable<K>, V> implements BSTreeMap<K, V> {

    private final BSTree<MapTreeEntry<K, V>> tree = new SimpleBSTree<>();

    @Override
    public BSTree<MapTreeEntry<K, V>> getTree() {
        return tree;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("{");
        for (MapTreeEntry<K, V> i : BinaryTreeAlgorithms.preOrderValues(tree.getRoot())) {
            res.append(i.toString());
            res.append(", ");
        }
        res.delete(res.length() - 2, res.length());
        return res.toString() + "}";
    }
}
