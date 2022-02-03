public class LSD {
	public static void lsdRadixSort(String[] strArr, int w) {
		int radix = 256; // ASCII alphabet
		int n = strArr.length; // number of strings to sort
		String[] aux = new String[n];

		// from right-most (least-significant) digit to left.
		// do key-indexed counting for each character (each digit).
		for (int d = w - 1; d >= 0; d--) {
			int[] frequencies = new int[radix + 1];
			for (String string : strArr)
				frequencies[string.charAt(d) + 1]++; // the 'd'th character

			for (int r = 0; r < radix; r++)
				frequencies[r + 1] += frequencies[r];
			int[] indexForNext = frequencies;

			for (String string : strArr)
				aux[indexForNext[string.charAt(d)]++] = string;

			System.arraycopy(aux, 0, strArr, 0, n);
		}
	}
}
