import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class BurrowsWheeler {

    // encode; read from standard input, write to standard output
    public static void transform() {
        String originalStr = BinaryStdIn.readString();
        int strLen = originalStr.length();
        CircularSuffixArray sortedCircularSuffixes = new CircularSuffixArray(originalStr);

        char[] tail = new char[strLen];
        int indexOfOriginalStr = -1;
        // build tail[] array & find index of original str
        for (int i = 0; i < strLen; i++) {
            final int indexInOriginalSuffixArr = sortedCircularSuffixes.index(i);
            int charIndex = indexInOriginalSuffixArr == 0 ? strLen - 1 :
                            indexInOriginalSuffixArr - 1;
            tail[i] = originalStr.charAt(charIndex);
            if (indexInOriginalSuffixArr == 0)
                indexOfOriginalStr = i;
        }

        // print output:
        BinaryStdOut.write(indexOfOriginalStr);
        for (char tailChar : tail) BinaryStdOut.write(tailChar);
        BinaryStdOut.close();
    }

    // decode; read from standard input, write to standard output
    public static void inverseTransform() {
        int indexOfOriginalStr = BinaryStdIn.readInt();
        String tailStr = BinaryStdIn.readString();
        char[] tail = tailStr.toCharArray();
        int strLen = tail.length;

        char[] head = Arrays.copyOf(tail, strLen);
        Arrays.sort(head);

        // convert the tail array into hashmap:
        // FIFO queue: add to and remove from nextSuffix[] in order.
        Map<Character, Queue<Integer>> tailMap = new HashMap<>();
        for (int i = 0; i < strLen; i++) {
            char curChar = tail[i];
            tailMap.putIfAbsent(curChar, new LinkedList<>());
            tailMap.get(curChar).add(i);
        }

        // construct the nextSuffix[] array:
        int[] nextSuffix = new int[strLen];
        for (int i = 0; i < strLen;) {
            char curChar = head[i];
            Queue<Integer> suffixesEndingWithCurChar = tailMap.get(curChar);
            // size of the queue is the number of consecutive curChar in head[].
            int firstIndexAfterCurChar = i + suffixesEndingWithCurChar.size();
            for (; i < firstIndexAfterCurChar; i++)
                nextSuffix[i] = suffixesEndingWithCurChar.poll();
        }

        // decode (and print out) into original string:
        int currentIndex = indexOfOriginalStr;
        for (int i = 0; i < strLen; i++) {
            BinaryStdOut.write(head[currentIndex]);
            currentIndex = nextSuffix[currentIndex];
        }
        BinaryStdOut.close();
    }

    // args[0] -> encode or decode
    public static void main(String[] args) {
        String operation = args[0];
        if (operation.equals("-")) transform();
        else if (operation.equals("+")) inverseTransform();
    }
}
