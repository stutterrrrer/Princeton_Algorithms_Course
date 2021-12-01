public class MaxPriorityQueue<Key extends Comparable<Key>> {

	private Key[] heap;
	private int n;

	// can be improved with a flexible array - no need to provide capacity as parameter.
	public MaxPriorityQueue(int capacity) {
		// heap[0] is empty
		heap = (Key[]) new Comparable[capacity + 1];
		n = 0;
	}

	public void insert(Key inserted) {
		// first insertion is at heap[1].
		heap[++n] = inserted;
		promote(n);
	}

	private void promote(int keyIndex) {
		int parentIndex = keyIndex / 2;
		// if parentIndex == 0, that means keyIndex == 1.
		// though heap[0] == null, the while condition will return false before any iteration.

		while (keyIndex > 1 &&
				less(parentIndex, keyIndex)) {
			exchange(keyIndex, parentIndex);
			keyIndex = parentIndex;
			parentIndex = keyIndex / 2;
		}
	}

	public Key deleteMax() {
		Key max = heap[1];
		exchange(1, n--);
		demote(1);
		heap[n + 1] = null; // remove & prevent loitering.
		return max;
	}

	private void demote(int keyIndex) {
		// left = key * 2; right = left + 1; right <= n;
		// if the last element is a left child (i.e. empty right), then left <= n;

		while (keyIndex * 2 <= n) { // while this key has at least 1 child
			int leftChildIndex = keyIndex * 2;
			int rightChildIndex = leftChildIndex + 1;
			int indexOfBiggerChild = rightChildIndex <= n // this key may have no right child.
					&& less(leftChildIndex, rightChildIndex) ?
					rightChildIndex : leftChildIndex;

			if (!less(keyIndex, indexOfBiggerChild)) break;
			exchange(keyIndex, indexOfBiggerChild);
			keyIndex = indexOfBiggerChild;
		}
	}

	private boolean less(int heapIndex1, int heapIndex2) {
		Key v = heap[heapIndex1];
		Key w = heap[heapIndex2];
		return v.compareTo(w) < 0;
	}

	private void exchange(int x, int y) {
		Key temp = heap[x];
		heap[x] = heap[y];
		heap[y] = temp;
	}
}
