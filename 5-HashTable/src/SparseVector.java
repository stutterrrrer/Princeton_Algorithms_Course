import java.util.HashMap;

public class SparseVector {

	private HashMap<Integer, Double> vector;

	public SparseVector() {
		vector = new HashMap<>();
	}

	public void put(int position, double nonZero) {
		vector.put(position, nonZero);
	}

	public double get(int position) {
		return vector.getOrDefault(position, 0.0);
	}

	public Iterable<Integer> indices() {
		return vector.keySet();
	}

	public double dotMultiplication(double[] that) {
		double sum = 0.0;
		for (int i : indices())
			sum += that[i] * this.get(i);
		return sum;
	}
}