import java.util.List;

public class ListNode {
    private ListItem head;
    private ListItem tail;

    public ListNode(){}

    public void append(Node node){
        ListItem item = new ListItem(node);
        if (head == null) {
            head = item;
        } else {
            tail.setNext(item);
        }
        tail = item;
    }

    public Node remove(){
        Node item = head.getData();
        head = head.getNext();
        if (head == null) tail = null;
        return item;
    }

    public boolean isEmpty(){
        return head == null;
    }
}
