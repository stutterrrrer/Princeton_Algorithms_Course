public class ConnectedComponents {
	private boolean[] visited;
	private int[] componentID;
	private int componentCount;

	public ConnectedComponents(Graph graph) {

		final int n = graph.numberOfVertices();
		visited = new boolean[n];
		componentID = new int[n];
		for (int v = 0; v < n; v++) {
			if (!visited[v]) {
				depthFirstSearch(graph, v);
				componentCount++;
			}
		}

	}

	private void depthFirstSearch(Graph graph, int v) {
		visited[v] = true;
		componentID[v] = componentCount;
		for (int adjVertex : graph.adjacentVertices(v))
			if (!visited[adjVertex])
				depthFirstSearch(graph, adjVertex);
	}

	public int count() {
		return componentCount;
	}

	public int id(int vertex) {
		return componentID[vertex];
	}
}
