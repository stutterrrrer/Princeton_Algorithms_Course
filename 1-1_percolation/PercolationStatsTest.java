

/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */
// public class PercolationStatsTest {
//     private PercolationStats percolationStats;
//     private Percolation percolation;
//     private int[] allSites;
//
//     @Before
//     public void initialize() {
//         percolationStats = new PercolationStats(4, 1);
//         percolation = percolationStats.getPercolation();
//         allSites = percolationStats.getAllSites();
//     }
//
//     @Test
//     public void colAndRowTest() {
//         int[] colAndRow = { 4, 1 };
//         assertArrayEquals(colAndRow, percolationStats.colAndRowOfSiteIndex(3));
//         int[] colAndRow2 = { 2, 3 };
//         assertArrayEquals(colAndRow2, percolationStats.colAndRowOfSiteIndex(9));
//     }
//     @Test
//     public void openRandomSiteTest() {
//         for (int i = 0; i < 4; i++) {
//             percolationStats.openRandomSite();
//         }
//         for (int i = 0; i < percolationStats.getBlockedCount(); i++) {
//             int col = percolationStats.colAndRowOfSiteIndex(allSites[i])[0];
//             int row = percolationStats.colAndRowOfSiteIndex(allSites[i])[1];
//             assertFalse(percolation.isOpen(col,row));
//         }
//         for (int i = percolationStats.getBlockedCount(); i < allSites.length; i++) {
//             int col = percolationStats.colAndRowOfSiteIndex(allSites[i])[0];
//             int row = percolationStats.colAndRowOfSiteIndex(allSites[i])[1];
//             assertTrue(percolation.isOpen(col, row));
//         }
//     }
// }