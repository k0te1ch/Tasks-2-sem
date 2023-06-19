import bst.SimpleBSTreeMap;
import util.ArrayUtils;

import java.io.FileNotFoundException;
import java.util.*;

class StudentMarksCustom implements Comparable<StudentMarksCustom> {
    private final SimpleBSTreeMap<String, String> marks = new SimpleBSTreeMap<>();
    // subject - mark

    public StudentMarksCustom(String subject, String mark) {
        marks.put(subject, mark);
    }

    @Override
    public String toString() {
        return marks.toString();
    }

    public SimpleBSTreeMap<String, String> getMarks() {
        return marks;
    }

    public void addMarks(String key, String value) {
        marks.put(key, value);
    }

    public boolean equals(StudentMarksCustom other) {
        if (marks.size() != other.getMarks().size()) return false;
        for (String key : marks.keySet()) if (!marks.get(key).equals(other.getMarks().get(key))) return false;
        return true;
    }

    @Override
    public int compareTo(StudentMarksCustom other) {
        if (marks.size() != other.getMarks().size()) return -1;
        for (String key : marks.keySet()) if (!marks.get(key).equals(other.getMarks().get(key))) return 1;
        return 0;
    }
}

class StudentMarks implements Comparable<StudentMarks> {
    private final Map<String, String> marks = new HashMap<>();
    // subject - mark

    public StudentMarks(String subject, String mark) {
        marks.put(subject, mark);
    }

    @Override
    public String toString() {
        return marks.toString();
    }

    public Map<String, String> getMarks() {
        return marks;
    }

    public void addMarks(String key, String value) {
        marks.put(key, value);
    }

    public boolean equals(StudentMarks other) {
        if (marks.size() != other.getMarks().size()) return false;
        for (String key : marks.keySet()) if (!marks.get(key).equals(other.getMarks().get(key))) return false;
        return true;
    }

    @Override
    public int compareTo(StudentMarks other) {
        if (marks.size() != other.getMarks().size()) return -1;
        for (String key : marks.keySet()) if (!marks.get(key).equals(other.getMarks().get(key))) return 1;
        return 0;
    }
}

public class Main {
    public static List<Map<String, String>> readerCSV(String path, String splitter) throws FileNotFoundException {
        List<Map<String, String>> result = new ArrayList<>();
        String[] marks = ArrayUtils.readLinesFromFile(path);
        if (marks.length < 2) return result;

        String[] head = marks[0].split(splitter);
        for (String i : Arrays.asList(marks).subList(1, marks.length)) {
            Map<String, String> elem = new HashMap<>();
            String[] f = i.split(splitter);
            for (int d = 0; d < head.length; d++) {
                elem.put(head[d], f[d]);
            }
            result.add(elem);
        }

        return result;
    }

    public static Map<StudentMarks, List<String>> process(String path, String splitter) throws FileNotFoundException {
        Map<StudentMarks, List<String>> result = new HashMap<>();
        List<Map<String, String>> marks = readerCSV(path, splitter);
        Map<String, StudentMarks> studentMarks = new HashMap<>();
        for (Map<String, String> i : marks) {
            String fio = i.get("fio");
            if (studentMarks.containsKey(fio)) studentMarks.get(fio).addMarks(i.get("subject"), i.get("mark"));
            else studentMarks.put(fio, new StudentMarks(i.get("subject"), i.get("mark")));

        }
        for (String i : studentMarks.keySet()) {
            StudentMarks mark = studentMarks.get(i);
            boolean contains = false;
            for (StudentMarks key : result.keySet()) {
                if (key.compareTo(mark) == 0) {
                    contains = true;
                    mark = key;
                    break;
                }
            }
            if (contains) {
                List<String> temp = new ArrayList<>(result.get(mark));
                if (!temp.contains(i)) temp.add(i);
                result.remove(mark);
                result.put(mark, temp);
            } else result.put(mark, Collections.singletonList(i));
        }

        return result;
    }

    public static Map<StudentMarksCustom, List<String>> process2(String path, String splitter) throws FileNotFoundException {
        Map<StudentMarksCustom, List<String>> result = new HashMap<>();
        List<Map<String, String>> marks = readerCSV(path, splitter);
        Map<String, StudentMarksCustom> StudentMarksCustom = new HashMap<>();
        for (Map<String, String> i : marks) {
            String fio = i.get("fio");
            if (StudentMarksCustom.containsKey(fio))
                StudentMarksCustom.get(fio).addMarks(i.get("subject"), i.get("mark"));
            else StudentMarksCustom.put(fio, new StudentMarksCustom(i.get("subject"), i.get("mark")));

        }
        for (String i : StudentMarksCustom.keySet()) {
            StudentMarksCustom mark = StudentMarksCustom.get(i);
            boolean contains = false;
            for (StudentMarksCustom key : result.keySet()) {
                if (key.compareTo(mark) == 0) {
                    contains = true;
                    mark = key;
                    break;
                }
            }
            if (contains) {
                List<String> temp = new ArrayList<>(result.get(mark));
                if (!temp.contains(i)) temp.add(i);
                result.remove(mark);
                result.put(mark, temp);
            } else result.put(mark, Collections.singletonList(i));
        }

        return result;
    }


    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(process("D:\\Programming\\Java\\Tasks-2-sem\\task_6_8\\src\\marks.csv", "\\|"));
        System.out.println(process2("D:\\Programming\\Java\\Tasks-2-sem\\task_6_8\\src\\marks.csv", "\\|"));
    }
}