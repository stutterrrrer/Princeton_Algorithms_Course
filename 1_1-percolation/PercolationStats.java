import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int n;
    private final int trials;
    private double[] percolationThresholds;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        this.trials = trials;
        percolationThresholds = new double[trials];
        runSimulations();
    }

    private void runSimulations() {
        // for each trial:
        for (int i = 0; i < trials; i++) {
            // reset everything:
            Percolation percolation = new Percolation(n);
            int[] allSites = new int[n * n];
            for (int j = 0; j < allSites.length; j++) allSites[j] = j;

            for (int blockedCount = allSites.length; !percolation.percolates(); blockedCount--) {
                openRandomSiteAndSwapToEnd(percolation, allSites, blockedCount);
            }
            percolationThresholds[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    // change it to private after unit-testing.
    private void openRandomSiteAndSwapToEnd(Percolation percolation, int[] allSites,
                                            int blockedCount) {
        // open the site, then move it to the end of the allSites array
        int openedSiteIndexInAllSitesArray = StdRandom.uniform(0, blockedCount);
        int openedSiteIndexInUnionFind = allSites[openedSiteIndexInAllSitesArray];
        int[] rowCol = rowAndColOfSiteIndex(openedSiteIndexInUnionFind);
        percolation.open(rowCol[0], rowCol[1]);

        blockedCount--;
        int temp = allSites[blockedCount];
        allSites[blockedCount] = openedSiteIndexInUnionFind;
        allSites[openedSiteIndexInAllSitesArray] = temp;
    }

    // change it to private after unit-testing.
    private int[] rowAndColOfSiteIndex(int siteIndex) {
        int row = (siteIndex / n) + 1;
        int col = siteIndex % n + 1;
        int[] rowCol = { row, col };
        return rowCol;
    }

    // getters: delete after unit-testing.
    // public Percolation getPercolation() {
    //     return percolation;
    // }
    // public int[] getAllSites() {
    //     return allSites;
    // }
    // public int getBlockedCount() {
    //     return blockedCount;
    // }

    public double mean() {
        return StdStats.mean(percolationThresholds);
    }

    public double stddev() {
        return StdStats.stddev(percolationThresholds);
    }

    public double confidenceLo() {
        return mean() - (1.96 * stddev()) / Math.sqrt(trials);
    }

    public double confidenceHi() {
        return mean() + (1.96 * stddev()) / Math.sqrt(trials);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);

        String interval = "95% confidence interval";
        int width = interval.length();
        StringBuilder output = new StringBuilder();
        output.append(
                String.format("%-" + width + "s = %f", "mean", percolationStats.mean()) + '\n');
        output.append(
                String.format("%-" + width + "s = %f", "stddev", percolationStats.stddev()) + '\n');
        output.append(String.format("%-" + width + "s = ", interval));
        output.append(
                "[" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi()
                        + "]");
        System.out.println(output);
    }
}