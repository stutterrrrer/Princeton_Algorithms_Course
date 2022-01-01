import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    public String outcast(String[] nouns) {
        // assume nouns contains only valid nouns in the wordNet
        final int n = nouns.length;
        int[][] distGrid = new int[n][n];
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                final int distance = wordNet.distance(nouns[i], nouns[j]);
                distGrid[i][j] = distance;
                distGrid[j][i] = distance;
            }
        }

        int[] distSums = new int[n];
        int max = 0;
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = 0; j < n; j++)
                sum += distGrid[i][j];
            distSums[i] = sum;
            max = distSums[i] > distSums[max] ? i : max;
        }

        return nouns[max];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
