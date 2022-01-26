import java.util.LinkedList;

public class FlowNetwork {
	private final int V;
	private final LinkedList<FlowEdge>[] adjListsOfEdges;

	public FlowNetwork(int V) {
		this.V = V;
		adjListsOfEdges = (LinkedList<FlowEdge>[]) new Object[V];
		for (int v = 0; v < V; v++)
			adjListsOfEdges[v] = new LinkedList<>();
	}

	public void addEdge(FlowEdge e) {
		int v = e.from();
		int w = e.to();
		adjListsOfEdges[v].add(e); // add forward edge
		adjListsOfEdges[w].add(e); // add backward edge
	}

	public Iterable<FlowEdge> adjEdges(int v) {
		// returns both forward & backward edges
		return adjListsOfEdges[v];
	}

	public int V() {
		return V;
	}
}
