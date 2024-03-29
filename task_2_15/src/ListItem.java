import java.util.List;

public class ListItem {
    private final String fio;
    private final int course;
    private ListItem next;
    private ListItem prev;
    private int index;

    public ListItem(List<String> info, int index) {
        course = Integer.parseInt(info.get(0));
        fio = info.get(1);
        this.index = index;
    }

    public ListItem getPrev() {
        return prev;
    }

    public void setPrev(ListItem prev) {
        this.prev = prev;
    }

    public ListItem getNext() {
        return next;
    }

    public void setNext(ListItem next) {
        this.next = next;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getCourse() {
        return course;
    }

    public boolean hasNext() {
        return this.next != null;
    }

    public String getFio() {
        return fio;
    }
}
