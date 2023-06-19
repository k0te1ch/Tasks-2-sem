import java.util.*;

public class TimeGraph {
    private final List<List<Integer>> intervals = new ArrayList<>();
    private final int endTime;
    private final int length;
    private List<List<Integer>> graph = new ArrayList<>();
    private int N;

    public TimeGraph(int N, int endTime, int length) {
        this.N = N;
        this.endTime = endTime;
        this.length = length;
        for (int i = 0; i <= endTime; i++) graph.add(new ArrayList<>());
    }

    public void addGuards(List<Integer> T1, List<Integer> T2) {
        for (int i = 0; i < N; i++) {
            int t1 = T1.get(i), t2 = T2.get(i);
            intervals.add(Arrays.asList(t1, t2));
            for (int d = t1; d <= t2; d++) graph.get(d).add(i);
        }
    }

    public void addGuard(int t1, int t2) {
        N++;
        intervals.add(Arrays.asList(t1, t2));
        for (int d = t1; d <= t2; d++) graph.get(d).add(N - 1);
    }

    public boolean checkA() {
        for (int i = 0; i <= endTime; i++) if (graph.get(i).size() < 2) return false;
        return true;
    }

    public List<List<Integer>> checkB() {
        List<List<Integer>> res = new ArrayList<>();
        int k = -1, l = -1, number = graph.get(0).size();
        for (int i = 0; i <= endTime; i++) {
            int len = graph.get(i).size();
            if (number != len && number < 2) {
                res.add(Arrays.asList(k, l, number));
            }
            if (len < 2) {
                if (number != len || k == -1) {
                    k = i;
                }
                l = i;
            }
            number = len;
        }
        if (l == endTime) res.add(Arrays.asList(k, l, number));
        return res;
    }

    public List<List<Integer>> checkV() {
        List<List<Integer>> res = new ArrayList<>(), intervals = checkB();
        List<List<Integer>> graph1 = new ArrayList<>();
        for (List<Integer> i : graph) {
            List<Integer> tmp = new ArrayList<>(i);
            graph1.add(tmp);
        }
        while (intervals.size() > 0) {
            List<Integer> item = intervals.get(0);
            int t1 = item.get(0), t2 = item.get(1), number = item.get(2);
            if (t2 - t1 <= length) res.add(Arrays.asList(t1, t1 + length));
            else {
                int offset = t1 + length;
                for (int i = 0; i < number; i++) res.add(Arrays.asList(t1, offset));
                while (offset + length <= t2) {
                    for (int i = 0; i < number; i++) res.add(Arrays.asList(offset + 1, offset + length));
                    offset += length;
                }
                for (int i = 0; i < number; i++) res.add(Arrays.asList(offset + 1, t2));
            }
            List<List<Integer>> res1 = new ArrayList<>(res);
            for (List<Integer> items : res1) {
                int T1 = items.get(0), T2 = items.get(1);
                if (T2 > endTime) {
                    res.remove(Arrays.asList(T1, T2));
                    T2 = endTime;
                    T1 = T2 - length;
                    res.add(Arrays.asList(T1, T2));
                }
                for (int i = T1; i <= T2; i++) graph.get(i).add(1);
            }
            intervals = checkB();
        }
        graph = new ArrayList<>();
        for (List<Integer> i : graph1) {
            List<Integer> tmp = new ArrayList<Integer>(i);
            graph.add(tmp);
        }
        Set<List<Integer>> set = new HashSet<>(res);
        res.clear();
        res.addAll(set);
        return res;
    }

    private List<List<List<Integer>>> splitIntervals() {
        List<List<Integer>> intervals1 = new ArrayList<>();
        for (List<Integer> i : intervals) {
            List<Integer> tmp = new ArrayList<>(i);
            intervals1.add(tmp);
        }
        int totalLength = 0;
        for (List<Integer> item : intervals1) totalLength += item.get(1) - item.get(0);
        if (totalLength / endTime < 2) return new ArrayList<>();

        intervals1.sort(Comparator.comparing(p -> p.get(1) - p.get(0)));
        Collections.reverse(intervals1);
        List<List<List<Integer>>> res = new ArrayList<>();

        for (List<Integer> interval : intervals1) {
            if (totalLength % endTime != 0) {
                int sumRes = 0;
                for (List<List<Integer>> item1 : res)
                    for (List<Integer> item : item1) sumRes += item.get(1) - item.get(0);
                if (res.size() >= 2 && sumRes / endTime >= 2) break;
                if (interval.get(1) - interval.get(0) >= endTime) {
                    res.add(List.of(interval));
                    continue;
                }
                List<List<Integer>> group = new ArrayList<>(res.get(res.size() - 1));
                int sumGroup = 0;
                for (List<Integer> item : group) sumGroup += item.get(1) - item.get(0);
                if (sumGroup >= endTime) res.add(List.of(Arrays.asList(interval.get(0), interval.get(1))));
                else {
                    res.remove(res.size() - 1);
                    group.add(Arrays.asList(interval.get(0), interval.get(1)));
                    res.add(group);
                }
            } else {
                if (res.isEmpty()) res.add(List.of(interval));
                else {
                    boolean added = false;
                    for (List<List<Integer>> group : res) {
                        List<List<Integer>> group1 = new ArrayList<>(group);
                        int index = res.indexOf(group);
                        int groupLength = 0;
                        for (List<Integer> item : group) groupLength += item.get(1) - item.get(0);
                        if (groupLength + (interval.get(1) - interval.get(0)) <= endTime) {
                            group1.add(interval);
                            res.set(index, group1);
                            added = true;
                        }
                    }
                    if (!added) res.add(List.of(interval));
                }
            }
        }
        return res;
    }

