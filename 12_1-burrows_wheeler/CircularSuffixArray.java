import java.util.Scanner;

public class CircularSuffixArray {

    private final int strLength;
    private int[] sortedArrOriginalIndex;

    private class SuffixWithOriginalIndex {
        private String str;
        private int originalIndex;

        public SuffixWithOriginalIndex(String str, int originalIndex) {
            this.str = str;
            this.originalIndex = originalIndex;
        }
    }

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
        SuffixWithOriginalIndex[] circularSuffixArray = new SuffixWithOriginalIndex[strLength];
        // build the original suffix array
        for (int i = 0; i < strLength; i++) {
            final String circularSuffix = strRepeated.substring(i, i + strLength);
            circularSuffixArray[i] = new SuffixWithOriginalIndex(circularSuffix, i);
        }

        // LSD radix sort since all strings have the same length:
        radixLSDOnCustomType(circularSuffixArray, strLength);
        // build the original index array
        for (int i = 0; i < strLength; i++)
            sortedArrOriginalIndex[i] = circularSuffixArray[i].originalIndex;
    }

    private void radixLSDOnCustomType(SuffixWithOriginalIndex[] circularSuffixArray,
                                      int strLen) {
        int radix = 256; // default: extended ASCII alphabet
        int n = circularSuffixArray.length; // actually just equal to strLen;
        SuffixWithOriginalIndex[] aux = new SuffixWithOriginalIndex[n];

        // from right (LSD) to left, do key-indexed counting for each char
        for (int d = strLen - 1; d >= 0; d--) {
            int[] frequencies = new int[radix + 1];
            for (SuffixWithOriginalIndex suffix : circularSuffixArray)
                frequencies[suffix.str.charAt(d) + 1]++; // the 'd'th char

            for (int r = 0; r < radix; r++)
                frequencies[r + 1] += frequencies[r];
            int[] indexForNext = frequencies;

            for (SuffixWithOriginalIndex suffix : circularSuffixArray)
                aux[indexForNext[suffix.str.charAt(d)]++] = suffix;

            System.arraycopy(aux, 0, circularSuffixArray, 0, n);
        }
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
