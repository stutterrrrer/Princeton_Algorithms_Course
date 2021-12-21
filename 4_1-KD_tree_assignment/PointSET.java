import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

// the brute force version
public class PointSET {

    // treeSet uses compareTo method for ordering -
    // Point2D's compareTo first compares Y coordinates, then X to break ties.
    private TreeSet<Point2D> ptSet;

    public PointSET() {
        ptSet = new TreeSet<>();
    }

    public boolean isEmpty() {
        return ptSet.size() == 0;
    }

    public int size() {
        return ptSet.size();
    }

    public void insert(Point2D pt) {
        if (pt == null) throw new IllegalArgumentException();
        ptSet.add(pt);
    }

    public boolean contains(Point2D pt) {
        if (pt == null) throw new IllegalArgumentException();
        return ptSet.contains(pt);
    }

    public void draw() {
        for (Point2D pt : ptSet) pt.draw();
    }

    public Iterable<Point2D> range(RectHV rectangle) {
        if (rectangle == null) throw new IllegalArgumentException();
        List<Point2D> ptList = new LinkedList<>();
        for (Point2D pt : ptSet) if (rectangle.contains(pt)) ptList.add(pt);
        return ptList;
    }

    public Point2D nearest(Point2D pt) {
        if (pt == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        return Collections.min(ptSet, Comparator.comparing(i -> i.distanceSquaredTo(pt)));
    }

    public static void main(String[] args) {

    }
}
