import graph.Graph;
import mst.MSTAlgorithms;
import connectivity.Connectivity;
import shortestpath.ShortestPath;
import java.nio.file.*;
import java.util.*;

public class Benchmark {
    interface Job { long run(); }
    record Measurement(long averageNs, long averageOps) {}

    static Measurement measure(Job job) {
        long time = 0, ops = 0;
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            long result = job.run();
            time += System.nanoTime() - start;
            ops += result;
        }
        return new Measurement(time / 5, ops / 5);
    }

    static void add(StringBuilder csv, String algorithm, String type, int n, int m, Measurement result, String detail) {
        csv.append(algorithm).append(',').append(type).append(',').append(n).append(',').append(m)
           .append(',').append(result.averageNs()).append(',').append(result.averageOps())
           .append(',').append(detail).append('\n');
    }

    static void run() throws Exception {
        Files.createDirectories(Path.of("results/tables"));
        StringBuilder csv = new StringBuilder("algorithm,graph,vertices,edges,average_ns,operations,result\n");
        int[][] sizes = {{10, 30}, {100, 500}, {1000, 5000}};
        for (int[] size : sizes) {
            int n = size[0], m = size[1];
            Graph roads = Graph.random(n, m, false, false, 12345 + n);
            Graph dag = Graph.random(n, m, true, true, 23456 + n);
            Graph cyclic = Graph.random(n, m, true, true, 23456 + n);
            cyclic.add(0, n - 1, 1);
            cyclic.add(n - 1, 0, 1);
            Graph directed = Graph.random(n, m, true, false, 34567 + n);

            MSTAlgorithms.MST k = MSTAlgorithms.kruskal(roads), p = MSTAlgorithms.prim(roads);
            if (!k.connected() || k.weight() != p.weight()) throw new AssertionError("MST weights differ");
            add(csv, "Kruskal", "Undirected", n, m,
                measure(() -> MSTAlgorithms.kruskal(roads).operations()), "weight=" + k.weight());
            add(csv, "Prim", "Undirected", n, m,
                measure(() -> MSTAlgorithms.prim(roads).operations()), "weight=" + p.weight());
            add(csv, "Dijkstra", "Undirected", n, m,
                measure(() -> ShortestPath.dijkstra(roads, 0).operations()), "source=0");
            add(csv, "Topological", "DAG", n, m,
                measure(() -> { Connectivity.topological(dag); return Connectivity.operations; }), "order_found");
            add(csv, "Topological", "Cycle", n, m + 2,
                measure(() -> { Connectivity.topological(cyclic); return Connectivity.operations; }), "cycle_detected");
            int components = Connectivity.scc(directed).size();
            add(csv, "SCC", "Directed", n, m,
                measure(() -> { Connectivity.scc(directed); return Connectivity.operations; }), "components=" + components);
        }
        Files.writeString(Path.of("results/tables/benchmarks.csv"), csv);
        System.out.println("Benchmark saved to results/tables/benchmarks.csv");
    }
}
