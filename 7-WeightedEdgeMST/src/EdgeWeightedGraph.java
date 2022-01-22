import java.util.LinkedList;

public class EdgeWeightedGraph {
	private final int V;
	private final LinkedList<Edge>[] adjListsOfEdges;

	public EdgeWeightedGraph(int V) {
		this.V = V;
		adjListsOfEdges = (LinkedList<Edge>[]) new Object[V];
		for (int v = 0; v < V; v++)
			adjListsOfEdges[v] = new LinkedList<>();
	}

	public void addEdge(Edge edge) {
		// given an edge that contains the information on
		// 2 vertices and weight:
		int v = edge.either(), w = edge.other(v);
		adjListsOfEdges[v].add(edge);
		adjListsOfEdges[w].add(edge);
	}

	public Iterable<Edge> adjEdges(int v) {
		return adjListsOfEdges[v];
	}

	public Iterable<Edge> edges() {
		// not implemented; returns all edges (no duplicates)
		return null;
	}

	public int V() {
		return V;
	}
}