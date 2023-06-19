import java.util.*;

class FriendshipGraph {
    private final Map<String, Map<String, Double>> FriendshipGraph;

    public FriendshipGraph() {
        FriendshipGraph = new HashMap<>();
    }

    public void addFriend(String person1, String person2, double relation) {
        FriendshipGraph.putIfAbsent(person1, new HashMap<>());
        FriendshipGraph.putIfAbsent(person2, new HashMap<>());

        FriendshipGraph.get(person1).put(person2, relation);
        FriendshipGraph.get(person2).put(person1, relation);
    }

    public double getRelation(String person1, String person2) {
        if (!(FriendshipGraph.containsKey(person1) && FriendshipGraph.containsKey(person2))) return 0;

        if (FriendshipGraph.get(person2).containsKey(person1)) return FriendshipGraph.get(person1).get(person2);
        if (FriendshipGraph.get(person1).containsKey(person2)) return FriendshipGraph.get(person2).get(person1);
        return 0;
    }

    private List<List<String>> getNeighbors(String node) {
        if (!(FriendshipGraph.containsKey(node))) return new ArrayList<>();
        List<List<String>> list = new ArrayList<>();
        for (String i : FriendshipGraph.get(node).keySet())
            list.add(Arrays.asList(i, String.valueOf(FriendshipGraph.get(node).get(i))));
        return list;
    }

    public List<String> getVertex() {
        return List.copyOf(FriendshipGraph.keySet());
    }

    public double calculateRelation(String person1, String person2) {
        if (FriendshipGraph.size() < 2) return 0;

        double relation = getRelation(person1, person2);
        if (relation == -1 || relation == 1) return relation;

        Set<List<String>> visited = new HashSet<>();
        Deque<List<String>> deque = new ArrayDeque<>();
        deque.add(Arrays.asList(person1, "1", null));
        double countAllies = 0, countEnemies = 0;
        while (deque.size() > 0) {
            List<String> items = deque.pop();
            String item = items.get(0), prev = items.get(2);
            double relationship = Double.parseDouble(items.get(1));
            if (item.equals(person2)) {
                if (relationship == -1) countEnemies++;
                else countAllies++;
                continue;
            }
            List<String> path = Arrays.asList(item, prev);
            if (visited.contains(path)) continue;
            visited.add(path);

            for (List<String> i : getNeighbors(item)) {
                List<String> tmp = new ArrayList<>();
                tmp.add(i.get(0));
                if (relationship < 0) tmp.add(String.valueOf(Double.parseDouble(i.get(1)) * (-1)));
                else tmp.add(i.get(1));
                tmp.add(item);
                deque.addFirst(tmp);
            }
        }


        if (countAllies == 0) {
            if (countEnemies == 0) return 0;
            return -1;
        }
        if (countEnemies == 0) {
            return 1;
        }

        if (countEnemies > countAllies) return -countEnemies / (countAllies + countEnemies);
        return countAllies / (countAllies + countEnemies);
    }

    public Map<String, List<List<String>>> getGraph() {
        Map<String, List<List<String>>> graph = new HashMap<>();
        for (String key : FriendshipGraph.keySet()) graph.put(key, getNeighbors(key));
        return graph;
    }
}

