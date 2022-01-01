import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class SAP {
    private final Digraph digraph;

    public SAP(Digraph digraph) {
        this.digraph = digraph;
    }

    private int[] ancestorAndLength(Iterable<Integer> v, Iterable<Integer> w) {
        // algs4's  Digraph.adj(), called in the bfsPaths class constructor,
        // throws illegal argument exception if int v or w is out of range
        if (containsNull(v) || containsNull(w)) throw new IllegalArgumentException();

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
        // key: vertex reached in BFS traversal from v;
        // value: shortest distance from set v to the vertex.
        HashMap<Integer, Integer> vAncestorsMap = new HashMap<>();
        for (int i = 0; i < digraph.V(); i++)
            if (bfsV.hasPathTo(i)) vAncestorsMap.put(i, bfsV.distTo(i));

        // BFS traverse w, check queue against vMap
        Queue<Integer> thisLevel = new LinkedList<>();
        for (int wi : w) thisLevel.offer(wi);
        int distFromWtoSca = -1;
        int sca = -1; // shortest common ancestor
        // don't need to check visited: constant time look up in the hashmap.
        while (!thisLevel.isEmpty()) {
            Queue<Integer> nextLevel = new LinkedList<>();
            distFromWtoSca++;
            while (!thisLevel.isEmpty()) {
                int next = thisLevel.poll();
                if (vAncestorsMap.containsKey(next)) {
                    sca = next;
                    break;
                }
                for (int x : digraph.adj(next)) nextLevel.add(x);
            }
            thisLevel = nextLevel;
        }

        return new int[] {
                sca,
                sca == -1 ? -1 : distFromWtoSca + vAncestorsMap.get(sca)
        };
    }

    private boolean containsNull(Iterable<Integer> v) {
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
