import graph.Graph;
import mst.MSTAlgorithms;
import connectivity.Connectivity;
import shortestpath.ShortestPath;
import java.util.*;

public class Tests {
    static void check(boolean condition) {
        if (!condition) throw new AssertionError("Test failed");
    }

    static void run() {
        Graph g = new Graph(4, false);
        g.add(0, 1, 1); g.add(1, 2, 1); g.add(2, 3, 2); g.add(0, 3, 2);
        check(MSTAlgorithms.kruskal(g).weight() == 4);
        check(MSTAlgorithms.prim(g).weight() == MSTAlgorithms.kruskal(g).weight());
        check(MSTAlgorithms.kruskal(g).connected());

        Graph disconnected = new Graph(4, false);
        disconnected.add(0, 1, 3);
        check(!MSTAlgorithms.kruskal(disconnected).connected());
        check(!MSTAlgorithms.prim(disconnected).connected());
        check(ShortestPath.dijkstra(disconnected, 0).distance()[3] == Long.MAX_VALUE);
        check(ShortestPath.path(ShortestPath.dijkstra(disconnected, 0), 3).isEmpty());

        Graph single = new Graph(1, false);
        check(MSTAlgorithms.kruskal(single).connected());
        check(MSTAlgorithms.prim(single).connected());
        check(MSTAlgorithms.kruskal(single).weight() == 0);

        Graph dag = new Graph(4, true);
        dag.add(0, 1, 1); dag.add(0, 2, 1); dag.add(1, 3, 1); dag.add(2, 3, 1);
        List<Integer> order = Connectivity.topological(dag);
        check(order.size() == 4);
        for (Graph.Edge e : dag.edges) check(order.indexOf(e.from()) < order.indexOf(e.to()));

        Graph cycle = new Graph(4, true);
        cycle.add(0, 1, 1); cycle.add(1, 0, 1); cycle.add(1, 2, 1); cycle.add(2, 3, 1);
        check(Connectivity.topological(cycle).isEmpty());
        List<List<Integer>> components = Connectivity.scc(cycle);
        Set<Integer> all = new HashSet<>();
        for (List<Integer> c : components) for (int v : c) check(all.add(v));
        check(all.size() == 4 && components.size() == 3);

        Graph routes = new Graph(4, false);
        routes.add(0, 1, 5); routes.add(0, 2, 2); routes.add(2, 1, 1);
        ShortestPath.Paths paths = ShortestPath.dijkstra(routes, 0);
        check(paths.distance()[1] == 3);
        check(ShortestPath.path(paths, 1).equals(List.of(0, 2, 1)));

        Graph negative = new Graph(2, true);
        negative.add(0, 1, -1);
        boolean rejected = false;
        try { ShortestPath.dijkstra(negative, 0); } catch (IllegalArgumentException ex) { rejected = true; }
        check(rejected);
        System.out.println("Tests passed");
    }
}
