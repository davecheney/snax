package net.cheney.snax.model;

import java.util.Iterator;
import java.util.List;

final class FastArrayList implements Iterable<Node> {

	private final Node[] elements;
	private final int length;

	private FastArrayList(Node[] elements, int length) {
		this.elements = elements;
		this.length = length;
	}

	private FastArrayList(Node[] elements) {
		this(elements, elements.length);
	}

	@Override
	public Iterator<Node> iterator() {
		return new Iterator<Node>() {

			int offset = 0;

			@Override
			public boolean hasNext() {
				return offset < length;
			}

			@Override
			public Node next() {
				return elements[offset++];
			}

			@Override
			public void remove() {
				// yangi
			}

		};
	}

	public static FastArrayList newInstance(List<? extends Node> content) {
		Node[] elements = new Node[content.size()];
		int offset = 0;
		for (Node n : content) {
			elements[offset++] = n;
		}
		return new FastArrayList(elements);
	}

	public static FastArrayList newInstance(Node[] content) {
		Node[] elements = new Node[content.length];
		System.arraycopy(content, 0, elements, 0, content.length);
		return new FastArrayList(elements);
	}

	public static FastArrayList newInstance(Iterable<? extends Node> content) {
		int size = 8, offset = 0;
		Node[] elements = new Node[size];
		for (Node n : content) {
			if (offset == size) {
				elements = doubleCapacity(elements);
				size = elements.length;
			}
			elements[offset++] = n;
		}
		return new FastArrayList(elements, offset);
	}

	private static Node[] doubleCapacity(Node[] elements) {
		Node[] n = new Node[elements.length * 2];
		System.arraycopy(elements, 0, n, 0, elements.length);
		return n;
	}

	// Comparison and hashing

	/**
	 * Compares the specified object with this list for equality. Returns
	 * {@code true} if and only if the specified object is also a list, both
	 * lists have the same size, and all corresponding pairs of elements in the
	 * two lists are <i>equal</i>. (Two elements {@code e1} and {@code e2} are
	 * <i>equal</i> if {@code (e1==null ? e2==null : e1.equals(e2))}.) In other
	 * words, two lists are defined to be equal if they contain the same
	 * elements in the same order.
	 * <p>
	 * 
	 * This implementation first checks if the specified object is this list. If
	 * so, it returns {@code true}; if not, it checks if the specified object is
	 * a list. If not, it returns {@code false}; if so, it iterates over both
	 * lists, comparing corresponding pairs of elements. If any comparison
	 * returns {@code false}, this method returns {@code false}. If either
	 * iterator runs out of elements before the other it returns {@code false}
	 * (as the lists are of unequal length); otherwise it returns {@code true}
	 * when the iterations complete.
	 * 
	 * @param o
	 *            the object to be compared for equality with this list
	 * @return {@code true} if the specified object is equal to this list
	 */
	public boolean equals(Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Iterable))
			return false;

		Iterator<Node> e1 = iterator();
		Iterator e2 = ((Iterable)o).iterator();
		while (e1.hasNext() && e2.hasNext()) {
			Node o1 = e1.next();
			Object o2 = e2.next();
			if (!(o1 == null ? o2 == null : o1.equals(o2)))
				return false;
		}
		return !(e1.hasNext() || e2.hasNext());
	}

	/**
	 * Returns the hash code value for this list.
	 * 
	 * <p>
	 * This implementation uses exactly the code that is used to define the list
	 * hash function in the documentation for the {@link List#hashCode} method.
	 * 
	 * @return the hash code value for this list
	 */
	public int hashCode() {
		int hashCode = 1;
		Iterator<Node> i = iterator();
		while (i.hasNext()) {
			Node obj = i.next();
			hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
		}
		return hashCode;
	}

}
