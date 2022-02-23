import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int RADIX = 256; // extended ASCII as alphabet
    private static final int CHAR_BITS = 8; // as normal
    private static final int INT_BITS = 8;  // down from 4 bytes = 32 bits

    public static void encode() {
        char[] encodingSequence = new char[RADIX];
        for (int i = 0; i < RADIX; i++)
            encodingSequence[i] = (char) i;

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(CHAR_BITS);
            BinaryStdOut.write(findCharAndMoveToFront(c, encodingSequence), INT_BITS);
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        char[] decodingSequence = new char[RADIX];
        for (int i = 0; i < RADIX; i++)
            decodingSequence[i] = (char) i;

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar(CHAR_BITS);
            BinaryStdOut.write(accessIndexAndMoveToFront(index, decodingSequence));
        }
        BinaryStdOut.close();
    }

    private static char accessIndexAndMoveToFront(int index, char[] sequence) {
        char c = sequence[index];
        for (int i = index; i >= 1; i--)
            sequence[i] = sequence[i - 1];
        sequence[0] = c;

        return c;
    }

    private static int findCharAndMoveToFront(char c, char[] sequence) {
        int charIndex = 0;
        for (int i = 0; i < sequence.length; i++)
            if (sequence[i] == c) {
                charIndex = i;
                break;
            }

        for (int i = charIndex; i >= 1; i--)
            sequence[i] = sequence[i - 1];
        sequence[0] = c;
        return charIndex;
    }

    public static void main(String[] args) {
        // if args[0] is "-", apply move-to-front encoding
        // if args[0] is "+", apply move-to-front decoding
        String mode = args[0];
        if (mode.equals("-")) encode();
        else if (mode.equals("+")) decode();
    }
}
