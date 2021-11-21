import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private int count = 0;
    // create a linked list, since arrayList is not allowed.
    private CollinearSegmentsNode firstNode;

    private class CollinearSegmentsNode {
        private LineSegment collinearSegment;
        private CollinearSegmentsNode next;

        public CollinearSegmentsNode(LineSegment collinearSegment,
                                     CollinearSegmentsNode next) {
            this.collinearSegment = collinearSegment;
            this.next = next;
        }
    }

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        int n = points.length;

        // for each segment of 4 points, need the lowest & the highest points
        for (int pt1Index = 0; pt1Index < n - 3; pt1Index++) {
            Point pt1 = points[pt1Index];
            Point lowPt = pt1;
            Point highPt = pt1;

            for (int pt2Index = pt1Index + 1; pt2Index < n - 2; pt2Index++) {
                Point pt2 = points[pt2Index];
                // since Point is an immutable class, can't pass lowPt & highPt by reference.
                lowPt = lowPt.compareTo(pt2) < 0 ? lowPt : pt2;
                highPt = highPt.compareTo(pt2) > 0 ? highPt : pt2;

                for (int pt3Index = pt2Index + 1; pt3Index < n - 1; pt3Index++) {
                    Point pt3 = points[pt3Index];
                    lowPt = lowPt.compareTo(pt3) < 0 ? lowPt : pt3;
                    highPt = highPt.compareTo(pt3) > 0 ? highPt : pt3;

                    for (int pt4Index = pt3Index + 1; pt4Index < n; pt4Index++) {
                        Point pt4 = points[pt4Index];
                        lowPt = lowPt.compareTo(pt4) < 0 ? lowPt : pt4;
                        highPt = highPt.compareTo(pt4) > 0 ? highPt : pt4;

                        if (pt1.slopeTo(pt2) == pt2.slopeTo(pt3)
                                && pt2.slopeTo(pt3) == pt3.slopeTo(pt4)) {
                            // assertions for testing:
                            assert lowPt.compareTo(highPt) < 0;
                            count++;
                            CollinearSegmentsNode oldFirst = firstNode;
                            firstNode = new CollinearSegmentsNode(new LineSegment(lowPt, highPt),
                                                                  oldFirst);
                        }
                    }
                }
            }
        }
    }

    // since Point is an immutable class, can't pass lowPt & highPt by reference.
    // private void updateLowAndHighPoint(Point lowPt, Point highPt, Point newPoint) {}

    public int numberOfSegments() {
        return count;
    }

    public LineSegment[] segments() {
        LineSegment[] collinearSegments = new LineSegment[count];
        int i = 0;
        for (CollinearSegmentsNode traversalNode = firstNode;
             traversalNode != null; traversalNode = traversalNode.next) {
            collinearSegments[i++] = traversalNode.collinearSegment;
        }
        return collinearSegments;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
