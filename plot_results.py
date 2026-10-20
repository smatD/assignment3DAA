import csv
from pathlib import Path
import matplotlib.pyplot as plt

with open('results/tables/benchmarks.csv', newline='') as file:
    data = list(csv.DictReader(file))

Path('results/plots').mkdir(parents=True, exist_ok=True)
for x, name in [('vertices', 'time_vs_vertices.png'), ('edges', 'time_vs_edges.png')]:
    plt.figure()
    for algorithm in ['Kruskal', 'Prim', 'Dijkstra', 'SCC', 'Topological']:
        rows = [r for r in data if r['algorithm'] == algorithm and r['graph'] != 'Cycle']
        rows.sort(key=lambda r: int(r[x]))
        plt.plot([int(r[x]) for r in rows], [int(r['average_ns']) / 1_000_000 for r in rows], marker='o', label=algorithm)
    plt.xlabel('Number of ' + x)
    plt.ylabel('Average time (ms)')
    plt.legend()
    plt.tight_layout()
    plt.savefig('results/plots/' + name)
    plt.close()
print('Plots saved')
