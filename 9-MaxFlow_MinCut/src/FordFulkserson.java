import javax.crypto.spec.PSource;
import java.util.LinkedList;
import java.util.Queue;

public class FordFulkserson {
	private boolean[] visitedOnThisPath; // instance variable, but specific to each explored path
	private FlowEdge[] edgeToOnThisPath; // instance variable, but specific to each explored path
	private double flowValue;

	public FordFulkserson(FlowNetwork graph, int source, int target) {
		flowValue = 0.0;
		// calling hasAugmentingPath (re)builds the visited[] & edgeTo[] arrays
		while (hasAugmentingPath(graph, source, target)) {

			// calculate bottleNeck = the amount of flow we can increase through this augmenting path
			double bottleNeck = Double.POSITIVE_INFINITY;
			// trace back from target to source on this augmenting path
			for (int v = target; v != source; v = edgeToOnThisPath[v].other(v))
				// first iteration: bottleNeck = residualCapacity from `edgeTo[target]` to `target`.
				bottleNeck = Math.min(bottleNeck, edgeToOnThisPath[v].residualCapacityTo(v));

			// add the calculated bottleNeck to each edge's flow (augment flow)
			for (int v = target; v != source; v = edgeToOnThisPath[v].other(v))
				edgeToOnThisPath[v].addResidualFlowTo(v, bottleNeck);

			flowValue += bottleNeck;
		}
	}

	private boolean hasAugmentingPath(FlowNetwork graph, int source, int target) {
		edgeToOnThisPath = new FlowEdge[graph.V()];
		visitedOnThisPath = new boolean[graph.V()];

		Queue<Integer> queue = new LinkedList<>();
		queue.add(source);
		visitedOnThisPath[source] = true;

		// BFS
		while (!queue.isEmpty()) {
			int v = queue.poll();
			for (FlowEdge e : graph.adjEdges(v)) {
				int w = e.other(v);
				// find the first edge (forward or backward) that has residual capacity
				// i.e. ignore those without residual capacity in this BFS.
				if (e.residualCapacityTo(w) > 0 && !visitedOnThisPath[w]) {
					edgeToOnThisPath[w] = e;
					visitedOnThisPath[w] = true;
					queue.add(w);
				}
			}
		}

		// if a path of only edges with residual-capacity leads to target
		// then this is an augmenting path.
		return visitedOnThisPath[target];
	}

	public double maxFlowValue() {
		// the constructor finds the max flow of this network
		return flowValue;
	}

	public boolean inMinCutSetA(int v) {
		// after the constructor,
		// visitedOnThisPath[] will be the final state of the FlowNetwork (with no more augmenting paths)
		// so if `v` can be reached from `source` through
		// a path where all edges have residual capacity,
		// then `v` is in the set A(containing `source`) of the min-cut (A,B)
		return visitedOnThisPath[v];
	}
}