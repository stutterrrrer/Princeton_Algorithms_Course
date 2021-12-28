import java.util.LinkedList;
import java.util.Queue;

public class BFSfromVertex {

	private boolean[] marked;
	private int[] edgeTo;

	private void bfs(Graph G, int s) {

		Queue<Integer> q = new LinkedList<>();
		q.offer(s);
		marked[s] = true;
		while (!q.isEmpty()) {
			int v = q.poll();
			for (int w : G.adjacentVertices(v))
				if (!marked[w]) {
					q.offer(w);
					marked[w] = true;
					edgeTo[w] = v;
				}
		}
	}
}
