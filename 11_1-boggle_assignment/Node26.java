public class Node26 {
    private static final int RADIX = 26; // 26 upper case letters.
    private boolean isFullWord;
    private Node26[] next = new Node26[RADIX];

    public Node26 getNextNode(char c) {
        return next[position(c)];
    }

    public void setNextAt(char c, Node26 node) {
        this.next[position(c)] = node;
    }

    public boolean isFullWord() {
        return isFullWord;
    }

    public void setFullWord(boolean fullWord) {
        isFullWord = fullWord;
    }

    private int position(char c) {
        return c - 'A';
    }

}
