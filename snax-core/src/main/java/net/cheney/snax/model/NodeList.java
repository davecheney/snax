package net.cheney.snax.model;

import static com.google.common.collect.Iterables.elementsEqual;
import static java.lang.System.arraycopy;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
final class NodeList implements Iterable<Node> {

	final Node[] elements;
	final int length;
	
	NodeList(Iterable<? extends Node> content) {
		int size = 8, offset = 0;
		Node[] elements = new Node[size];
		for (Node n : content) {
			if (offset == size) {
				elements = doubleCapacity(elements);
				size = elements.length;
			}
			elements[offset++] = n;
		}
		this.elements = elements;
		this.length = offset;
	}

	NodeList(Node[] elements) {
		this(elements, elements.length);
	}
	
	private NodeList(@Nonnull final Node[] elements, int length) {
		this.elements = new Node[length];
		arraycopy(elements, 0, this.elements, 0, length);
		this.length = length;
	}

	@Override
	public Iterator iterator() {
		return new Iterator();
	}

	private static Node[] doubleCapacity(@Nonnull final Node[] elements) {
		final Node[] n = new Node[elements.length << 1];
		arraycopy(elements, 0, n, 0, elements.length);
		return n;
	}

	@Override
	public boolean equals(Object obj) {
		return elementsEqual(this, (Iterable<?>) obj);
	}
	
	public int hashCode() {
		int h = 1;
		for(Node n : elements) {
			h = 31 * h + n.hashCode();
		}
		return h;
	}

	public final class Iterator implements java.util.Iterator<Node> {
		private int offset = 0;

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
	}

}
