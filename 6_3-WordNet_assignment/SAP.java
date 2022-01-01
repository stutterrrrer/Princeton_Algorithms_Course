import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class SAP {
    private final Digraph digraph;

    public SAP(Digraph digraph) {
        this.digraph = digraph;
    }

    private int[] ancestorAndLength(Iterable<Integer> v, Iterable<Integer> w) {
        if (containsNull(v) || containsNull(w)) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);

        int n = digraph.V();
        HashMap<Integer, Integer> commonAncestors = new HashMap<>();
        for (int i = 0; i < n; i++)
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i))
                commonAncestors.put(i, bfsV.distTo(i) + bfsW.distTo(i));

        if (commonAncestors.isEmpty()) return new int[] { -1, -1 };

        // sca = shortest common ancestor
        int sca = -1, minDist = Integer.MAX_VALUE;
        for (int ancestor : commonAncestors.keySet()) {
            final int distance = commonAncestors.get(ancestor);
            if (distance < minDist) {
                sca = ancestor;
                minDist = distance;
            }
        }
        return new int[] { sca, minDist };
    }

    private boolean containsNull(Iterable<Integer> v) {
        if (v == null) return true;
        for (Integer i : v) if (i == null) return true;
        return false;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return ancestorAndLength(v, w)[1];
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return ancestorAndLength(v, w)[0];
    }

    public int length(int v, int w) {
        return length(new HashSet<Integer>(Collections.singleton(v)),
                      new HashSet<Integer>(Collections.singleton(w)));
    }

    public int ancestor(int v, int w) {
        return ancestor(new HashSet<Integer>(Collections.singleton(v)),
                        new HashSet<Integer>(Collections.singleton(w)));
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}