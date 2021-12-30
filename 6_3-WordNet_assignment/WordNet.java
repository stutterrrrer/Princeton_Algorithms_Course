import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WordNet {
    private final int synsetCount;
    private HashMap<String, List<Integer>> nounIDs;
    private LinkedList<Integer>[] hyperAdjLists;
    private LinkedList<Integer>[] hypoAdjLists;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        // read the hypernyms file (less content) to get the vertex count
        In fileReader = new In(hypernyms); // uses java.io.File to read
        int count = 0;
        while (fileReader.hasNextLine()) count++;
        synsetCount = count;
        // file-reading is not done with a try-with block, need to close manually
        fileReader.close();

        // assume the avg number of nouns in each set is 1.5 - minimize resizing.
        nounIDs = new HashMap<>(3 * synsetCount / 2);
        hyperAdjLists = (LinkedList<Integer>[]) new Object[synsetCount];
        hypoAdjLists = (LinkedList<Integer>[]) new Object[synsetCount];

        loadIntoAdjLists(synsets, hypernyms);
    }

    private void loadIntoAdjLists(String synsets, String hypernyms) {
        //TODO load into adjacency-lists; if not rooted-DAG, throw exception.

    }
}
