import graph.Graph;
import mst.MSTAlgorithms;
import connectivity.Connectivity;
import shortestpath.ShortestPath;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Tests.run();
        Graph roads = new Graph(5, false);
        roads.add(0, 1, 4); roads.add(0, 2, 2); roads.add(1, 2, 1);
        roads.add(1, 3, 5); roads.add(2, 3, 8); roads.add(3, 4, 3);
        System.out.println("Kruskal MST: " + MSTAlgorithms.kruskal(roads).weight());
        System.out.println("Prim MST: " + MSTAlgorithms.prim(roads).weight());
        ShortestPath.Paths paths = ShortestPath.dijkstra(roads, 0);
        for (int v = 0; v < roads.n; v++)
            System.out.println("District " + v + ": distance " + paths.distance()[v] + ", path " + ShortestPath.path(paths, v));
        Graph directed = new Graph(4, true);
        directed.add(0, 1, 1); directed.add(1, 2, 1); directed.add(2, 0, 1); directed.add(2, 3, 1);
        System.out.println("Topological order (empty if cycle): " + Connectivity.topological(directed));
        System.out.println("SCC: " + Connectivity.scc(directed));
        Benchmark.run();
    }
}
