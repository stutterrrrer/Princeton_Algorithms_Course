import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    // use bool for dividing axis: save space since it's an instance variable
    private static final boolean VERTICAL = false;
    private static final boolean HORIZONTAL = true;
    // use ENUM for left / right of parent: local variable, won't take space.
    private enum ChildDirection {
        LEFT, RIGHT
    }
    // each node corresponds to a rectangle & a dividing axis
    private class Node2D {
        private final Point2D pt;
        private final boolean divAxis;
        private final RectHV rect;
        private Node2D left;
        private Node2D right;

        public Node2D(Point2D pt, Node2D parent, ChildDirection direction) {
            this.pt = pt;
            if (parent == null) {
                this.divAxis = VERTICAL;
                this.rect = new RectHV(0, 0, 1, 1);
                return;
            }
            this.divAxis = !parent.divAxis;

            double xMin = parent.rect.xmin(), xMax = parent.rect.xmax(),
                    yMin = parent.rect.ymin(), yMax = parent.rect.ymax();
            if (parent.divAxis == VERTICAL)
                if (direction == ChildDirection.LEFT)
                    xMax = parent.pt.x();
                else xMin = parent.pt.x();
            else // if parent node divides horizontally
                if (direction == ChildDirection.LEFT)
                    yMax = parent.pt.y();
                else yMin = parent.pt.y();

            this.rect = new RectHV(xMin, yMin, xMax, yMax);
        }
    }

    private Node2D treeRoot;
    private int size;

    public KdTree() {
        treeRoot = null;
    }

    public boolean isEmpty() {
        return treeRoot == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D pt) {
        if (treeRoot == null) {
            treeRoot = new Node2D(pt, null, null);
            size++;
            return;
        }

        // recursion has the recursive method as the last line: use loop
        Node2D root = treeRoot;
        while (true) {
            // simplification: no 2 points are on the same orthogonal line
            final int cmp = ptCmpToNode(pt, root);
            if (cmp < 0)
                if (root.left == null) {
                    root.left = new Node2D(pt, root, ChildDirection.LEFT);
                    break;
                }
                else root = root.left;
            else // if pt goes on the right sub-tree:
                if (root.right == null) {
                    root.right = new Node2D(pt, root, ChildDirection.RIGHT);
                    break;
                }
                else root = root.right;
        }
        size++;
    }

    public boolean contains(Point2D pt) {
        for (Node2D root = treeRoot; root != null;) {
            Point2D rootPt = root.pt;
            if (rootPt.equals(pt)) return true;

            int cmp = ptCmpToNode(pt, root);
            if (cmp < 0) root = root.left;
            else root = root.right;
        }
        return false;
    }

    public void draw() {
    }

    private int ptCmpToNode(Point2D pt, Node2D node) {
        double diff;
        if (node.divAxis == VERTICAL)
            diff = pt.x() - node.pt.x();
        else diff = pt.y() - node.pt.y();

        if (diff < 0) return -1;
        else if (diff == 0) return 0;
        else return 1;
    }

    public static void main(String[] args) {

    }
}
