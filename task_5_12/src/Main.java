import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Tree {
    private Node root;

    public Tree(String path) {
        root = new Node(path);
    }

    public Node getRoot() {
        return root;
    }

    public Node insert(Node node, String path) {
        if (new File(path).isFile()) return new Node(path);

        for (File i : Objects.requireNonNull(new File(path).listFiles())) {
            String pathNew = i.getPath();
            if (new File(pathNew).isDirectory()) node.files.add(insert(new Node(pathNew), pathNew));
            else node.files.add(insert(node, pathNew));
        }
        return node;
    }

    public void print(Node node, int level) {
        System.out.printf("%s%s\n", new String(new char[level]).replace("\0", "\t"), node.file.getPath());
        for (Node i : node.files) {
            print(i, level + 1);
        }
    }

    public List<File> find(Node node, String name) {
        List<File> tmp = new ArrayList<>();

        String[] tmp1 = name.split("\\.");
        String fileExt = tmp1.length > 1 ? tmp1[tmp1.length - 1] : "";
        String fileName = name.substring(0, name.length() - fileExt.length() - 1);
        String name2 = node.file.getName();
        String[] tmp2 = name2.split("\\.");
        String fileExt2 = tmp2.length > 1 ? tmp2[tmp2.length - 1] : "";
        String fileName2 = name2.substring(0, name2.length() - fileExt2.length() - 1);
        if (fileName2.contains(fileName) && fileExt2.contains(fileExt))
            tmp.add(node.file);


        for (Node i : node.files) tmp.addAll(find(i, name));

        return tmp;
    }

    static class Node {
        private final File file;
        private final List<Node> files = new ArrayList<>();

        public Node(String path) {
            file = new File(path);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        String path = "D:\\Programming\\Java\\Tasks-2-sem\\task_5_12\\src\\dir";
        Tree tree = new Tree(path);
        Tree.Node root = tree.getRoot();
        tree.insert(root, path);
        System.out.println(tree.find(root, "1.p"));
        tree.print(root, 0);
    }
}