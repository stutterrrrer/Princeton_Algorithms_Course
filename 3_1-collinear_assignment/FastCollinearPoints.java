import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class FastCollinearPoints {
    private static final int BUCKET_SIZE = 250;
    // HashMap not allowed. - implement your own to store the checkedLines.
    // using a List would make the list.contains() search method too slow.
    private ArrayList<Line>[] checkedLines;

    private List<LineSegment> collinearSegments;

    private static class Line {
        // Point's x & y instance variables are private and there are no getters.
        private final Point pointOnTheLine;
        private final double lineSlope;

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

        public int hashCode() {
            long temp = Double.doubleToLongBits(lineSlope);
            return Math.abs((int) (temp ^ (temp >>> 32)));
        }
    }

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point point : points)
            if (point == null) throw new IllegalArgumentException();

        List<Point> ptsListImmutable = Arrays.stream(points).collect(Collectors.toList());
        List<Point> ptsList = new ArrayList<>(ptsListImmutable);
        collinearSegments = new ArrayList<>();
        checkedLines = new ArrayList[BUCKET_SIZE];

        // find all lines.
        while (ptsList.size() > 0) {
            Point thisPt = ptsList.get(0);
            ptsList.sort(thisPt.slopeOrder());

            findAllNewLinesThatPassThisPointAndAddToCollinearSegments(ptsList, thisPt);
            // this point doesn't need to be considered anymore.
            // since after sorting thisPt itself will be at index 0 (negative infinity)
            ptsList.remove(0);
        }
    }

    private void findAllNewLinesThatPassThisPointAndAddToCollinearSegments(List<Point> ptsList,
                                                                           Point thisPt) {
        Iterator<Point> iterator = ptsList.iterator();
        // initialize firstPtOnNewLine to be the first element in the list.
        Point firstPtOnNewLine = iterator.next();
        // if after we find a new firstPtOnNewLine, there's no point after it,
        // then this line doesn't need to be considered - stop loop.
        while (iterator.hasNext()) {
            double thisLineSlope = thisPt.slopeTo(firstPtOnNewLine);

            // this line has at least 2 points - thisPt & firstPtOnNewLine
            int thisLinePtsCount = 2;
            Point beginPt = thisPt.compareTo(firstPtOnNewLine) < 0 ? thisPt : firstPtOnNewLine;
            Point endPt = beginPt == thisPt ? firstPtOnNewLine : thisPt;

            // find all the points on this line
            while (iterator.hasNext()) {
                Point newPoint = iterator.next();
                if (thisPt.slopeTo(newPoint) != thisLineSlope) {
                    // a new line started; update the variable for next loop.
                    firstPtOnNewLine = newPoint;
                    break;
                }
                thisLinePtsCount++;
                beginPt = beginPt.compareTo(newPoint) < 0 ? beginPt : newPoint;
                endPt = endPt.compareTo(newPoint) > 0 ? endPt : newPoint;
            }

            if (thisLinePtsCount >= 4) {
                // only update the checkedLines collection when necessary.
                // if the count < 4, then this line won't be added to collinearSegments anyway.
                Line thisLine = new Line(thisPt, thisLineSlope);
                checkForDuplicaLineAndAddToCollinearSegments(thisLine, beginPt, endPt);
            }
        }
    }

    private void checkForDuplicaLineAndAddToCollinearSegments(Line thisLine,
                                                              Point beginPt, Point endPt) {
        int hashValue = thisLine.hashCode() % BUCKET_SIZE;

        if (checkedLines[hashValue] == null)
            checkedLines[hashValue] = new ArrayList<Line>();
        ArrayList<Line> linesAtThisHash = checkedLines[hashValue];
        if (linesAtThisHash.contains(thisLine)) return;

        // if there's no duplicates for this line:
        collinearSegments.add(new LineSegment(beginPt, endPt));
        linesAtThisHash.add(thisLine);
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
