import java.util.LinkedList;

public class Digraph {
	private final int vertices;
	// array of linked-lists.
	private LinkedList<Integer>[] adjLists;

	public Digraph(int vertices) {
		this.vertices = vertices;
		// ugly cast to create generic array
		adjLists = (LinkedList<Integer>[]) new Object[vertices];
		// initialize each array element
		for (int vertex = 0; vertex < vertices; vertex++)
			adjLists[vertex] = new LinkedList<>();
	}

	public void addEdge(int vertex1, int v2) {
		// represent each edge only once - from v1 to v2
		// only difference from undirected graph's adjacency lists
		adjLists[vertex1].add(v2);
	}

	public Iterable<Integer> adjacentVertices(int vertex) {
		// returns a linked list of vertices(int)
		return adjLists[vertex];
	}

	public int numberOfVertices() {
		return vertices;
	}
}
