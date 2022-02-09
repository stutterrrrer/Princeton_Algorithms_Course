public class Tries26 {

    private Node26 root = new Node26();

    public Node26 root() {
        return root;
    }

    public void put(String str) {
        root = recursivePut(root, str, 0);
    }

    public Node26 findPrefix(String prefix) {
        int strLen = prefix.length();
        Node26 node = root;
        for (int i = 0; i < strLen; i++) {
            if (node.getNextNode(prefix.charAt(i)) == null)
                return null;
            node = node.getNextNode(prefix.charAt(i));
        }
        return node;
    }

    public boolean contains(String str) {
        Node26 node = findPrefix(str);
        return node != null && node.isFullWord();
    }

    private Node26 recursivePut(Node26 node, String str, int charIndex) {
        if (node == null) node = new Node26();
        if (charIndex == str.length()) {
            node.setFullWord(true);
            return node;
        }
        char c = str.charAt(charIndex);
        node.setNextAt(c, recursivePut(node.getNextNode(c), str, charIndex + 1));
        return node;
    }

    public static void main(String[] args) {
        Tries26 tries26 = new Tries26();
        tries26.put("THIS");
        System.out.println(tries26.contains("THIS"));
        System.out.println(tries26.contains("THS"));
    }
}
