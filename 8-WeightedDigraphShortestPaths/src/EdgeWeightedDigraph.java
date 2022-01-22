import java.util.LinkedList;

public class EdgeWeightedDigraph {

	private final int V;
	private final LinkedList<DirectedEdge>[] adjListsOfEdges;

	public EdgeWeightedDigraph(int V) {
		this.V = V;
		adjListsOfEdges = (LinkedList<DirectedEdge>[]) new Object[V];
		for (int v = 0; v < V; v++)
			adjListsOfEdges[v] = new LinkedList<DirectedEdge>();
	}

	public void addEdge(DirectedEdge e) {
		int v = e.from();
		adjListsOfEdges[v].add(e); // only add edge (e = v -> w) to v
	}

	public Iterable<DirectedEdge> adjEdges(int v) {
		return adjListsOfEdges[v];
	}

	public int V() {
		return V;
	}

}