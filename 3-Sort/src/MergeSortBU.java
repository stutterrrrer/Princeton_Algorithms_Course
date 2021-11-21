public class MergeSortBU {

	public static void mergeSortBU(Comparable[] arr) {
		int n = arr.length;
		Comparable[] aux = new Comparable[n];

		// bottom up: start merging sub-arrays of size 1,
		// then double sub-array size in each pass:
		for (int size = 1; size < n; size *= 2) {
			// merge {0} with   {1}   -- {2} with   {3} for size 1;
			// merge {0,1} with {2,3} -- {4,5} with {6,7} for size 2.
			for (int left = 0; left < n - size; left += size * 2) {
				// why left < n - size:
				// e.g. if an array has 5 elements
				// then in the first pass after merging {2} & {3},
				// the last element remaining is arr[4],
				// and left would be incremented to 2 + (2 * size) = 4 = n - size

				merge(arr, aux, left, left + size - 1,
						Math.min(left + 2 * size - 1, n - 1));

				// why math.min():
				// if an array has 15 elements, when size = 4:
				// after 1st pass, left = 8
				// if right = 8 + 2 * size - 1 = 15, it would be out of bounds
				// so in this case, right can only be the end of the array: n - 1 = 14
			}
		}
	}

	// merge() identical to the recursive approach
	private static void merge(Comparable[] arr, Comparable[] aux,
							  int left, int mid, int right) {
		assert isSorted(arr, left, mid);
		assert isSorted(arr, mid + 1, right);

		for (int j = left; j <= right; j++) aux[j] = arr[j];

		int leftNext = left, rightNext = mid + 1;
		for (int arrNext = left; arrNext <= right; arrNext++) {
			if (leftNext <= mid && rightNext <= right) {
				arr[arrNext] =
						less(aux[leftNext], aux[rightNext]) ?
								aux[leftNext++] :
								aux[rightNext++];
			} else {
				arr[arrNext] = leftNext > mid ?
						aux[rightNext++] :
						aux[leftNext++];
			}
		}
		assert isSorted(arr, left, right);
	}

	private static boolean isSorted(Comparable[] arr, int left, int right) {
		for (int i = left + 1; i <= right; i++)
			// if an element is less than the one to its left:
			if (less(arr[i], arr[i - 1])) return false;
		return true;
	}

	private static boolean less(Comparable v, Comparable w) {
		return v.compareTo(w) < 0;
	}

}
