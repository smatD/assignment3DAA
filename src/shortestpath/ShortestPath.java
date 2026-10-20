package shortestpath;
import graph.Graph;
import java.util.*;
public class ShortestPath {
    public record Paths(long[] distance, int[] previous, long operations) {}
    public static Paths dijkstra(Graph g, int source) {
        if (source < 0 || source >= g.n) throw new IllegalArgumentException("Invalid source");
        for (Graph.Edge e : g.edges) if (e.weight() < 0) throw new IllegalArgumentException("Negative weight");
        long[] distance = new long[g.n];
        int[] previous = new int[g.n];
        Arrays.fill(distance, Long.MAX_VALUE);
        Arrays.fill(previous, -1);
        distance[source] = 0;
        long[] comparisons = {0};
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> {
            comparisons[0]++;
            return Long.compare(a[1], b[1]);
        });
        pq.add(new long[]{source, 0});
        long examined = 0;
        while (!pq.isEmpty()) {
            long[] item = pq.poll();
            int v = (int) item[0];
            if (item[1] != distance[v]) continue;
            for (Graph.Edge e : g.adj.get(v)) {
                examined++;
                long candidate = distance[v] + e.weight();
                if (candidate < distance[e.to()]) {
                    distance[e.to()] = candidate;
                    previous[e.to()] = v;
                    pq.add(new long[]{e.to(), candidate});
                }
            }
        }
        return new Paths(distance, previous, examined + comparisons[0]);
    }

    public static List<Integer> path(Paths result, int target) {
        if (result.distance()[target] == Long.MAX_VALUE) return List.of();
        LinkedList<Integer> path = new LinkedList<>();
        for (int v = target; v != -1; v = result.previous()[v]) path.addFirst(v);
        return path;
    }
}
