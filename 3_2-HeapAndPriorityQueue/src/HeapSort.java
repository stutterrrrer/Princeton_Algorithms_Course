public class HeapSort {

	public static void sort(Comparable[] arr) {
		int n = arr.length;
		// treat the array as 1-index here,
		// but convert to 0-index in private less() & exchange().
		// because only those 2 methods directly access the array.

		// first construct a heap-ordered max-heap.
		for (int k = n / 2; k >= 1; k--)
			demote1Indexed(arr, k, n);

		// then repeatedly move max to end of array
		while (n > 1) {
			exchange(arr, 1, n);
			demote1Indexed(arr, 1, --n);
		}
	}

	private static void demote1Indexed(Comparable[] heap, int keyIndex, int n) {
		// left = key * 2; right = left + 1; right <= n;
		// if the last element is a left child (i.e. empty right), then left <= n;

		while (keyIndex * 2 <= n) { // while this key has at least 1 child
			int leftChildIndex = keyIndex * 2;
			int rightChildIndex = leftChildIndex + 1;
			int indexOfBiggerChild = rightChildIndex <= n // this key may have no right child.
					&& less(heap, leftChildIndex, rightChildIndex) ?
					rightChildIndex : leftChildIndex;

			if (!less(heap, keyIndex, indexOfBiggerChild)) break;
			exchange(heap, keyIndex, indexOfBiggerChild);
			keyIndex = indexOfBiggerChild;
		}
	}

	private static boolean less(Comparable[] heap, int oneIndexedP, int oneIndexedQ) {
		// convert to 0-indexed.
		Comparable p = heap[oneIndexedP - 1];
		Comparable q = heap[oneIndexedQ - 1];
		return p.compareTo(q) < 0;
	}

	private static void exchange(Comparable[] heap, int oneIndexedP, int oneIndexedQ) {
		// convert to 0-indexed.
		int p = oneIndexedP - 1;
		int q = oneIndexedQ - 1;

		Comparable temp = heap[p];
		heap[p] = heap[q];
		heap[q] = temp;
	}


	// test
	public static void main(String[] args) {
		Integer[] arr = {2, 5, 5, 6, 1, -6, 20, -5};
		HeapSort.sort(arr);
		for (int i : arr) System.out.print(i + ", ");
	}
}
