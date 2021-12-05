import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Board {
    // make it immutable - final.
    private final int[][] tiles;
    private final int n;
    private int hamming = -1;
    private int manhattan = -1;
    private int rowOfBlank;
    private int colOfBlank;

    public Board(int[][] grid) {
        n = grid.length;
        tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = grid[i][j];

        // find the blank tile.
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (grid[row][col] != 0) continue;
                rowOfBlank = row;
                colOfBlank = col;
                return;
            }
        }
    }

    public String toString() {
        StringBuilder board = new StringBuilder(n + '\n');
        for (int[] row : tiles) {
            for (int tile : row)
                board.append(" " + tile);
            board.append('\n');
        }
        return board.toString();
    }

    public int dimension() {
        return n;
    }

    private int computeHamming() {
        int distance = 0;
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++) {
                final int tile = tiles[row][col];
                distance +=
                        tile == goalTile(row, col)
                                || tile == 0 ?
                        0 : 1;
            }

        return distance;
    }

    private int computeManhattan() {
        int distance = 0;
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                distance += Math.abs(goalRow(row, col) - row)
                        + Math.abs(goalCol(row, col) - col);

        return distance;
    }

    public int hamming() {
        return hamming == -1 ? computeHamming() : hamming;
    }

    public int manhattan() {
        return manhattan == -1 ? computeManhattan() : manhattan;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public boolean equals(Object compared) {
        if (this == compared) return true;
        if (compared == null || getClass() != compared.getClass()) return false;
        Board cmpBoard = (Board) compared;

        if (n != cmpBoard.n) return false;
        for (int row = 0; row < n; row++)
            for (int col = 0; col < n; col++)
                if (tiles[row][col] != cmpBoard.tiles[row][col])
                    return false;

        return true;
    }

    public Iterable<Board> neighbors() {
        // return a List, since List implements Iterable interface,
        List<Board> neighbors = new ArrayList<>();
        neighbors.add(boardWithBlankAt(rowOfBlank - 1, colOfBlank));
        neighbors.add(boardWithBlankAt(rowOfBlank + 1, colOfBlank));
        neighbors.add(boardWithBlankAt(rowOfBlank, colOfBlank - 1));
        neighbors.add(boardWithBlankAt(rowOfBlank, colOfBlank + 1));

        // list may contain null if row or col is out of bounds.
        while (neighbors.remove(null)) ;
        return neighbors;
    }

    private Board boardWithBlankAt(int row, int col) {
        if (row < 0 || col < 0 || row >= n || col >= n) return null;
        int[][] neighborBoard = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                neighborBoard[i][j] = tiles[i][j];
        // swap the blank tile with the slid tile
        neighborBoard[rowOfBlank][colOfBlank] = tiles[row][col];
        neighborBoard[row][col] = 0;
        return new Board(neighborBoard);
    }

    public Board twin() {
        // don't want to do random exchange because don't know how long it takes.
        int row1 = (rowOfBlank + 1) % n, col1 = (colOfBlank + 1) % n;
        int row2 = (rowOfBlank + 2) % n, col2 = (colOfBlank + 2) % n;
        int[][] twinBoard = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                twinBoard[i][j] = tiles[i][j];
        twinBoard[row1][col1] = tiles[row2][col2];
        twinBoard[row2][col2] = tiles[row1][col1];
        return new Board(twinBoard);
    }

    private int goalCol(int row, int col) {
        final int tile = tiles[row][col];
        if (tile == 0) return col;
        return (tile - 1) % n;
    }

    private int goalRow(int row, int col) {
        final int tile = tiles[row][col];
        if (tile == 0) return row;
        return (tile - 1) / n;
    }

    private int goalTile(int row, int col) {
        return row * n + col + 1;
    }

    public static void main(String[] args) {
        // scan input, because both scanner and paths class
        // require nio.file library which is not permitted in this course
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine());
        int[][] grid = new int[n][n];
        for (int row = 0; row < n; row++) {
            String input = scanner.nextLine();
            String[] splits = input.split("\\D+");
            // the last of the splits will always be an int
            final int length = splits.length;
            for (int col = n - 1, y = length - 1; col >= 0; col--, y--)
                grid[row][col] = Integer.parseInt(splits[y]);
        }

        Board board = new Board(grid);
        System.out.println(board);
        System.out.println("hamming: " + board.computeHamming() +
                                   ", manhatten: " + board.computeManhattan());
        System.out.println("neightbors: ");
        for (Board neighbor : board.neighbors())
            System.out.println(neighbor + (neighbor.isGoal() ? "goal" : "not goal"));

        final Board twin = board.twin();
        System.out.println("twin:" + '\n' + twin);
        System.out.println("twin is " + (twin.equals(board) ? "" : "not") + "equal");

        Board dupBoard = new Board(grid);
        System.out.println("duplicate is " +
                                   (dupBoard.equals(board) ? "" : "not") + "equal");
    }
}
