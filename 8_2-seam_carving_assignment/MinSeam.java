import java.util.ArrayList;
import java.util.List;

public class MinSeam {
    private int height, width;
    private double[][] pixelsEnergy;
    private int[][] prevPixelCols; // bc the row number will always be one row above.
    private double[][] distTo;
    // update minDist when a last row pixel is successfully relaxed
    private double minDistInLastRow = Double.POSITIVE_INFINITY;
    private int minDistColInLastRow;

    public MinSeam(double[][] pixelsEnergy) {
        this.pixelsEnergy = pixelsEnergy.clone();
        height = pixelsEnergy.length;
        width = pixelsEnergy[0].length;
        prevPixelCols = new int[height][width];
        distTo = new double[height][width];

        findSPT();
    }

    public int[] minSeam() {
        int[] minEnergyColInRow = new int[height];
        minEnergyColInRow[height - 1] = minDistColInLastRow;
        for (int row = height - 2; row >= 0; row--)
            minEnergyColInRow[row] = prevPixelCols[row + 1][minEnergyColInRow[row + 1]];

        return minEnergyColInRow;
    }

    private void findSPT() {
        // one topological order is just going through each row top to bottom
        // there are no edges pointing out from pixels in last row.
        for (int row = 0; row < height - 1; row++)
            for (int col = 0; col < width; col++)
                for (int colOfAdjPixelBelow : adjPixelsCols(col))
                    relaxPathTo(row, col, colOfAdjPixelBelow);
    }

    private void relaxPathTo(int prevPixRow, int prevPixCol, int col) {
        int row = prevPixRow + 1;
        double edgeWeight = pixelsEnergy[prevPixRow][prevPixCol] +
                pixelsEnergy[row][col];
        double newDist = distTo[prevPixRow][prevPixCol] + edgeWeight;
        if (distTo[row][col] > newDist) {
            distTo[row][col] = newDist;
            prevPixelCols[row][col] = prevPixCol;

            // if we reached a bottom-row pixel &
            // it's relaxed to smaller than the current minDist:
            if (row == height - 1 && newDist < minDistInLastRow) {
                minDistInLastRow = newDist;
                minDistColInLastRow = col;
            }
        }

    }

    private List<Integer> adjPixelsCols(int col) {
        List<Integer> adj = new ArrayList<>();
        adj.add(col);
        if (col != 0) adj.add(col - 1);
        if (col != width - 1) adj.add(col + 1);
        return adj;
    }
}
