/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private WeightedQuickUnionUF unionFind;
    private boolean[] isOpen;
    private final int top, bottom;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        this.n = n;
        // the n * n grid plus 2 virtual sites.
        unionFind = new WeightedQuickUnionUF(n * n + 2);
        isOpen = new boolean[n * n + 2];
        for (int i = 0; i < n * n; i++) isOpen[i] = false;
        // the 2 virtual sites are always open, waiting to be connected.
        top = n * n;
        bottom = n * n + 1;
        for (int i = n * n; i < isOpen.length; i++) isOpen[i] = true;
    }

    public void open(int col, int row) {
        if (col < 1 || col > n || row < 1 || row > n)
            throw new IllegalArgumentException("colum / row is between 1 and " + n);
        isOpen[siteIndex(col, row)] = true;
        connectNewOpenSite(col, row);
    }

    private void connectNewOpenSite(int col, int row) {
        int thisSite = siteIndex(col, row);
        if (col == 1) unionFind.union(thisSite, top);
        if (col == n) unionFind.union(thisSite, bottom);
        for (int i : adjacentSites(col, row)) {
            if (isOpen[i]) unionFind.union(thisSite, i);
        }
    }

    private int[] adjacentSites(int col, int row) {
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
        validIndexCount += (fourWays[0] = siteIndex(col - 1, row)) == -1 ? 0 : 1;
        validIndexCount += (fourWays[1] = siteIndex(col + 1, row)) == -1 ? 0 : 1;
        validIndexCount += (fourWays[2] = siteIndex(col, row - 1)) == -1 ? 0 : 1;
        validIndexCount += (fourWays[3] = siteIndex(col, row + 1)) == -1 ? 0 : 1;
        int[] adjacent = new int[validIndexCount];
        for (int i = 0, j = 0; i < fourWays.length; i++) {
            if (fourWays[i] != -1) {
                adjacent[j] = fourWays[i];
                j++;
            }
        }
        return adjacent;
    }

    private int siteIndex(int col, int row) {
        if (col < 1 || col > n || row < 1 || row > n) return -1;
        return (row - 1) * n + col - 1;
    }

    public boolean isOpen(int col, int row) {
        return isOpen[siteIndex(col, row)];
    }

    public boolean isFull(int col, int row) {
        // if this open site has the same root as the virtual top row site
        // the `connected()` method is deprecated.
        return isOpen(col, row) && unionFind.find(siteIndex(col, row)) == unionFind.find(top);
    }

    public boolean percolates() {
        return unionFind.find(top) == unionFind.find(bottom);
    }

    public int numberOfOpenSites() {
        int count = 0;
        for (boolean bool : isOpen) {
            if (bool) count++;
        }
        return count;
    }

    public static void main(String[] args) {

    }
}
