import java.util.Stack;

public class DFSfromVertex {
	private boolean[] visited;
	private int[] cameFrom;
	private int sourceVertex;

	public DFSfromVertex(Graph graph, int sourceVertex) {
		int n = graph.numberOfVertices();
		visited = new boolean[n];
		cameFrom = new int[n];
		depthFirstSearch(graph, sourceVertex);
	}

	private void depthFirstSearch(Graph graph, int vertex) {
		visited[vertex] = true;
		for (int adjVertex : graph.adjacentVertices(vertex))
			if (!visited[adjVertex]) {
				cameFrom[adjVertex] = vertex;
				// recursion:
				depthFirstSearch(graph, adjVertex);
			}
	}

	public boolean hasPathTo(int target) {
		return visited[target];
	}

	public Iterable<Integer> pathTo(int target) {
		if (!hasPathTo(target)) return null;
		// use stack, LIFO:
		// start at the target and trace back to source.
		Stack<Integer> path = new Stack<>();
		for (int vertex = target;
			 vertex != sourceVertex;
			 vertex = cameFrom[vertex])
			path.push(vertex);

		return path;
	}
}
