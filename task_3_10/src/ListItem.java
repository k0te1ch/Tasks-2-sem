public class ListItem {
    private final Node data;
    private ListItem next;

    public ListItem(Node data){
        this.data = data;
    }

    public Node getData(){
        return data;
    }

    public ListItem getNext() {
        return next;
    }

    public void setNext(ListItem next) {
        this.next = next;
    }
}
