import java.util.Arrays;

public class LRSwithSuffixArray {
	public static String LongestRepeatedSubstring(String str) {
		int n = str.length();
		// create suffix array:
		String[] suffixArr = new String[n];
		for (int i = 0; i < n; i++)
			suffixArr[i] = str.substring(i, n);
		// sort the suffix array
		Arrays.sort(suffixArr);

		// find Longest Repeated Substring (between sorted adjacent suffixes)
		String longestRepeatedSubstring = "";
		for (int i = 0; i < n - 1; i++) {
			int longestCommonLength = longestCommonPrefix(suffixArr[i], suffixArr[i + 1]);
			if (longestCommonLength > longestRepeatedSubstring.length())
				longestRepeatedSubstring = suffixArr[i].substring(0, longestCommonLength);
		}
		return longestRepeatedSubstring;
	}

	public static int longestCommonPrefix(String s1, String s2) {
		int n = Math.min(s1.length(), s2.length());
		for (int i = 0; i < n; i++)
			if (s1.charAt(i) != s2.charAt(i))
				return i;
		return n;
	}
}
