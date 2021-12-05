import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private class Node implements Comparable<Node> {
        private final Board board;
        private final int prevMoves;
        private final int manhattan;
        private final Node prevNode;

        public Node(Board board, int prevMoves, Node prevNode) {
            this.board = board;
            this.prevMoves = prevMoves;
            manhattan = board.manhattan();
            this.prevNode = prevNode;
        }

        public int compareTo(Node compared) {
            final int sum =
                    (prevMoves + manhattan) -
                            (compared.prevMoves + compared.manhattan);
            return sum == 0 ? manhattan - compared.manhattan : sum;
        }
    }

    // null if not solvable.
    private Node solvedNode;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        solve(initial);
    }

    private void solve(Board initial) {
        MinPQ<Node> minPQ = new MinPQ<>();
        MinPQ<Node> twinPQ = new MinPQ<>();
        minPQ.insert(new Node(initial, 0, null));
        twinPQ.insert(new Node(initial.twin(), 0, null));

        while (true) {
            Node node = minPQ.delMin();
            Node twinNode = twinPQ.delMin();

            if (node.manhattan == 0) {
                solvedNode = node;
                break;
            }
            else if (twinNode.manhattan == 0) {
                solvedNode = null;
                break;
            }

            addNeighbor(minPQ, node);
            addNeighbor(twinPQ, twinNode);
        }
    }

    private void addNeighbor(MinPQ<Node> pq, Node node) {
        int prevMoves = node.prevMoves;
        final Iterable<Board> neighbors = node.board.neighbors();
        for (Board neighbor : neighbors) {
            if (node.prevNode != null &&
                    neighbor.equals(node.prevNode.board))
                continue;
            pq.insert(new Node(neighbor, prevMoves + 1, node));
        }
    }

    public boolean isSolvable() {
        return solvedNode != null;
    }

    public int moves() {
        return isSolvable() ? solvedNode.prevMoves : -1;
    }

    public Iterable<Board> solution() {
        if (solvedNode == null) return null;
        // LIFO - use stack.
        Stack<Board> trace = new Stack<>();
        for (Node tracker = solvedNode; tracker != null; tracker = tracker.prevNode)
            trace.push(tracker.board);
        return trace;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
        }
    }
}
