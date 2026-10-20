package graph;
import java.util.*;

public class Graph {
    public record Edge(int from, int to, int weight) {}
    public final int n;
    public final boolean directed;
    public final List<Edge> edges = new ArrayList<>();
    public final List<List<Edge>> adj = new ArrayList<>();

    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
    }

    public void add(int a, int b, int weight) {
        Edge edge = new Edge(a, b, weight);
        edges.add(edge);
        adj.get(a).add(edge);
        if (!directed) adj.get(b).add(new Edge(b, a, weight));
    }

    public static Graph random(int n, int m, boolean directed, boolean dag, long seed) {
        Graph g = new Graph(n, directed);
        Random r = new Random(seed);
        Set<Long> used = new HashSet<>();
        if (!directed) {
            for (int i = 1; i < n; i++) {
                int j = r.nextInt(i);
                g.add(j, i, 1 + r.nextInt(100));
                used.add((long) j * n + i);
            }
        }
        while (g.edges.size() < m) {
            int a = r.nextInt(n), b = r.nextInt(n);
            if (a == b) continue;
            if (dag || !directed) {
                int low = Math.min(a, b), high = Math.max(a, b);
                a = low;
                b = high;
            }
            if (used.add((long) a * n + b)) g.add(a, b, 1 + r.nextInt(100));
        }
        return g;
    }
}
