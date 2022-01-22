public class AcyclicSP {
	private DirectedEdge[] edgeTo;
	private double[] distTo;

	public AcyclicSP(EdgeWeightedDigraph graph, int source) {
		edgeTo = new DirectedEdge[graph.V()];
		distTo = new double[graph.V()];

		for (int v = 0; v < graph.V(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		distTo[source] = 0.0;

		// use a Topological graph-processing-class to find the topo-order.
		Topological topological = new Topological(graph);
		for (int v : topological.order())
			for (DirectedEdge e : graph.adjEdges(v))
				relax(e);
	}
}
