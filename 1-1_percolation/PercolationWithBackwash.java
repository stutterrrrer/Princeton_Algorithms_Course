/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationWithBackwash {
    private final int n;
    private WeightedQuickUnionUF unionFind;
    private boolean[] isOpen;
    private final int top, bottom;

    public PercolationWithBackwash(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        this.n = n;
        // the n * n grid plus 2 virtual sites.
        unionFind = new WeightedQuickUnionUF(n * n + 2);
        isOpen = new boolean[n * n + 2];
        for (int i = 0; i < n * n; i++) isOpen[i] = false;
        // the 2 virtual sites are always open, waiting to be connected.
        // but utilizing virtual sites have the back-wash problem.
        top = n * n;
        bottom = n * n + 1;
        isOpen[top] = true;
        isOpen[bottom] = true;
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
        if (row == 1) unionFind.union(thisSite, top);
        if (row == n) unionFind.union(thisSite, bottom);
        int[] ints = adjacentSites(row, col);
        for (int i : ints) {
            if (isOpen[i]) unionFind.union(thisSite, i);
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

    // change to private after testing
    public int siteIndex(int row, int col) {
        if (col < 1 || col > n || row < 1 || row > n) return -1;
        return (row - 1) * n + col - 1;
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("colum / col is between 1 and " + n);
        return isOpen[siteIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        // if this open site has the same root as the virtual top col site
        // the `connected()` method is deprecated.
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException("colum / col is between 1 and " + n);
        return isOpen(row, col) && unionFind.find(siteIndex(row, col)) == unionFind.find(top);
    }

    public boolean percolates() {
        return unionFind.find(top) == unionFind.find(bottom);
    }

    public int numberOfOpenSites() {
        int count = 0;
        for (boolean bool : isOpen) {
            if (bool) count++;
        }
        // minus 2 because the 2 virtual top and bottom sites are always open.
        return count - 2;
    }

    public static void main(String[] args) {
    }
}
