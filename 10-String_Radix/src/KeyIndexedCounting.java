public class KeyIndexedCounting {
	public static int[] keyIndexCounting
			(int[] sortTarget, int radix) {
		int n = sortTarget.length;
		int[] frequencies = new int[radix + 1];

		// record individual element frequencies
		for (int element : sortTarget)
			frequencies[element + 1]++;

		// turn into cumulative frequency
		for (int r = 0; r < radix; r++)
			frequencies[r + 1] += frequencies[r];
		// rename the cumulative frequency array
		int[] indexForNext = frequencies;

		// create and fill the sorted auxiliary array:
		int[] aux = new int[n];
		for (int element : sortTarget)
			aux[indexForNext[element]++] = element;
		// insert the first "2" at aux[5],
		// increment indexForNext[2] to 6

		return aux;
	}

	public static void main(String[] args) {
		int[] arr = {4, 0, 2, 5, 5, 1, 3, 1, 5, 1, 4, 0};
		for (int element : keyIndexCounting(arr, 6))
			System.out.println(element);
	}
}