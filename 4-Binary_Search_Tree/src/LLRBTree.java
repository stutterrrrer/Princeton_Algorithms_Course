public class LLRBTree<Key extends Comparable<Key>, Value> {

	private static final boolean RED = true;
	private static final boolean BLACK = false;
	private Node treeRoot;

	private class Node {
		private Key key;
		private Value val;
		private Node left, right;
		private int subTreeSize;
		boolean color; // color of parent's link

		public Node(Key key, Value val, int subTreeSize, boolean color) {
			this.key = key;
			this.val = val;
			this.subTreeSize = subTreeSize;
			// though every new node should be automatically colored red
			this.color = color;
		}
	}

	public void put(Key key, Value val) {
		treeRoot = put(treeRoot, key, val);
		treeRoot.color = BLACK;
	}

	private Node put(Node visited, Key key, Value val) {
		if (visited == null) // insert the key into node
			return new Node(key, val, 1, RED);

		int cmp = key.compareTo(visited.key);
		if (cmp < 0) visited.left = put(visited.left, key, val);
		else if (cmp > 0) visited.right = put(visited.right, key, val);
		else visited.val = val;

		// fixUp() call is the only part different from BST insertion
		visited = fixUp(visited);

		visited.subTreeSize =
				size(visited.left) + size(visited.right) + 1;
		return visited;
	}

	public void deleteMin() {
		treeRoot = deleteMin(treeRoot);
		treeRoot.color = BLACK;
	}

	private Node deleteMin(Node node) {
		// delete this node as it's the min
		if (node.left == null) return null;

		// if the next left child to visit is a 1-key-node:
		if (!isRed(node.left) && !isRed(node.left.left))
			node = growLeftChild(node);

		// recursion - keep going to the left:
		node.left = deleteMin(node.left);

		// after deleting the min, on the way back up:
		node = fixUp(node);
		node.subTreeSize = size(node.left) + size(node.right) + 1;
		return node;
	}

	private Node growLeftChild(Node root) {
		// root's color would always be red
		// since we ensure each node we visit has at least 2 keys.

		// if left child's immediate sibling has only 1 key:
		// flipping root's color is equivalent to
		// combining 1.root's smallest key + 2.immediate sibling + 3.left child
		// into a 4-key node
		flipColors(root);

		// but if left child's immediate sibling has 2 keys:
		if (isRed(root.right.left)) {
			// by flipping color we've created a 4-key-node, so:
			root.right = rotateRight(root.right);
			root = rotateLeft(root);
			flipColors(root);
		}
		return root;
	}

	public void delete(Key key) {
		treeRoot = delete(treeRoot, key);
		treeRoot.color = BLACK;
	}

	private Node delete(Node root, Key key) {
		// go to the left
		if (key.compareTo(root.key) < 0) {
			if (!isRed(root.left) && !isRed(root.left.left))
				root = growLeftChild(root);
			root.left = delete(root.left, key);
		} else { // either compareTo > or = 0:
			if (isRed(root.left))
				root = rotateRight(root);
			// when the key to be deleted is in this node,
			// and this node is a leaf node
			if (key.compareTo(root.key) == 0 && (root.right == null))
				return null;
			// when the key to be deleted is bigger than this node's key:
			if (!isRed(root.right) && !isRed(root.right.left))
				root = growRightChild(root);
			// when the key to be deleted is in this node,
			// but this node still has children (not leaf node)
			if (key.compareTo(root.key) == 0) {
				// find a substitute: the smallest key in the right sub-tree
				root.val = get(root.right, min(root.right).key);
				root.key = min(root.right).key;
				root.right = deleteMin(root.right);
			} else root.right = delete(root.right, key);
		}

		fixUp(root);
		root.subTreeSize = size(root.left) + size(root.right) + 1;
		return root;
	}

	private Node growRightChild(Node root) {
		flipColors(root);
		if (isRed(root.left.left)) {
			root = rotateRight(root);
			flipColors(root);
		}
		return root;
	}

	public void deleteMax() {
		treeRoot = deleteMax(treeRoot);
		treeRoot.color = BLACK;
	}

	private Node deleteMax(Node root) {
		if (isRed(root.left))
			root = rotateRight(root);

		if (root.right == null)
			return null;

		if (!isRed(root.right) && !isRed(root.right.left))
			root = growRightChild(root);

		root.left = deleteMax(root.left);

		return fixUp(root);
	}

	private Node fixUp(Node node) {
		if (isRed(node.right) && !isRed(node.left))
			node = rotateLeft(node); // make red link lean left
		if (isRed(node.left) && isRed(node.left.left))
			node = rotateRight(node); // balance the 3-key-node
		if (isRed(node.left) && isRed(node.right))
			// (in insertion) when all illegal states are reduced to
			// the same balanced 3-key-node, where the middle key is root:
			flipColors(node);

		return node;
	}

	private boolean isRed(Node node) {
		if (node == null) return false;
		return node.color == RED;
	}

	private Node rotateLeft(Node oldRoot) {
		// rotate a (temporarily) right-leaning red link to lean left
		assert isRed(oldRoot.right);

		Node newRoot = oldRoot.right;
		oldRoot.right = newRoot.left;
		newRoot.left = oldRoot;

		newRoot.color = oldRoot.color;
		oldRoot.color = RED;

		return newRoot;
	}

	private Node rotateRight(Node oldRoot) {
		// rotate a left-leaning red link to (temporarily) lean right
		assert isRed(oldRoot.left);

		Node newRoot = oldRoot.left;
		oldRoot.left = newRoot.right;
		newRoot.right = oldRoot;

		newRoot.color = oldRoot.color;
		oldRoot.color = RED;

		return newRoot;
	}

	private void flipColors(Node root) {
		root.color = !root.color;
		root.left.color = !root.left.color;
		root.right.color = !root.right.color;
	}

	public int size() {
		return size(treeRoot);
	}

	private int size(Node node) {
		// allows null parameter, as opposed to the instance variable
		return node == null ? 0 : node.subTreeSize;
	}

	public Value get(Key key) {
		return get(treeRoot, key);
	}

	private Value get(Node root, Key key) {
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

	public Value min() {
		return min(treeRoot).val;
	}

	private Node min(Node root) {
		Node visited;
		for (visited = root; visited.left != null; visited = visited.left) ;
		return visited;
	}

}
