import edu.princeton.cs.algs4.Quick3string;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CircularSuffixArray {

    private String strRepeated;
    private final int strLength;
    private int[] sortedArrOriginalIndex;

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        // takes advantage of String's subString method - saves space
        // compared to if each string in the circular suffix array has
        // a different underlying char array.
        strRepeated = s + s;
        strLength = s.length();
        sortedArrOriginalIndex = new int[strLength];
        buildCircularSuffixArray();
    }

    private void buildCircularSuffixArray() {
        String[] circularSuffixArray = new String[strLength];
        // HashMap<String,Integer> : string as key, string's index in the original array as value.
        Map<String, Integer> strIndexInOriginalArr = new HashMap<>(strLength);
        // build the original suffix array
        for (int i = 0; i < strLength; i++) {
            final String circularSuffix = strRepeated.substring(i, i + strLength);
            circularSuffixArray[i] = circularSuffix;
            strIndexInOriginalArr.put(circularSuffix, i);
        }

        // 3-way radix quick sort:
        Quick3string.sort(circularSuffixArray);
        // build the original index array
        for (int i = 0; i < strLength; i++)
            sortedArrOriginalIndex[i] = strIndexInOriginalArr.get(circularSuffixArray[i]);
    }

    public int length() {
        return strLength;
    }

    public int index(int i) {
        if (i < 0 || i >= strLength) throw new IllegalArgumentException();
        return sortedArrOriginalIndex[i];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // assume input is only 1 line (1 string)
        String str = scanner.nextLine();
        CircularSuffixArray csa = new CircularSuffixArray(str);
        final int len = csa.length();
        System.out.println("length: " + len);
        for (int i = 0; i < len; i++)
            System.out.println("sorted index: " + i + "; original index: " + csa.index(i));
    }
}
