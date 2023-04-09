import java.util.*;

public class Main {
    public static List<Integer> sort(List<Integer> s){
        List<List<Integer>> a = new ArrayList<>();
        for (int i = 0; i < s.size(); i++){
            a.add(Arrays.asList(s.get(i), i, -1));
        }
        a.sort(Comparator.comparing((List<Integer> o) -> o.get(0)));
        for (int i = 0; i < a.size(); i++){
            a.get(i).set(2, i);
        }
        a.sort(Comparator.comparing((List<Integer> o) -> o.get(1)));
        int n = 0;
        for (int i = 0, c = 0; c < a.size()*2; i++, c++){
            if (c == a.size()) i = 0;
            List<Integer> f = a.get(i);
            if (i != f.get(2)){
                int tmp = s.get(f.get(2));
                s.set(f.get(2), s.get(i));
                s.set(i, tmp);
                List<Integer> temp = a.get(f.get(2));
                a.set(f.get(2), a.get(i));
                a.set(i, temp);
                n++;
            }
        }
        System.out.println(n);
        return s;
    }

    public static void main(String[] args) {
        List<Integer> a = Arrays.asList(6, 4, 3, 1, 2, 5);
        System.out.println(sort(a));
    }
}