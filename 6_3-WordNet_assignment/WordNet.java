import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class WordNet {
    private final int synsetCount;
    private int root;
    //TODO use alg4 Digraph - SAP's constructor parameter
    private LinkedList<Integer>[] adjHyper;
    private LinkedList<Integer>[] adjHypo;
    private TreeMap<String, List<Integer>> nounIDs;

    // public API doesn't allow returning adjacent lists - no Cycle class possible
    private boolean foundCycle;
    private int visitedCount;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        // read the hypernyms file (less content) to get the vertex count
        In fileReader = new In(hypernyms); // uses java.io.File to read
        int count = 0;
        while (fileReader.hasNextLine()) count++;
        synsetCount = count;
        // file-reading is not done with a try-with block, need to close manually
        fileReader.close();

        // since key size is unknown, hashmap may need too many resizing
        nounIDs = new TreeMap<>();
        adjHyper = (LinkedList<Integer>[]) new Object[synsetCount];
        adjHypo = (LinkedList<Integer>[]) new Object[synsetCount];
        for (int i = 0; i < synsetCount; i++) {
            adjHyper[i] = new LinkedList<>();
            adjHypo[i] = new LinkedList<>();
        }

        loadIntoAdjLists(hypernyms);
        populateNounIDs(synsets);
    }

    private void loadIntoAdjLists(String hypernyms) {
        int rootCount = 0;
        In fileReader = new In(hypernyms);
        while (fileReader.hasNextLine()) {
            String[] splits = fileReader.readLine().split(",");
            if (splits[0].isEmpty()) continue;

            int hypo = Integer.parseInt(splits[0]);
            if (splits.length == 1) {
                root = hypo;
                rootCount++;
            }

            for (int i = 1; i < splits.length; i++) {
                int hyper = Integer.parseInt(splits[i]);
                adjHyper[hypo].add(hyper);
                adjHypo[hyper].add(hypo);
            }
        }
        if (rootCount != 1 || containsCycle()) throw new IllegalArgumentException();
    }

    private boolean containsCycle() {
        boolean[] visited = new boolean[synsetCount];
        boolean[] prevOnThisPath = new boolean[synsetCount];
        // if there's only 1 root,
        // then in a digraph pointing from root to hyponyms,
        // all vertices are reachable from root unless there's a cycle
        dfsForCycle(root, visited, prevOnThisPath);
    }

    private void dfsForCycle(int v, boolean[] visited, boolean[] prevOnThisPath) {
        visited[v] = true;
        prevOnThisPath[v] = true;
        for (int w : adjHyper[v])
            if (!visited[w])
    }

    private void populateNounIDs(String synsets) {
        In fileReader = new In(synsets);
    }
}
