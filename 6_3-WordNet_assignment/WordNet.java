import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.List;
import java.util.TreeMap;

public class WordNet {
    private final int synsetCount;
    private Digraph toHyper;
    private Digraph toHypo; // need the reverse digraph for ancestral path?
    private TreeMap<String, List<Integer>> nounIDs;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        // read the hypernyms file (less content) to get the vertex count
        In fileReader = new In(hypernyms); // uses java.io.File to read
        int count = 0;
        while (fileReader.hasNextLine()) count++;
        synsetCount = count;
        // file-reading is not done with a try-with block, need to close manually
        fileReader.close();

        toHyper = new Digraph(synsetCount);
        // since key size is unknown, hashmap may need too many resizing
        nounIDs = new TreeMap<>();

        loadIntoDigraph(hypernyms);
        populateNounIDs(synsets);
    }

    private void loadIntoDigraph(String hypernyms) {
        int rootCount = 0;
        In fileReader = new In(hypernyms);
        while (fileReader.hasNextLine()) {
            String[] splits = fileReader.readLine().split(",");
            if (splits[0].isEmpty()) continue;

            int hypo = Integer.parseInt(splits[0]);
            if (splits.length == 1) rootCount++;

            for (int i = 1; i < splits.length; i++) {
                int hyper = Integer.parseInt(splits[i]);
                toHyper.addEdge(hypo, hyper);
            }
        }

        if (rootCount != 1 || new DirectedCycle(toHyper).hasCycle())
            throw new IllegalArgumentException();

        toHypo = toHyper.reverse();
    }

    private void populateNounIDs(String synsets) {
        In fileReader = new In(synsets);
    }
}
