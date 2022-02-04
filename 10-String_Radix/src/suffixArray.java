public class suffixArray {
	public static String[] suffixArr(String str) {
		int n = str.length();
		String[] suffixArr = new String[n];
		for (int i = 0; i < n; i++)
			suffixArr[i] = str.substring(i, n);
		return suffixArr;
	}
}
