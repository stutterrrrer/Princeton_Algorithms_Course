import java.util.LinkedList;
import java.util.Queue;

public class RWayTries {

	private static class Node {
		private boolean isFullWord;
		// implicitly defines character - next[0] = a, next[25] = z.
		// the array is created as soon as the node is created.
		private Node[] next = new Node[RADIX];
	}

	private static final int RADIX = 256; // extended ASCII
	private Node root = new Node(); // the root represents no character (empty)

	public void put(String str) {
		root = recursivePut(root, str, 0);
	}

	private Node recursivePut(Node node, String str, int charIndex) {
		// e.g. when put("as") as first word:

		// 1st call: node = root, charIndex = 0;
		// 2nd call: node = root.next[a], charIndex = 1;
		// 3rd call: node = root.next[a].next[s], charIndex = 2;
		if (node == null) node = new Node();
		if (charIndex == str.length()) {
			// happens in 3rd call, when charIndex = 2.
			// root.next[a].next[s].isFullWord = true.
			// root.next[a].next[s] is a node with an array of 256 null elements.
			node.isFullWord = true;
			return node;
		}

		// 1st call: thisChar = str.charAt(0) = a
		// 2nd call: thisChar = str.charAt(1) = s
		char thisChar = str.charAt(charIndex);
		// 1st call: root.next[a] = recursivePut(root.next[a], "in", 1)
		// 2nd call: root.next[a].next[s] = recursivePut(root.next[a].next[s], "in", 2)
		node.next[thisChar] = recursivePut(node.next[thisChar], str, charIndex + 1);
		return node;
	}

	public boolean contains(String str) {
		Node strNode = findPrefix(str);
		return strNode != null && strNode.isFullWord;
	}

	private Node findPrefix(String prefix) {
		int strLen = prefix.length();
		Node node = root;
		for (int i = 0; i < strLen; i++) {
			// if the next character is not in the dictionary (tries)
			if (node.next[prefix.charAt(i)] == null) return null;
			node = node.next[prefix.charAt(i)];
		}

		return node;
	}

	public Iterable<String> orderedStrings() {
		Queue<String> queue = new LinkedList<>();
		inOrder(root, "", queue);
		return queue;
	}

	public Iterable<String> stringsWithPrefix(String prefix) {
		Queue<String> queue = new LinkedList<>();
		Node prefixNode = findPrefix(prefix);
		inOrder(prefixNode, prefix, queue);
		return queue;
	}

	private void inOrder(Node node, String prefix, Queue<String> queue) {
		// prefix: sequence of characters on the path from root to node.
		if (node == null) return;
		// prefix already includes the character represented by this node from the previous call.
		if (node.isFullWord) queue.add(prefix);
		for (char nextChar = 0; nextChar < RADIX; nextChar++)
			inOrder(node.next[nextChar], prefix + nextChar, queue);
	}

	public String longestPrefixOf(String query) {
		// find a COMPLETE word that is a prefix of the query string.
		int length = searchPrefixOf(query, root, 0, 0);
		return query.substring(0, length);
	}

	private int searchPrefixOf(String query, Node node, int charIndex, int length) {
		if (node == null) return length;
		if (node.isFullWord) length = charIndex; // only count the prefix if it's a complete word
		if (charIndex == query.length()) return length;
		char nextChar = query.charAt(charIndex);
		return searchPrefixOf(query, node.next[nextChar], charIndex + 1, length);
	}

	public static void main(String[] args) {
		RWayTries dict = new RWayTries();
		dict.put("something");
		System.out.println(dict.contains("something"));
		System.out.println(dict.contains("some"));

		dict.put("where?");
		dict.put("somewhere");
		System.out.println("All strings:");
		for (String str : dict.orderedStrings())
			System.out.print(str + ", ");
		System.out.println();
		System.out.println("prefix: some");
		for (String str : dict.stringsWithPrefix("some"))
			System.out.print(str + ", ");

		System.out.println();
		String x = "someone";
		dict.put("some");
		System.out.println("longest prefix of " + x + " is ");
		System.out.println(dict.longestPrefixOf(x));
	}
}
