import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int itemCount;
    private Node first;
    private Node last;

    private class Node {
        private final Item item;
        private Node next;
        private Node previous;

        public Node(Item item) {
            this.item = item;
        }
    }

    public Deque() {
        itemCount = 0;
        first = null;
        last = null;
    }

    public void addFirst(Item item) {
        // corner case - passed in null:
        if (item == null) throw new IllegalArgumentException("non-null element required.");

        Node oldFirst = first;
        first = new Node(item);
        itemCount++;

        if (itemCount == 1) {
            // previous and next would both be null.
            last = first;
        }
        else {
            // first node's previous will always be null.
            first.next = oldFirst;
            oldFirst.previous = first;
        }
    }

    public void addLast(Item item) {
        // corner case - passed in null:
        if (item == null) throw new IllegalArgumentException("non-null element required.");

        if (itemCount == 0) {
            addFirst(item);
            return;
        }

        last.next = new Node(item);
        itemCount++;
        last.next.previous = last;

        last = last.next;
    }

    public Item removeFirst() {
        if (itemCount == 0) throw new NoSuchElementException("Deque is empty");

        Node removed = first;
        first = first.next;
        itemCount--;
        if (itemCount == 0) {
            // without this, `last` would still be pointing to the removed item.
            last = first;
        }
        else {
            // without this the new first's previous would still point to the removed item.
            first.previous = null;
        }

        return removed.item;
    }

    public Item removeLast() {
        if (itemCount == 0) throw new NoSuchElementException("Deque is empty");

        if (itemCount == 1) return removeFirst();

        Node removed = last;
        last = last.previous;
        itemCount--;
        last.next = null;

        return removed.item;
    }

    public boolean isEmpty() {
        return itemCount == 0;
    }

    public int size() {
        return itemCount;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node iteratorNext = first;

        public boolean hasNext() {
            return iteratorNext != null;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("no more elements");

            Item returnedItem = iteratorNext.item;
            iteratorNext = iteratorNext.next;
            return returnedItem;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) deque.addFirst(i);
            else deque.addLast(i);
        }
        Iterator<Integer> iterator = deque.iterator();
        while (iterator.hasNext()) System.out.print(iterator.next() + ", ");
        System.out.println();
        System.out.println("the deque's size is " + deque.size());

        for (int i = 0; !deque.isEmpty(); i++) {
            int removed = i % 2 == 0 ? deque.removeLast() : deque.removeFirst();
            System.out.print(removed + ", ");
        }
    }
}
