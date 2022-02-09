import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BoggleSolver {

    private Tries26 triesDict;

    public BoggleSolver(String[] dictionary) {
        triesDict = new Tries26();
        for (String word : dictionary)
            triesDict.put(word);
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Set<String> validWords = new HashSet<>();
        // recursive - DFS; a local tries node at each call.
        // one visited[][] grid for each starting letter
        final int rows = board.rows(), cols = board.cols();
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                StringBuilder strBuilder = new StringBuilder();
                Node26 dictRoot = triesDict.root();
                boolean[][] visited = new boolean[rows][cols];
                dfs(validWords, board, strBuilder, dictRoot, visited, i, j);
            }
        return validWords;
    }

    private void dfs(Set<String> validWords, BoggleBoard board, StringBuilder strBuilder,
                     Node26 node, boolean[][] visited, int row, int col) {
        if (visited[row][col]) return;

        char newChar = board.getLetter(row, col);
        Node26 nextNode = node.getNextNode(newChar);

        // if no such string is in the dictionary:
        if (nextNode == null) return;
        if (newChar == 'Q') {
            nextNode = nextNode.getNextNode('U');
            if (nextNode == null) return;
        }

        // if the dictionary contains this string (word or prefix):
        visited[row][col] = true;
        strBuilder.append(newChar == 'Q' ? "QU" : newChar);

        // if it's a word:
        int strLen = strBuilder.length();
        if (strLen >= 3 && nextNode.isFullWord())
            validWords.add(strBuilder.toString());

        // treat it as a prefix:
        for (int[] adjTiles : adjacentTiles(board, row, col))
            dfs(validWords, board, strBuilder, nextNode, visited, adjTiles[0], adjTiles[1]);
        // upon returning from recursion - undo visit.
        visited[row][col] = false;
        strBuilder.delete(newChar == 'Q' ? strLen - 2 : strLen - 1, strLen);
    }

    private List<int[]> adjacentTiles(BoggleBoard board, int row, int col) {
        final int rows = board.rows(), cols = board.cols();
        // assume row & col parameters are valid.
        List<int[]> adjTiles = new LinkedList<>();
        if (row > 0) adjTiles.add(new int[] { row - 1, col }); // above
        if (row < rows - 1) adjTiles.add(new int[] { row + 1, col }); // below
        if (col > 0) adjTiles.add(new int[] { row, col - 1 }); // left
        if (col < cols - 1) adjTiles.add(new int[] { row, col + 1 }); // right

        if (row > 0 && col > 0) adjTiles.add(new int[] { row - 1, col - 1 }); // upper-left
        if (row > 0 && col < cols - 1)
            adjTiles.add(new int[] { row - 1, col + 1 }); // upper-right
        if (row < rows - 1 && col > 0)
            adjTiles.add(new int[] { row + 1, col - 1 }); // lower-left
        if (row < rows - 1 && col < cols - 1)
            adjTiles.add(new int[] { row + 1, col + 1 }); // lower-right

        return adjTiles;
    }

    public int scoreOf(String word) {
        if (!triesDict.contains(word)) return 0;
        int len = word.length();
        if (len < 3) return 0;
        if (len == 3 || len == 4) return 1;
        else if (len == 5 || len == 6) return len - 3;
        else if (len == 7) return 5;
        else return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
