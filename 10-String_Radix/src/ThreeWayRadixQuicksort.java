public class ThreeWayRadixQuicksort {
	private static void radix3WayQuicksort(String[] strArr) {
		radix3WayQuicksort(strArr, 0, strArr.length - 1, 0);
	}

	private static void radix3WayQuicksort(String[] strArr, int left, int right, int digit) {
		// almost identical to normal 3-way quicksort
		if (right <= left) return;
		int startOfEqual = left, endOfEqual = right;
		int partitionChar = charAt(strArr[left], digit);
		int i = left + 1;
		while (i <= endOfEqual) {
			int dthCharInStrI = charAt(strArr[i], digit);
			if (dthCharInStrI < partitionChar) exchange(strArr, startOfEqual++, i++);
			else if (dthCharInStrI > partitionChar) exchange(strArr, i, endOfEqual--);
			else i++;
		}

		// recursively sort 3 partitioned sub-arrays
		radix3WayQuicksort(strArr, left, startOfEqual - 1, digit);
		if (partitionChar >= 0) radix3WayQuicksort(strArr, startOfEqual, endOfEqual, digit + 1);
		radix3WayQuicksort(strArr, endOfEqual + 1, right, digit);
	}

	private static int charAt(String str, int digit) {
		if (digit < str.length()) return str.charAt(digit);
		else return -1;
	}

	private static void exchange(String[] strArr, int i1, int i2) {
		String temp = strArr[i1];
		strArr[i1] = strArr[i2];
		strArr[i2] = temp;
	}
}