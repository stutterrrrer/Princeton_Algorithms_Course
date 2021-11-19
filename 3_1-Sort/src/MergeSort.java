public class MergeSort {

	private static void merge(Comparable[] arr, Comparable[] auxiliaryArr,
							  int left, int mid, int right) {
		// precondition: both halves are already sorted.
		// left half: sorted from arr[left] to arr[mid].
		assert isSorted(arr, left, mid);
		assert isSorted(arr, mid + 1, right);

		// copy entire array to auxiliary array:
		for (int j = left; j <= right; j++) auxiliaryArr[j] = arr[j];

		// merge left and right in sorted order:
		// i.e. copy the sorted result of auxiliary array to original array:
		int leftNext = left, rightNext = mid + 1;
		for (int arrNext = left; arrNext <= right; arrNext++) {
			// if both halves have elements remaining:
			// compare the first of left remaining with first of right remaining:
			if (leftNext <= mid && rightNext <= right) {
				arr[arrNext] =
						less(auxiliaryArr[leftNext], auxiliaryArr[rightNext]) ?
								auxiliaryArr[leftNext++] :
								auxiliaryArr[rightNext++];
			}

			else {// either the left or the right half has all been copied into original arr:
				arr[arrNext] = leftNext > mid ?
						// if it's the left half:
						// copy the rest of the auxArr's right half into original array as is
						auxiliaryArr[rightNext++] :
						auxiliaryArr[leftNext++];
			}
		}

		// after the merge - original arr should now be sorted from left to right.
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
