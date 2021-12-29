import java.util.Stack;

public class DirectedCycle {
	private boolean[] visited;
	private int[] cameFrom;

	// different from a common DFS traversal:
	private boolean[] visitedOnThisPath; // mimics the recursion call stack
	private Stack<Integer> cycle; // vertices on a cycle (if one exists)

	public DirectedCycle(Digraph graph) {
		int n = graph.numberOfVertices();
		visited = new boolean[n];
		cameFrom = new int[n];

		// different from a common DFS:
		visitedOnThisPath = new boolean[n];
		// check every un-visited vertex as source vertex until a cycle is found
		for (int v = 0; v < n; v++)
			if (!visited[v]) dfsCycle(graph, v);
	}

	private void dfsCycle(Digraph graph, int v) {
		visited[v] = true;
		// this vertex is now on the recursion call stack, i.e. on this path
		visitedOnThisPath[v] = true;
		for (int w : graph.adjacentVertices(v)) {
			// dfs BEFORE checking for cycle - probably takes less time to find the first cycle
			if (!visited[w]) {
				cameFrom[w] = v;
				dfsCycle(graph, w); // w added to recursive stack
			}
			// if this adjacent vertex has already been visited earlier on this path: cycle
			else if (visitedOnThisPath[w]) {
				cycle = new Stack<>();
				for (int vOnCycle = v; vOnCycle != w; vOnCycle = cameFrom[vOnCycle])
					cycle.push(vOnCycle);
				cycle.push(w);
				cycle.push(v); // complete the cycle: start & end at `v`.

				return;
			}
			// popped off the recursion stack; i.e. off this path.
			visitedOnThisPath[w] = false;
		}
	}

	public boolean hasCycle() {
		return cycle != null;
	}

	public Iterable<Integer> cycle() {
		return cycle;
	}

}
