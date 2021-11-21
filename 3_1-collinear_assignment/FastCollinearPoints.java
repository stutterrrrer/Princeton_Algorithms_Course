import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> collinearSegments;
    private List<Line> checkedLines;

    public static class Line {
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

    public FastCollinearPoints(Point[] points) {
        List<Point> ptsListImmutable = Arrays.stream(points).toList();
        List<Point> ptsList = new ArrayList<>(ptsListImmutable);
        collinearSegments = new ArrayList<>();
        checkedLines = new ArrayList<>();

        // find all lines.
        while (ptsList.size() > 0) {
            Point thisPt = ptsList.get(0);
            ptsList.sort(thisPt.slopeOrder());

            findAllNewLinesThatPassThisPointAndAddToCollinearSegments(ptsList, thisPt);
            // this point doesn't need to be considered anymore.
            ptsList.remove(thisPt);
        }
    }

    private void findAllNewLinesThatPassThisPointAndAddToCollinearSegments(List<Point> ptsList,
                                                                           Point thisPt) {
        int n = ptsList.size();
        for (int i = 0; i < n;) {
            double thisLineSlope = thisPt.slopeTo(ptsList.get(i));
            // if this line has been checked, go to next point.
            Line thisLine = new Line(thisPt, thisLineSlope);
            if (checkedLines.contains(thisLine)) {
                i++;
                continue;
            }
            else checkedLines.add(thisLine);

            // this line has at least 1 points - thisPt
            int thisLinePtsCount = 1;
            // this line has a beginPoint and an endPoint.
            Point beginPt = thisPt, endPt = thisPt;

            // find all the points on this line
            while (i < n && thisPt.slopeTo(ptsList.get(i)) == thisLineSlope) {
                thisLinePtsCount++;
                Point newPoint = ptsList.get(i);
                beginPt = beginPt.compareTo(newPoint) < 0 ? beginPt : newPoint;
                endPt = endPt.compareTo(newPoint) > 0 ? endPt : newPoint;
                i++;
            }
            // when all points on this line have been found:
            if (thisLinePtsCount >= 4) {
                collinearSegments.add(new LineSegment(beginPt, endPt));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
