import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class KruskalMST {
	private Queue<Edge> mst = new LinkedList<>();

	public KruskalMST(EdgeWeightedGraph graph) {
		PriorityQueue<Edge> minPQ = new PriorityQueue<>(); // compares weight
		for (Edge e : graph.edges())
			minPQ.add(e);

		// see the UnionFind - QuickUnionPathCompression data type in Notion
		UnionFind uf = new UnionFind(graph.V());

		while (!minPQ.isEmpty() && mst.size() < graph.V() - 1) {
			Edge e = minPQ.remove();
			int v = e.either(), w = e.other(v);
			if (!uf.connected(v, w)) { // detect cycle
				uf.union(v,w);
				mst.add(e);
			}
		}
	}

	public Iterable<Edge> edges() {
		return mst;
	}
}
