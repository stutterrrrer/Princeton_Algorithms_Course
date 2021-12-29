import java.util.Stack;

public class TopologicalOrder {
	private boolean[] visited;
	private Stack<Integer> reversePost;

	public TopologicalOrder(Digraph graph) {
		reversePost = new Stack<>();
		int n = graph.numberOfVertices();
		visited = new boolean[n];
		for (int v = 0; v < graph.numberOfVertices(); v++) {
			if (!visited[v]) dfsAndPush(graph, v);
		}
	}

	private void dfsAndPush(Digraph graph, int v) {
		visited[v] = true;
		for (int w : graph.adjacentVertices(v))
			if (!visited[w]) dfsAndPush(graph, w);
		// only push this vertex after all its "children" / dependants have been pushed.
		reversePost.push(v);
	}

	public Iterable<Integer> reversePost() {
		return reversePost;
	}
}
