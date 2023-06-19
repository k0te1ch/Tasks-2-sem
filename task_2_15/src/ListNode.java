import java.util.List;

public class ListNode {
    private ListItem head;
    private ListItem tail = null;
    private int len = 0;

    public ListNode(List<List<String>> info) {
        for (List<String> i : info) {
            append(new ListItem(i, len));
        }
    }

    public void append(ListItem item) {
        if (head == null) {
            head = item;
        } else {
            tail.setNext(item);
        }
        item.setPrev(tail);
        tail = item;
        len++;
    }

    /*public void set(int index, ListItem item){
        ListItem cursor1, cursor2;
        if (len == 0 || index == len) append(item);
        else if (index == 0){
            item.setPrev(null);
            item.setNext(head);
            head.setPrev(item);
            head = item;
            indexes();
        }
        else if (index+1 < len && index > 0) {
            int index1 = index-1;
            int index2 = index+1;
            if (item.getIndex() == index1) index1--;
            if (item.getIndex() == index2) index2++;
            cursor1 = cursor(index1);
            cursor2 = cursor(index2, cursor1);
            cursor1.setNext(item); // IDE зря ругается то...
            item.setPrev(cursor1);
            item.setNext(cursor2);
            cursor2.setPrev(item); // IDE зря ругается то...
            len++;
            indexes();
        }
    }*/

    private void indexes() {
        ListItem item = head;
        int i = 0;
        while (item != null) {
            item.setIndex(i);
            i++;
            item = item.getNext();
        }
    }


    private void swap(ListItem item1, ListItem item2) {
        int index1 = item1.getIndex();
        int index2 = item2.getIndex();
        if (index1 - index2 == 0) return;
        if (index1 - index2 == 1) {
            item2.setNext(item1.getNext());
            if (item2.getNext() != null) {
                item2.getNext().setPrev(item2);
            }
            item1.setPrev(item2.getPrev());
            item1.setNext(item2);
            item2.setPrev(item1);
            if (item1.getPrev() != null) {
                item1.getPrev().setNext(item1);
            }
        } else if (index2 - index1 == 1) {
            item1.setNext(item2.getNext());
            if (item1.getNext() != null) {
                item1.getNext().setPrev(item1);
            }
            item2.setPrev(item1.getPrev());
            item2.setNext(item1);
            item1.setPrev(item2);
            if (item2.getPrev() != null) {
                item2.getPrev().setNext(item2);
            }
        } else {
            ListItem temp = item2.getNext();
            item2.setNext(item1.getNext());
            item1.setNext(temp);
            temp = item1.getPrev();
            item1.setPrev(item2.getPrev());
            item2.setPrev(temp);
            if (item2.getPrev() != null) {
                item2.getPrev().setNext(item2);
            }
            if (item1.getPrev() != null) {
                item1.getPrev().setNext(item1);
            }
            if (item1.getNext() != null) {
                item1.getNext().setPrev(item1);
            }
            if (item2.getNext() != null) {
                item2.getNext().setPrev(item2);
            }
        }
        if (index1 == 0) {
            head = item2;
        }
        if (index2 == 0) {
            head = item1;
        }
        if (index1 == len - 1) {
            tail = item2;
        }
        if (index2 == len - 1) {
            tail = item1;
        }
        indexes();
    }

    private ListItem cursor(int index) {
        if (index >= 0 && index < len) {
            ListItem item;
            if (len / 2 <= index) {
                item = tail;
                for (int i = len - 1; i > index; i--) {
                    item = item.getPrev();
                }
            } else {
                item = head;
                for (int i = 0; i < index; i++) {
                    item = item.getNext();
                }
            }
            return item;
        }
        return null;
    }

    /*private ListItem cursor(int index, ListItem item){
        if (index >= 0 && index < len){
            int itemIndex = item.getIndex();
            for (int i = 0; i < Math.abs(index-itemIndex); i++){
                if (itemIndex > index){
                    item = item.getPrev();
                } else item = item.getNext();
            }
            return item;
        }
        return null;
    }*/

    public void sort() {
        ListItem item1, item2, item3;
        for (int i = 0; i < len - 1; i++) {
            item3 = cursor(i);
            if (item3 == null || !item3.hasNext()) break;
            item1 = item3;
            item2 = item3.getNext();
            for (int j = i + 1; j < len && item2 != null; ++j) {
                if (item2.getCourse() < item1.getCourse()) {
                    item1 = item2;
                }
                if (j + 1 != len) item2 = item2.getNext();
            }

            swap(item3, item1);
        }
    }

    public void print() {
        System.out.println("{");
        ListItem item = head;
        for (int i = 0; i < len; i++) {
            String s = String.format(" %s курс <-> %s,", item.getCourse(), item.getFio());
            if (i + 1 == len) s = s.substring(0, s.length() - 1);
            System.out.println(s);
            item = item.getNext();
        }
        System.out.print("}\n");
    }

}
