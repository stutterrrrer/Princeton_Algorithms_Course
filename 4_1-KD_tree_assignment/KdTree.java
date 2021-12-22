import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// equal dimension: insert into right sub-tree
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
            else if (parent.divAxis == HORIZONTAL)
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
        if (pt == null) throw new IllegalArgumentException();
        if (treeRoot == null) {
            treeRoot = new Node2D(pt, null, null);
            size++;
            return;
        }

        // recursion has the recursive method as the last line: use loop
        Node2D root = treeRoot;
        while (true) {
            if (root.pt.equals(pt)) return;

            final int cmp = ptCmpToNode(pt, root);
            if (cmp < 0)
                if (root.left == null) {
                    root.left = new Node2D(pt, root, ChildDirection.LEFT);
                    break;
                }
                else root = root.left;
            else // if dimension >= this node's pt's dimension
                if (root.right == null) {
                    root.right = new Node2D(pt, root, ChildDirection.RIGHT);
                    break;
                }
                else root = root.right;
        }
        size++;
    }

    public boolean contains(Point2D pt) {
        if (pt == null) throw new IllegalArgumentException();
        for (Node2D root = treeRoot; root != null; ) {
            Point2D rootPt = root.pt;
            if (rootPt.equals(pt)) return true;

            int cmp = ptCmpToNode(pt, root);
            if (cmp < 0) root = root.left;
            // since the points with equal dimension are inserted in right:
            else root = root.right;
        }
        return false;
    }

    public void draw() {
        if (treeRoot == null) return;
        // level-order traversal, more sensible than in / pre- / post- order
        Queue<Node2D> queue = new LinkedList<>(Collections.singleton(treeRoot));
        while (!queue.isEmpty()) {
            Node2D node = queue.poll();
            Point2D pt = node.pt;
            double x0 = pt.x(), x1 = pt.x(),
                    y0 = pt.y(), y1 = pt.y();
            StdDraw.setPenRadius(0.015);
            StdDraw.setPenColor();
            StdDraw.point(x0, y0);

            // draw the division line
            StdDraw.setPenRadius();
            if (node.divAxis == VERTICAL) {
                y0 = node.rect.ymin();
                y1 = node.rect.ymax();
                StdDraw.setPenColor(StdDraw.RED);
            }
            else {
                x0 = node.rect.xmin();
                x1 = node.rect.xmax();
                StdDraw.setPenColor(StdDraw.BLUE);
            }
            StdDraw.line(x0, y0, x1, y1);

            // enqueue node's children
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        // level-order: add intersecting rect nodes to queue only
        Queue<Node2D> queue = new LinkedList<>(Collections.singleton(treeRoot));
        // each visited node: add points if it's contained in the rect
        List<Point2D> ptList = new LinkedList<>();
        while (!queue.isEmpty()) {
            Node2D node = queue.poll();
            if (rect.contains(node.pt)) ptList.add(node.pt);

            if (node.left != null && rect.intersects(node.left.rect))
                queue.offer(node.left);
            if (node.right != null && rect.intersects(node.right.rect))
                queue.offer(node.right);
        }
        return ptList;
    }

    public Point2D nearest(Point2D pt) {
        if (pt == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        // PriorityQueue<Node2D> minPQ = new PriorityQueue<>(
        //         Comparator.comparingDouble(i -> i.rect.distanceSquaredTo(pt)));
        MinPQ<Node2D> minPQ = new MinPQ<Node2D>(
                Comparator.comparingDouble(i -> i.rect.distanceSquaredTo(pt)));
        Point2D closest = treeRoot.pt;
        double minDist = closest.distanceSquaredTo(pt);

        // first keep diving into the smallest node.rect that contains pt.
        // and add the nodes whose rect doesn't contain pt to the minPQ
        for (Node2D node = treeRoot; node != null; ) {
            Point2D newPt = node.pt;
            double dist = pt.distanceSquaredTo(newPt);
            // update the closest point
            if (dist < minDist) {
                closest = newPt;
                minDist = dist;
            }
            // dive; add to minPQ
            int cmp = ptCmpToNode(pt, node);
            if (cmp < 0) {
                if (node.right != null) minPQ.insert(node.right);
                node = node.left;
            }
            else { // since points with equal dimension are inserted in right
                if (node.left != null) minPQ.insert(node.left);
                node = node.right;
            }
        }

        // next, check the nodes whose rect doesn't contain pt.
        while (!minPQ.isEmpty()) {
            Node2D node = minPQ.delMin();
            // if found the min - no need to check any other rectangles.
            if (minDist <= node.rect.distanceSquaredTo(pt)) break;

            // update the closest point
            Point2D newPt = node.pt;
            double dist = newPt.distanceSquaredTo(pt);
            if (dist < minDist) {
                closest = newPt;
                minDist = dist;
            }

            // add this node's children to minPQ
            insertIfFit(pt, minPQ, minDist, node.left);
            insertIfFit(pt, minPQ, minDist, node.right);
        }

        return closest;
    }

    private void insertIfFit(Point2D pt, MinPQ<Node2D> minPQ, double minDist, Node2D node) {
        if (node != null &&
                node.rect.distanceSquaredTo(pt) < minDist)
            minPQ.insert(node);
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
        KdTree kdTree = new KdTree();
        int count = Integer.parseInt(args[0]);
        for (int i = 0; i < count; i++)
            kdTree.insert(new Point2D(StdRandom.uniform(), StdRandom.uniform()));
        kdTree.draw();

        // test the range method
        StdDraw.setPenColor(StdDraw.MAGENTA);
        StdDraw.setPenRadius(0.008);
        double xMin = StdRandom.uniform(0, 0.5),
                yMin = StdRandom.uniform(0, 0.5);
        RectHV rect = new RectHV(xMin, yMin,
                                 xMin + 0.5, yMin + 0.5);
        rect.draw();
        for (Point2D pt : kdTree.range(rect)) pt.draw();

        // test the nearest method:
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.02);
        Point2D randPt = new Point2D(StdRandom.uniform(), StdRandom.uniform());
        Point2D nearest = kdTree.nearest(randPt);
        randPt.draw();
        StdDraw.setPenRadius(0.005);
        StdDraw.line(randPt.x(), randPt.y(), nearest.x(), nearest.y());

        StdDraw.show();
    }
}
