import java.util.LinkedList;

public class Graph {
	private final int vertices;
	// array of linked-lists.
	private LinkedList<Integer>[] adjLists;

	public Graph(int vertices) {
		this.vertices = vertices;
		// ugly cast to create generic array
		adjLists = (LinkedList<Integer>[]) new Object[vertices];
		// initialize each array element
		for (int vertex = 0; vertex < vertices; vertex++)
			adjLists[vertex] = new LinkedList<>();
	}

	public void addEdge(int vertex1, int v2) {
		// represent each edge twice
		// parallel edges and self-loops allowed.
		adjLists[vertex1].add(v2);
		adjLists[v2].add(vertex1);
	}

	public Iterable<Integer> adjacentVertices(int vertex) {
		// returns a linked list of vertices(int)
		return adjLists[vertex];
	}

	public int numberOfVertices() {
		return vertices;
	}
}