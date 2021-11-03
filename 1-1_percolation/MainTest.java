/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import java.util.Scanner;
import java.nio.file.Paths;

public class MainTest {
    public static void main(String[] args) {
        String fileName = args[0];
        PercolationWithoutVirtualBottom grid = null;
        // some tests don't open all sites before testing.
        int openSiteCount = 0;

        try (Scanner fileReader = new Scanner(Paths.get(fileName))) {
            // first line of the input file is always one integer representing grid dimension.
            int n = Integer.parseInt(fileReader.nextLine());
            grid = new PercolationWithoutVirtualBottom(n);
            while (fileReader.hasNextLine()) {
                String nextLine = fileReader.nextLine();
                if (nextLine.isEmpty()) break;
                // following lines are all 2 integers starting with non-digit white-space.
                String[] splits = nextLine.split("\\D+");
                int row = Integer.parseInt(splits[1]);
                int col = Integer.parseInt(splits[2]);
                grid.open(row, col);

                openSiteCount++;
                if (openSiteCount == 7) break;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // test backwash with test cases shown by the auto-grader.
        System.out.println(grid.percolates() ? "percolates" : "doesn't percolate");
        System.out.println("enter row & column separated by any non-digit char");
        Scanner inputReader = new Scanner(System.in);
        String[] splits = inputReader.nextLine().split("\\D+");
        int row = Integer.parseInt(splits[0]);
        int col = Integer.parseInt(splits[1]);
        System.out.println("site (" + row + ", " + col + ") " +
                                   (grid.isFull(row, col) ? "is full" : "is not full"));
    }
}
