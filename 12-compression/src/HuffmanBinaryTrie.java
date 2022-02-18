import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.PriorityQueue;

public class HuffmanBinaryTrie {

	private static class Node implements Comparable<Node> {
		private final char leafNodeChar;
		private final int frequency;
		private final Node left, right;

		public Node(char leafNodeChar, int frequency, Node left, Node right) {
			this.leafNodeChar = leafNodeChar;
			this.frequency = frequency;
			this.left = left;
			this.right = right;
		}

		public boolean isLeaf() {
			return left == null && right == null;
		}

		public int compareTo(Node that) {
			return this.frequency - that.frequency;
		}
	}

	private static Node HuffmanBuildTrie(int[] freq) {
		// freq is an int array of size = radix, which is ASCII 256 by default
		int radix = freq.length;
		// don't give radix as the initial capacity,
		// because it's likely most chars in the (ASCII) alphabet didn't appear
		PriorityQueue<Node> minFreqSubTriesPQ = new PriorityQueue<>();
		for (char i = 0; i < radix; i++)
			if (freq[i] > 0)
				minFreqSubTriesPQ.add(new Node(i, freq[i], null, null));

		while (minFreqSubTriesPQ.size() > 1) {
			// take the 2 least frequent nodes off,
			// create a parent(merged trie) for them and put the parent on, until there's only 1 trie
			Node x = minFreqSubTriesPQ.poll(); // least frequent
			Node y = minFreqSubTriesPQ.poll(); // second least frequent
			Node parentMergedTrie = new Node('\0', x.frequency + y.frequency, x, y);
			minFreqSubTriesPQ.add(parentMergedTrie);
		}

		return minFreqSubTriesPQ.poll();
	}

	private static void representTrieAsBitStream(Node node) {
		// pre-order traversal:
		// 1. root first
		if (node.isLeaf()) {
			// output a "1" to indicate this is a leaf node
			BinaryStdOut.write(true);
			// then output the ASCII bits representation of the character following the "1"
			BinaryStdOut.write(node.leafNodeChar, 8);
			return;
		}
		// output a "0" to indicate an internal node
		BinaryStdOut.write(false);
		// 2. children second (left to right)
		representTrieAsBitStream(node.left);
		representTrieAsBitStream(node.right);
	}

	private static Node reconstructTrieFromBitStream() {
		// preorder:
		// 1. start reading the root:
		if (BinaryStdIn.readBoolean()) {
			// if read a bit that indicates a leaf node:
			// ASCII: 8 bits to represent a char
			char character = BinaryStdIn.readChar(8);
			return new Node(character, 0, null, null);
		}
		// 2. then, if it's not a leaf node,
		// read the 2 children:
		Node leftChild = reconstructTrieFromBitStream();
		Node rightChild = reconstructTrieFromBitStream();
		// when it's not a leaf node, there's no leafNodeChar -
		// so put the '\0', which ASCII translates as null character
		return new Node('\0', 0, leftChild, rightChild);
	}

	public void expand() {
		Node root = reconstructTrieFromBitStream();
		int charCount = BinaryStdIn.readInt();

		for (int i = 0; i < charCount; i++) {
			// expand codeword (decrypt) into the ith character.
			Node node = root;
			while (!root.isLeaf()) {
				// read the next bit: if 0, go left, if 1, go right
				if (!BinaryStdIn.readBoolean())
					node = node.left;
				else
					node = node.right;
			}
			// when the above while loop terminates: reaches leaf node (complete codeword for a character):
			// print out the character (ASCII)
			BinaryStdOut.write(node.leafNodeChar, 8);
		}
	}
}
