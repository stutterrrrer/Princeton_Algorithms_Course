import java.util.Random;

public class StdRandom {

	public static void shuffle(Object[] arr) {
		int n = arr.length;

		Random rand = new Random();
		for (int i = 0; i < n; i++) {
			// generate a number in [0, i]:
			int r = rand.nextInt(i + 1);
			swap(arr, i, r);
		}
	}

	private static void swap(Object[] a, int i, int j) {
		Object temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
}