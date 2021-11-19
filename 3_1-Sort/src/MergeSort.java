public class MergeSort {

	public static void mergeSort(Comparable[] arr) {
		int n = arr.length;
		// make sure to create only 1 auxiliary array in this public method
		// otherwise if the auxiliary array is created in the sort() method,
			// since it's recursive, #of auxArr created would be = #of times sort() is called
		Comparable[] aux = new Comparable[n];
		sort(arr, aux, 0, n - 1);
	}

	// recursive sort - sorts left, then sorts right, then merge 2 sorted halves.
	private static void sort(Comparable[] arr, Comparable[] aux, int left, int right) {
		// base case:
		// when the arr segment we are trying to sort is only 1 element -> already "sorted".
		if (right <= left) return;

		int mid = left + (right - left) / 2;
		sort(arr, aux, left, mid);
		sort(arr, aux, mid + 1, right);

		// improvement - don't need to merge() 2 halves if
		// last element of left half is already <= 1st element of right half:
		if (!less(arr[mid+1], arr[mid])) return;

		merge(arr, aux, left, mid, right);
	}

	// merge - the method that does the heavy lifting.
	private static void merge(Comparable[] arr, Comparable[] aux,
							  int left, int mid, int right) {
		// precondition: both halves are already sorted.
		// left half: sorted from arr[left] to arr[mid].
		assert isSorted(arr, left, mid);
		assert isSorted(arr, mid + 1, right);

		// copy entire array to auxiliary array:
		for (int j = left; j <= right; j++) aux[j] = arr[j];

		// merge left and right in sorted order:
		// i.e. copy the sorted result of auxiliary array to original array:
		int leftNext = left, rightNext = mid + 1;
		for (int arrNext = left; arrNext <= right; arrNext++) {
			// if both halves have elements remaining:
			// compare the first of left remaining with first of right remaining:
			if (leftNext <= mid && rightNext <= right) {
				arr[arrNext] =
						less(aux[leftNext], aux[rightNext]) ?
								aux[leftNext++] :
								aux[rightNext++];
			}

			else { // either the left or the right half has all been copied into original arr:
				arr[arrNext] = leftNext > mid ?
						// if it's the left half:
						// copy the rest of the auxArr's right half into original array as is
						aux[rightNext++] :
						aux[leftNext++];
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

	public static void main(String[] args) {
		Integer[] arr = {3, 5, 1, 4, 0, -5, -6, -12, 23, -50};
		MergeSort.mergeSort(arr);
		for (int i : arr)
			System.out.print(i + ", ");
	}
}
