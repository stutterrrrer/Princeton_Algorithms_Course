import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] arr;
    private int nextItemIndex;

    public RandomizedQueue() {
        arr = (Item[]) new Object[10];
        nextItemIndex = 0;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();

        if (nextItemIndex == arr.length) resize(arr.length * 2);
        arr[nextItemIndex++] = item;
    }

    // remove an item in random order
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();

        int removedItemIndex = StdRandom.uniform(nextItemIndex);
        // move the last item to the place of the removed item.
        // since dequeue & iterator both return items at random, item order doesn't matter.
        Item removed = arr[removedItemIndex];
        arr[removedItemIndex] = arr[--nextItemIndex];
        arr[nextItemIndex] = null;

        if (arr.length > 10 && nextItemIndex > 0 && nextItemIndex == arr.length / 4)
            resize(arr.length / 2);

        return removed;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();

        int randomItemIndex = StdRandom.uniform(nextItemIndex);
        return arr[randomItemIndex];
    }

    private void resize(int newSize) {
        Item[] newArray = (Item[]) new Object[newSize];
        for (int i = 0; i < nextItemIndex; i++) newArray[i] = arr[i];
        arr = newArray;
    }

    public boolean isEmpty() {
        return nextItemIndex == 0;
    }

    public int size() {
        return nextItemIndex;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final Item[] iteratorArr;
        private int iteratorNextItemIndex;

        public RandomizedQueueIterator() {
            iteratorNextItemIndex = 0;

            // first create an identical array
            iteratorArr = (Item[]) new Object[size()];
            for (int i = 0; i < iteratorArr.length; i++) iteratorArr[i] = arr[i];
            // then shuffle the array contents by swapping elements - what's the running time of this?
            for (int i = 0; i < iteratorArr.length; i++) {
                int randomSwapIndex = StdRandom.uniform(iteratorArr.length);
                Item temp = iteratorArr[i];
                iteratorArr[i] = iteratorArr[randomSwapIndex];
                iteratorArr[randomSwapIndex] = temp;
            }
        }

        public boolean hasNext() {
            return iteratorNextItemIndex != iteratorArr.length;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return iteratorArr[iteratorNextItemIndex++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 0; i < 10; i++) queue.enqueue(i);
        System.out.println("queue size is " + queue.size());

        System.out.println("random samples:");
        for (int i = 0; i < 4; i++) {
            System.out.print(queue.sample() + ", ");
        }
        System.out.println();

        Iterator<Integer> iterator = queue.iterator();
        System.out.println("iterator output:");
        while (iterator.hasNext()) System.out.print(iterator.next() + ", ");
        System.out.println();

        Iterator<Integer> iterator2 = queue.iterator();
        System.out.println("iterator output:");
        while (iterator2.hasNext()) System.out.print(iterator2.next() + ", ");
        System.out.println();

        System.out.println("randomized dequeue:");
        while (!queue.isEmpty()) {
            System.out.print(queue.dequeue() + ", ");
        }
    }
}
