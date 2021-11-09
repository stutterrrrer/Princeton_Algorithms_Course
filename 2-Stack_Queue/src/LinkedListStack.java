import java.util.Iterator;

public class LinkedListStack<Item> implements Iterable<Item> {
	private Node first = null;

	// a privat inner class
	private class Node {
		Item item;
		Node next;
	}

	public boolean isEmpty() {
		return first == null;
	}

	public Item pop() {
		Item item = first.item;
		first = first.next;
		return item;
	}

	public void push(Item item) {
		Node oldFirst = first;
		first = new Node();
		first.item = item;
		first.next = oldFirst;
	}

	private class StackIterator implements Iterator<Item> {
		// initialization - like a constructor.
		private Node iteratorNext = first;

		@Override
		public boolean hasNext() {
			return iteratorNext != null;
		}
		@Override
		public Item next() {
			Item nextItem = iteratorNext.item;
			iteratorNext = iteratorNext.next;
			return nextItem;
		}
	}

	@Override
	public Iterator<Item> iterator() {
			return new StackIterator();
		}
}