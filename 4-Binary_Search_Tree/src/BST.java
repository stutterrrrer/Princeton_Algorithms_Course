import java.util.LinkedList;
import java.util.Queue;

// no duplicates; override previous value of a key
public class BST<Key extends Comparable<Key>, Value> {

	private Node root;

	private class Node {
		private Key key;
		private Value val;
		private Node left, right;
		private int subTreeSize;

		public Node(Key key, Value val, int subTreeSize) {
			this.key = key;
			this.val = val;
			this.subTreeSize = subTreeSize;
		}
	}

	public void put(Key key, Value val) {
		root = insertNodeOrOverrideVal(root, key, val);
	}

	private Node insertNodeOrOverrideVal(Node visitedNode, Key key, Value val) {
		// returns either the visited node,
		// or (eventually) the inserted or overridden node
		if (visitedNode == null) return new Node(key, val, 1);

		int cmp = key.compareTo(visitedNode.key);
		if (cmp < 0)
			visitedNode.left = insertNodeOrOverrideVal(visitedNode.left, key, val);
		else if (cmp > 0)
			visitedNode.right = insertNodeOrOverrideVal(visitedNode.left, key, val);
		else
			// if cmp == 0, this visited node has the same key as the node to be put:
			// override the existing value previously at this key.
			visitedNode.val = val;

		// because if the node was inserted (instead of overriding an existing node's value)
		// then one of this visited node's children will increase in size.
		visitedNode.subTreeSize = 1 + subTreeSize(visitedNode.left) + subTreeSize(visitedNode.right);
		return visitedNode;
	}

	public Value get(Key key) {
		Node visitedNode = root;
		while (visitedNode != null) {
			int cmp = key.compareTo(visitedNode.key);
			if (cmp < 0) visitedNode = visitedNode.left;
			else if (cmp > 0) visitedNode = visitedNode.right;
			else return visitedNode.val;
		}
		// if the key doesn't exist
		return null;
	}

	public Key floor(Key keyAboveFloor) {
		Node floorNode = floorNode(root, keyAboveFloor);
		if (floorNode == null) return null;
		return floorNode.key;
	}

	private Node floorNode(Node visited, Key keyAboveFloor) {
		if (visited == null) return null;

		int cmp = visited.key.compareTo(keyAboveFloor);
		if (cmp == 0) return visited;
		if (cmp > 0) return floorNode(visited.left, keyAboveFloor);
		// else, cmp < 0: visited node's key is smaller than parameter key:
		// the floorNode may be this visited node or in its right sub-tree.
		Node floorNodeInRightSubTree = floorNode(visited.right, keyAboveFloor);
		if (floorNodeInRightSubTree == null) return visited;
		else return floorNodeInRightSubTree;
	}

	public int rank(Key rankedKey) {
		return rankInSubTree(rankedKey, root);
	}

	private int rankInSubTree(Key rankedKey, Node subTreeRoot) {
		if (subTreeRoot == null) return 0;
		int cmp = rankedKey.compareTo(subTreeRoot.key);
		// if ranked key is bigger than the subTreeRoot key,
		// then the rank is (left sub-tree's size) + (this node: +1) + (its rank in right sub-tree)
		if (cmp > 0)
			return subTreeSize(subTreeRoot.left) + 1 + rankInSubTree(rankedKey, subTreeRoot.right);
		if (cmp < 0)
			return rankInSubTree(rankedKey, subTreeRoot.left);
		else // if ranked key = subTreeRoot's key
			return subTreeSize(subTreeRoot.left);
	}

	public Key select(int rank) {
		if (rank < 0 || rank >= subTreeSize(root)) throw new IllegalArgumentException();
		Node visited = root;
		// since rank is valid, visited.left or visited.right
		// in this loop will never be null.
		while (true) {
			int rankOfVisited = rank(visited.key);
			if (rank < rankOfVisited) visited = visited.left;
			else if (rank > rankOfVisited) visited = visited.right;
			else return visited.key;
		}
	}

	public Iterable<Key> keys() {
		Queue<Key> queue = new LinkedList<>();
		inOrder(root, queue);
		return queue;
	}

	private void inOrder(Node visited, Queue<Key> queue) {
		if (visited == null) return;
		inOrder(visited.left, queue);
		queue.add(visited.key);
		inOrder(visited.right, queue);
	}

	public Value max() {
		Node visited;
		for (visited = root; visited.right != null; visited = visited.right) ;
		return visited.val;
	}

	public Value min() {
		Node visited;
		for (visited = root; visited.left != null; visited = visited.left) ;
		return visited.val;
	}

	public int size() {
		return subTreeSize(root);
	}

	private int subTreeSize(Node node) {
		// allows null parameter, as opposed to the instance variable
		return node == null ? 0 : node.subTreeSize;
	}

}
