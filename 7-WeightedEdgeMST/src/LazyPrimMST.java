import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class LazyPrimMST {

	private boolean[] marked; // vertices currently on MST
	private Queue<Edge> mst; // MST edges
	private PriorityQueue<Edge> pq; // PQ of edges

	public LazyPrimMST(EdgeWeightedGraph G) {

		pq = new PriorityQueue<>();
		mst = new LinkedList<>();
		marked = new boolean[G.V()];
		visit(G, 0);

		while (!pq.isEmpty() && mst.size() < G.V() - 1) {

			Edge e = pq.remove();
			int v = e.either(), w = e.other(v);
			if (marked[v] && marked[w]) continue;
			mst.add(e);
			if (!marked[v]) visit(G, v);
			if (!marked[w]) visit(G, w);

		}
	}

	private void visit(EdgeWeightedGraph G, int v) {

		marked[v] = true;
		for (Edge e : G.adjEdges(v))
			if (!marked[e.other(v)]) pq.add(e);
	}

	public Iterable<Edge> mst() {
		return mst;
	}
}