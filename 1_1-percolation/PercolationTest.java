// import org.junit.Before;
// import org.junit.Test;
//
// import static org.junit.Assert.*;
//
// /* *****************************************************************************
//  *  Name:              Alan Turing
//  *  Coursera User ID:  123456
//  *  Last modified:     1/1/2019
//  **************************************************************************** */
// public class PercolationTest {
//     private Percolation percolation;
//
//     @SuppressWarnings("checkstyle:IllegalToken")
//     @Before
//     public void setUp() {
//         percolation = new Percolation(4);
//     }
//
//     @Test
//     public void firstRowIsFullWhenOpened() {
//         for (int i = 1; i < 5; i++) {
//             percolation.open(i, 1);
//             assertTrue(percolation.isFull(i, 1));
//         }
//     }
//     @Test
//     public void firstColumnIsFullWhenOpened() {
//         for (int i = 1; i < 5; i++) {
//             percolation.open(1, i);
//             assertTrue(percolation.isFull(1, i));
//         }
//     }
//
//     @Test
//     public void percolatesTest() {
//         // open the whole first column
//         for (int i = 1; i < 5; i++) percolation.open(1, i);
//         assertTrue(percolation.percolates());
//     }
//
//     @Test
//     public void zigzag1() {
//         for (int i = 1; i <= 3; i++) {
//             percolation.open(i, 1);
//         }
//         percolation.open(3, 2);
//         for (int i = 1; i <= 4; i++) {
//             percolation.open(i, 3);
//         }
//         assertFalse(percolation.percolates());
//         percolation.open(2,4);
//         assertTrue(percolation.percolates());
//     }
// }
