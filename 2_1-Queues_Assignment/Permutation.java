import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int outputCount = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            String str = StdIn.readString();
            if (outputCount == 0) continue;
            if (i < outputCount)
                queue.enqueue(str);
            else {
                // only add this to the queue with the probability of 1/(i + 1)
                if (StdRandom.bernoulli((double) 1 / (i + 1))) {
                    queue.dequeue();
                    queue.enqueue(str);
                }
            }
        }

        for (int i = 0; i < outputCount; i++) {
            System.out.println(queue.dequeue());
        }
    }
}
