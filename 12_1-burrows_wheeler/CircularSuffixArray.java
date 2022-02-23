import edu.princeton.cs.algs4.LSD;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class CircularSuffixArray {

    private final int strLength;
    private int[] sortedArrOriginalIndex;

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        // takes advantage of String's subString method - saves space
        // compared to if each string in the circular suffix array has
        // a different underlying char array.
        strLength = s.length();
        sortedArrOriginalIndex = new int[strLength];
        buildCircularSuffixArray(s);
    }

    private void buildCircularSuffixArray(String s) {
        String strRepeated = s + s;
        String[] circularSuffixArray = new String[strLength];
        // Queue: in case of identical suffixes
        HashMap<String, Queue<Integer>> strIndexInOriginalArr = new HashMap<>(strLength);
        // build the original suffix array
        for (int i = 0; i < strLength; i++) {
            final String circularSuffix = strRepeated.substring(i, i + strLength);
            circularSuffixArray[i] = circularSuffix;
            strIndexInOriginalArr.putIfAbsent(circularSuffix, new LinkedList<>());
            strIndexInOriginalArr.get(circularSuffix).add(i);
        }

        // LSD radix sort since all strings have the same length:
        LSD.sort(circularSuffixArray, strLength);
        // build the original index array
        for (int i = 0; i < strLength; i++)
            sortedArrOriginalIndex[i] = strIndexInOriginalArr.get(circularSuffixArray[i]).poll();
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
