/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    /*
    2 union find instance variables,
    1 with both virtual bottom and virtual top sites for fast percolation test (but has backwash problem)
    1 with only virtual top site - avoids backwash, used for accurate isFull() test.
    */
    private WeightedQuickUnionUF backwashUF;
    private WeightedQuickUnionUF noVirtualBottomUF;
    private boolean[] isOpen;
    private final int top;
    private final int bottom;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        this.n = n;
        // the n * n grid plus 2 virtual sites.
        backwashUF = new WeightedQuickUnionUF(n * n + 2);
        noVirtualBottomUF = new WeightedQuickUnionUF(n * n + 1);
        isOpen = new boolean[n * n];
        for (int i = 0; i < isOpen.length; i++) isOpen[i] = false;
        /*
        top doesn't need to be open,
        since it will be connected "manually" in the connectNewOpenSite method,
        */
        top = n * n;
        bottom = n * n + 1;
    }

    public void open(int row, int col) {
        if (col < 1 || col > n || row < 1 || row > n)
            throw new IllegalArgumentException("colum / row is between 1 and " + n);
        if (isOpen[siteIndex(row, col)]) {
            return;
        }
        isOpen[siteIndex(row, col)] = true;
        connectNewOpenSite(row, col);
    }

    private void connectNewOpenSite(int row, int col) {
        int thisSite = siteIndex(row, col);
        if (row == 1) {
            backwashUF.union(thisSite, top);
            noVirtualBottomUF.union(thisSite, top);
        }
        if (row == n) backwashUF.union(thisSite, bottom);

        int[] ints = adjacentSites(row, col);
        for (int i : ints) {
            if (isOpen[i]) {
                backwashUF.union(thisSite, i);
                noVirtualBottomUF.union(thisSite, i);
            }
        }
    }

    private int[] adjacentSites(int row, int col) {
        // minus 2 because the 2 virtual top and bottom sites are always open.
        /* can't use java.util for assignments
        List<Integer> adjacent = new ArrayList<>();
        adjacent.add(siteIndex(col - 1, row));
        adjacent.add(siteIndex(col + 1, row));
        adjacent.add(siteIndex(col, row + 1));
        adjacent.add(siteIndex(col, row - 1));
        return adjacent.stream().filter(i -> i != -1).mapToInt(Integer::intValue).toArray();
        */
        int[] fourWays = new int[4];
        int validIndexCount = 0;
        validIndexCount += (fourWays[0] = siteIndex(row - 1, col)) == -1 ? 0 : 1;
        validIndexCount += (fourWays[1] = siteIndex(row + 1, col)) == -1 ? 0 : 1;
        validIndexCount += (fourWays[2] = siteIndex(row, col - 1)) == -1 ? 0 : 1;
        validIndexCount += (fourWays[3] = siteIndex(row, col + 1)) == -1 ? 0 : 1;
        int[] adjacent = new int[validIndexCount];
        for (int i = 0, j = 0; i < fourWays.length; i++) {
            if (fourWays[i] != -1) {
                adjacent[j] = fourWays[i];
                j++;
            }
        }
        return adjacent;
    }

    private int siteIndex(int row, int col) {
        if (col < 1 || col > n || row < 1 || row > n) return -1;
        return (row - 1) * n + col - 1;
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("colum / col is between 1 and " + n);
        return isOpen[siteIndex(row, col)];
    }

    // use noVirtualBottomUF for isFull method to avoid false positive full() from backwash problem
    public boolean isFull(int row, int col) {
        // if this open site has the same root as the virtual top col site
        // the `connected()` method is deprecated.
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("colum / col is between 1 and " + n);
        return isOpen(row, col) && noVirtualBottomUF.find(siteIndex(row, col)) == noVirtualBottomUF.find(top);
    }

    // use backwashUF for percolates() method for quick run time.
    public boolean percolates() {
        return backwashUF.find(bottom) == backwashUF.find(top);
    }

    public int numberOfOpenSites() {
        int count = 0;
        for (boolean bool : isOpen) count += bool ? 1 : 0;
        return count;
    }
}