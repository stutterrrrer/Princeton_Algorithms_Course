import java.util.LinkedList;
import java.util.Queue;

// no duplicates; override previous value of a key
public class BST<Key extends Comparable<Key>, Value> {

	private Node treeRoot;

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
		treeRoot = insertNodeOrOverrideVal(treeRoot, key, val);
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
		Node visitedNode = treeRoot;
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
		Node floorNode = floorNode(treeRoot, keyAboveFloor);
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
		return rankInSubTree(rankedKey, treeRoot);
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
		return selectFromSubTree(treeRoot, rank).key;
	}

	private Node selectFromSubTree(Node subTreeRoot, int rank) {
		if (subTreeRoot == null) return null;
		int leftSubTreeSize = subTreeSize(subTreeRoot.left);
		if (leftSubTreeSize > rank)
			return selectFromSubTree(subTreeRoot.left, rank);
		else if (leftSubTreeSize < rank)
			// if the selected node is in the right sub tree
			// then the selected node's rank in the whole tree =
			// (left sub-tree size) + (this node, 1) + !!!(its rank in the right sub-tree)
			return selectFromSubTree(subTreeRoot.right, rank - leftSubTreeSize - 1);
		else
			return subTreeRoot;
	}

	public Iterable<Key> keys() {
		return keysInRange(min(treeRoot).key, max(treeRoot).key);
	}

	public Iterable<Key> keysInRange(Key low, Key high) {
		Queue<Key> orderedKeysInRange = new LinkedList<>();
		rangeSearch(treeRoot, orderedKeysInRange, low, high);
		return orderedKeysInRange;
	}

	public void rangeSearch(Node root, Queue<Key> orderedKeysInRange,
							Key low, Key high) {
		if (root == null) return;
		int cmpLow = low.compareTo(root.key);
		int cmpHigh = high.compareTo(root.key);
		// does a variation of in-order traversal that's capped and floored:
		if (cmpLow < 0)
			rangeSearch(root.left, orderedKeysInRange, low, high);
		if (cmpLow <= 0 && cmpHigh >= 0)
			orderedKeysInRange.add(root.key);
		if (cmpHigh > 0)
			rangeSearch(root.right, orderedKeysInRange, low, high);
	}

	public Value max() {
		return max(treeRoot).val;
	}

	private Node max(Node root) {
		Node visited;
		for (visited = root; visited.right != null; visited = visited.right) ;
		return visited;
	}

	public Value min() {
		return min(treeRoot).val;
	}

	private Node min(Node root) {
		Node visited;
		for (visited = root; visited.left != null; visited = visited.left) ;
		return visited;
	}

	public int size() {
		return subTreeSize(treeRoot);
	}

	private int subTreeSize(Node node) {
		// allows null parameter, as opposed to the instance variable
		return node == null ? 0 : node.subTreeSize;
	}

}
