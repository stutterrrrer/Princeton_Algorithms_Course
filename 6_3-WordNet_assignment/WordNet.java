import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class WordNet {
    private final SAP sap;
    private final Digraph toHyper;
    private final TreeMap<String, List<Integer>> nounIDs;
    private final String[] synsetArr;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        // read the hypernyms file (less content) to get the vertex count
        In fileReader = new In(hypernyms); // uses java.io.File to read
        // decrement count by 1, because of the last empty line in the txt file
        int synsetCount = -1;
        while (fileReader.hasNextLine()) synsetCount++;
        // file-reading is not done with a try-with block, need to close manually
        fileReader.close();

        toHyper = new Digraph(synsetCount);
        // since key size is unknown, hashmap may need too many resizing
        nounIDs = new TreeMap<>();
        synsetArr = new String[synsetCount];

        loadIntoDigraph(hypernyms);
        sap = new SAP(toHyper);
        populateNounIDsAndSynsets(synsets);
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
        fileReader.close();

        if (rootCount != 1 || new DirectedCycle(toHyper).hasCycle())
            throw new IllegalArgumentException();
    }

    private void populateNounIDsAndSynsets(String synsets) {
        In fileReader = new In(synsets);
        while (fileReader.hasNextLine()) {
            String[] splits = fileReader.readLine().split(",");
            if (splits[0].isEmpty()) continue;
            // get the ID and synset (nouns separated by space)
            int id = Integer.parseInt(splits[0]);
            synsetArr[id] = splits[1];
            String[] nouns = splits[1].split(" ");

            for (String noun : nouns) {
                nounIDs.putIfAbsent(noun, new ArrayList<>());
                nounIDs.get(noun).add(id);
            }
        }
        fileReader.close();
    }


    public Iterable<String> nouns() {
        return nounIDs.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nounIDs.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        List<Integer> synsetsContainingA = nounIDs.get(nounA);
        List<Integer> synsetsContainingB = nounIDs.get(nounB);
        return sap.length(synsetsContainingA, synsetsContainingB);
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null || !isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        List<Integer> synsetsContainingA = nounIDs.get(nounA);
        List<Integer> synsetsContainingB = nounIDs.get(nounB);
        int shortestCommonAncestor =
                sap.ancestor(synsetsContainingA, synsetsContainingB);
        return synsetArr[shortestCommonAncestor];
    }
}
