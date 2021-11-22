import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class BruteCollinearPoints {
    private List<LineSegment> collinearSegments;
    private List<Line> checkedLines;

    private static class Line {
        private Point pointOnTheLine;
        private double lineSlope;

        public Line(Point pointOnTheLine, double lineSlope) {
            this.pointOnTheLine = pointOnTheLine;
            this.lineSlope = lineSlope;
        }

        public boolean equals(Object compared) {
            if (compared == null) return false;
            if (!compared.getClass().equals(this.getClass())) return false;
            Line comparedLine = (Line) compared;

            double slope = this.lineSlope;
            if (comparedLine.lineSlope == slope)
                return this.pointOnTheLine.slopeTo(comparedLine.pointOnTheLine) == slope;
            // if the slope is not the same
            return false;
        }
    }


    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        collinearSegments = new ArrayList<>();
        checkedLines = new ArrayList<>();
        int n = points.length;

        // for each segment of 4 points, need the lowest & the highest points
        for (int pt1Index = 0; pt1Index < n - 3; pt1Index++) {
            Point pt1 = points[pt1Index];
            Point beginPt = pt1;
            Point endPt = pt1;

            for (int pt2Index = pt1Index + 1; pt2Index < n - 2; pt2Index++) {
                Point pt2 = points[pt2Index];
                // since Point is an immutable class, can't pass beginPt & endPt by reference.
                beginPt = beginPt.compareTo(pt2) < 0 ? beginPt : pt2;
                endPt = endPt.compareTo(pt2) > 0 ? endPt : pt2;

                for (int pt3Index = pt2Index + 1; pt3Index < n - 1; pt3Index++) {
                    Point pt3 = points[pt3Index];
                    beginPt = beginPt.compareTo(pt3) < 0 ? beginPt : pt3;
                    endPt = endPt.compareTo(pt3) > 0 ? endPt : pt3;

                    for (int pt4Index = pt3Index + 1; pt4Index < n; pt4Index++) {
                        Point pt4 = points[pt4Index];
                        beginPt = beginPt.compareTo(pt4) < 0 ? beginPt : pt4;
                        endPt = endPt.compareTo(pt4) > 0 ? endPt : pt4;

                        double slope = pt1.slopeTo(pt2);
                        if (pt2.slopeTo(pt3) == slope && pt3.slopeTo(pt4) == slope) {
                            // has this line been checked already?
                            Line thisLine = new Line(pt1, slope);
                            if (checkedLines.contains(thisLine)) continue;

                            checkedLines.add(thisLine);
                            collinearSegments.add(new LineSegment(beginPt, endPt));
                        }
                    }
                }
            }
        }
    }

    public int numberOfSegments() {
        return collinearSegments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] segments = collinearSegments.toArray(new LineSegment[0]);
        return segments;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
