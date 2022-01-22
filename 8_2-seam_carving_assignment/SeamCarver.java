import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    // the grid is what the image looks like.
    // the image's pixel at [row, col] corresponds to pixelsRGB[row][col]
    private int width;
    private int height;
    private int[][] pixelsRGB;
    private double[][] pixelsEnergy;
    private boolean energyTransposed;

    public SeamCarver(Picture picture) {
        // creates a deep copy - avoid mutating original picture.
        if (picture == null) throw new IllegalArgumentException();

        width = picture.width();
        height = picture.height();
        // fill the pixels' RGB and energy grid
        pixelsRGB = new int[height][width];
        pixelsEnergy = new double[height][width];
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                pixelsRGB[row][col] = picture.getRGB(col, row);

        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                pixelsEnergy[row][col] = energy(col, row);
    }

    public Picture picture() {
        Picture pic = new Picture(width, height);
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                pic.setRGB(col, row, pixelsRGB[row][col]);

        return pic;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public double energy(int col, int row) {
        if (col < 0 || col > width() - 1 || row < 0 || row > height() - 1)
            throw new IllegalArgumentException();

        if (col == 0 || col == width() - 1 || row == 0 || row == height() - 1)
            return 1000.0;

        Color rightPixel = new Color(pixelsRGB[row][col + 1]);
        Color leftPixel = new Color(pixelsRGB[row][col - 1]);
        int xSquaredGradient = sqrGradient(rightPixel, leftPixel);

        Color abovePixel = new Color(pixelsRGB[row - 1][col]);
        Color belowPixel = new Color(pixelsRGB[row + 1][col]);
        int ySquaredGradient = sqrGradient(abovePixel, belowPixel);

        return Math.sqrt((double) xSquaredGradient + ySquaredGradient);
    }

    public int[] findVerticalSeam() {
        if (energyTransposed) pixelsEnergy = transposeEnergy();
        MinSeam vertSeam = new MinSeam(pixelsEnergy);
        return vertSeam.minSeam();
    }

    public void removeVerticalSeam(int[] seam) {
        if (seam == null ||
                width <= 1 ||
                seam.length != height)
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length - 1; i++)
            if (Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException();

        // remove seam (by shifting the RGB & energy grid to the left)
        if (energyTransposed) pixelsEnergy = transposeEnergy();
        for (int row = 0; row < height; row++)
            for (int col = seam[row]; col < width - 1; col++) {
                pixelsRGB[row][col] = pixelsRGB[row][col + 1];
                pixelsEnergy[row][col] = pixelsEnergy[row][col + 1];
            }

        // update the affected pixels' energy
        for (int row = 0; row < height; row++) {
            final int seamCol = seam[row];
            pixelsEnergy[row][seamCol - 1] = energy(seamCol - 1, row);
            pixelsEnergy[row][seamCol] = energy(seamCol, row);
        }

        width--;
    }

    public int[] findHorizontalSeam() {
        if (!energyTransposed) pixelsEnergy = transposeEnergy();
        MinSeam horiSeam = new MinSeam(pixelsEnergy);
        return horiSeam.minSeam();
    }

    public void removeHorizontalSeam(int[] seam) {
        if (seam == null ||
                height <= 1 ||
                seam.length != width)
            throw new IllegalArgumentException();
        for (int i = 0; i < seam.length - 1; i++)
            if (Math.abs(seam[i] - seam[i + 1]) > 1)
                throw new IllegalArgumentException();

        if (energyTransposed) pixelsEnergy = transposeEnergy();
        // shift up
        for (int col = 0; col < width; col++)
            for (int row = seam[col]; row < height - 1; row++) {
                pixelsRGB[row][col] = pixelsRGB[row + 1][col];
                pixelsEnergy[row][col] = pixelsEnergy[row + 1][col];
            }

        // update affected pixels' energy
        for (int col = 0; col < width; col++) {
            final int seamRow = seam[col];
            pixelsEnergy[seamRow - 1][col] = energy(col, seamRow - 1);
            pixelsEnergy[seamRow][col] = energy(col, seamRow);
        }

        height--;
    }

    private int sqrGradient(Color pixel1, Color pixel2) {
        int redDiffSqr = (int) Math.pow((pixel1.getRed() - pixel2.getRed()), 2);
        int greenDiffSqr = (int) Math.pow((pixel1.getGreen() - pixel2.getGreen()), 2);
        int blueDiffSqr = (int) Math.pow((pixel1.getBlue() - pixel2.getBlue()), 2);
        return redDiffSqr + greenDiffSqr + blueDiffSqr;
    }

    private double[][] transposeEnergy() {
        final int newWidth = pixelsEnergy.length;
        final int newHeight = pixelsEnergy[0].length;
        double[][] transGrid = new double[newHeight][newWidth];

        for (int row = 0; row < newHeight; row++)
            for (int col = 0; col < newWidth; col++)
                transGrid[row][col] = pixelsEnergy[col][row];

        energyTransposed = !energyTransposed;
        return transGrid;
    }

    public static void main(String[] args) {
    }
}