    private List<List<List<Integer>>> eliminateGaps() {
        List<List<List<Integer>>> res = new ArrayList<>();
        List<List<List<Integer>>> temp = splitIntervals();
        for (List<List<Integer>> intervals : temp) {
            if (intervals.size() == 1) continue;
            List<List<Integer>> sortedIntervals = new ArrayList<>(intervals);
            sortedIntervals.sort(Comparator.comparing(p -> p.get(0)));
            List<List<Integer>> result = new ArrayList<>(Collections.singletonList(sortedIntervals.get(0)));
            for (List<Integer> interval : sortedIntervals.subList(1, sortedIntervals.size())) {
                List<Integer> prev = result.get(result.size() - 1);
                if (interval.get(0) <= prev.get(1)) {
                    int shift;
                    if (prev.get(1).equals(interval.get(1))) shift = interval.get(1) - prev.get(0);
                    else shift = prev.get(1) - interval.get(0) + 1;
                    if (prev.get(1) == endTime) shift *= -1;
                    int shiftedStart = interval.get(0) + shift;
                    int shiftedEnd = interval.get(1) + shift;

                    if (shiftedEnd > endTime) {
                        shiftedStart = endTime - interval.get(1) + interval.get(0);
                        shiftedEnd = endTime;
                    }

                    if (shiftedStart < 0) {
                        shiftedStart = 0;
                        shiftedEnd = interval.get(1) - interval.get(0);
                    }

                    result.add(Arrays.asList(shiftedStart, shiftedEnd));
                    if (shiftedStart - interval.get(0) != 0)
                        res.add(Arrays.asList(interval, List.of(shiftedStart - interval.get(0))));
                } else {
                    result.add(interval);
                }
            }
        }
        return res;
    }

    public List<List<Integer>> checkG() {
        List<List<List<Integer>>> res = eliminateGaps();
        List<List<Integer>> ans = new ArrayList<>();
        for (List<List<Integer>> i : res) {
            int skip = 0;
            int number = getNumber(i.get(0).get(0), i.get(0).get(1), skip);
            while (true) {
                boolean breaking = false;
                for (List<Integer> a : ans) {
                    if (a.get(0) == number) {
                        skip++;
                        number = getNumber(i.get(0).get(0), i.get(0).get(1), skip);
                        breaking = true;
                        break;
                    }
                }
                if (!breaking) break;
            }
            ans.add(Arrays.asList(number, i.get(1).get(0)));
        }
        return ans;
    }

    public void removeGuard(int from, int to) {
        if (!intervals.contains(Arrays.asList(from, to))) return;
        N--;
        for (int i = from; i <= to; i++) {
            List<Integer> item = graph.get(i);
            if (item.size() > 0) item.remove(item.size() - 1);
        }
        intervals.remove(Arrays.asList(from, to));
    }

    public String[][] toTable() {
        int max = 0;
        for (List<Integer> item : graph) max = Math.max(max, item.size());
        String[][] table = new String[max][graph.size()];
        for (int i = 0; i < graph.size(); i++) {
            int d = 0;
            for (d = 0; d < graph.get(i).size(); d++) {
                table[d][i] = String.valueOf(graph.get(i).get(d));
            }
            for (; d < max; d++) table[d][i] = "";
        }
        return table;
    }

    private int getNumber(int from, int to, int skip) {
        if (!intervals.contains(Arrays.asList(from, to))) return -1;
        int index = -1;
        int cnt = 0;
        for (List<Integer> interval : intervals) {
            index++;
            if (interval.equals(Arrays.asList(from, to))) {
                if (skip <= 0) return index;
                skip--;
                cnt++;
            }
        }
        if (cnt > 0) return index;
        return -1;
    }

    public String fromGraph() {
        StringBuilder s = new StringBuilder();
        s.append(endTime).append("\n");
        s.append(N).append("\n");
        for (int i = 0; i < N; i++) {
            s.append(String.format("%s %s\n", intervals.get(i).get(0), intervals.get(i).get(1)));
        }
        s.append(length);
        return s.toString();
    }
}
