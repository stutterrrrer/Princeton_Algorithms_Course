public class QuickSort {

	public static void quickSort(Comparable[] arr) {
		StdRandom.shuffle(arr);
//		recursivePartition(arr, 0, arr.length - 1);
		threeWaySort(arr, 0, arr.length - 1);
	}

	public static Comparable quickSelect(Comparable[] arr, int k) {
		StdRandom.shuffle(arr);
		k--; // array 0-index: in a sorted array arr[k-1] would be the kth smallest element
		int left = 0, right = arr.length - 1;
		while (left < right) {
			int partitionIndex = partition(arr, left, right);
			if (partitionIndex < k) left = partitionIndex + 1;
			else if (partitionIndex > k) right = partitionIndex - 1;
			else return arr[k];
		}
		// when left >= right, the array element(s) between [right, left] is(are) equal,
		// and k is within [right, left] -- I think.
		return arr[k];
	}

	private static void recursivePartition(Comparable[] arr, int left, int right) {
		if (left >= right) return;
		int partitionIndex = partition(arr, left, right);
		recursivePartition(arr, left, partitionIndex - 1);
		recursivePartition(arr, partitionIndex + 1, right);
	}

	private static void threeWaySort(Comparable[] arr, int left, int right) {
		if (left >= right) return;
		int startOfEqual = left, endOfEqual = right;
		Comparable partitionElement = arr[left];
		int i = left;

		while (i <= endOfEqual) {
			int cmp = arr[i].compareTo(partitionElement);
			// if arr[i] is smaller than partition,
			// increment both startOfEqual & i after exchanging them.
			if (cmp < 0) exchange(arr, startOfEqual++, i++);
			else if (cmp > 0) exchange(arr, i, endOfEqual--);
			else i++;
		}

		threeWaySort(arr, left, startOfEqual - 1);
		threeWaySort(arr, endOfEqual + 1, right);
	}

	private static int partition(Comparable[] arr, int left, int right) {
		int i = left, j = right + 1;
		// since array is shuffled -
		// randomly choose the 1st element of subarray arr[left] as partition.
		while (true) {
			// keep incrementing i till arr[i] > partition
			while (less(arr[++i], arr[left]))
				if (i == right) break;
			// keep decrementing j till arr[j] < partition
			while (less(arr[left], arr[--j]))
				if (j == left) break;

			// when arr[i] > partition && arr[j] < partition,
			// if i & j cross: 2 partitions are complete.
			if (i >= j) break;
			// if they hadn't crossed, swap the elements
			// so elements on the left are all < partition, right > partition.
			exchange(arr, i, j);
		}
		// move the partition element from beginning to its rightful position
		exchange(arr, left, j);
		return j; // return position of the partition.
	}

	private static boolean less(Comparable v, Comparable w) {
		return v.compareTo(w) < 0;
	}

	private static void exchange(Comparable[] arr, int x, int y) {
		Comparable temp = arr[x];
		arr[x] = arr[y];
		arr[y] = temp;
	}

	public static void main(String[] args) {
		Integer[] arr = {2, 5, 5, 6, 1, -6, 20, -5};
		quickSort(arr);
		for (int i : arr) System.out.print(i + ", ");
		System.out.println();
		int k = 4;
		System.out.println("the " + k + "th smallest element is " + quickSelect(arr, k));
	}
}
