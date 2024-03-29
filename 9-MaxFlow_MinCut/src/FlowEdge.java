public class FlowEdge {
	private final int v, w; // directed edge: from v to w
	private final double capacity;
	private double flow;

	public FlowEdge(int v, int w, double capacity) {
		this.v = v;
		this.w = w;
		this.capacity = capacity;
	}

	public double residualCapacityTo(int vertex) {
		if (vertex == v) return flow; // forward edge
		else if (vertex == w) return capacity - flow; // backward edge
		else throw new RuntimeException("Illegal endpoint");
	}

	public void addResidualFlowTo(int vertex, double delta) {
		if (vertex == v) flow -= delta;
		else if (vertex == w) flow += delta;
		else throw new RuntimeException("Illegal endpoint");
	}

	public int from() {return v;}
	public int to() {return w;}
	public double capacity() {return capacity;}
	public double flow() {return flow;}
	public int other(int vertex) {
		if (vertex == v) return w;
		else if (vertex == w) return v;
		else throw new RuntimeException("Illegal endpoint");
	}
}