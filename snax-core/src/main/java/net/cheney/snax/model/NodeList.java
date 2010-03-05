package net.cheney.snax.model;

import com.google.common.collect.Iterables;

final class NodeList implements Iterable<Node> {

	final Node[] elements;
	final int length;

	private NodeList(Node[] elements, int length) {
		this.elements = elements;
		this.length = length;
	}

	private NodeList(Node[] elements) {
		this(elements, elements.length);
	}

	@Override
	public Iterator iterator() {
		return new Iterator();
	}

	public static NodeList newInstance(Node[] content) {
		Node[] elements = new Node[content.length];
		System.arraycopy(content, 0, elements, 0, content.length);
		return new NodeList(elements);
	}

	public static NodeList newInstance(Iterable<? extends Node> content) {
		int size = 8, offset = 0;
		Node[] elements = new Node[size];
		for (Node n : content) {
			if (offset == size) {
				elements = doubleCapacity(elements);
				size = elements.length;
			}
			elements[offset++] = n;
		}
		return new NodeList(elements, offset);
	}

	private static Node[] doubleCapacity(Node[] elements) {
		Node[] n = new Node[elements.length * 2];
		System.arraycopy(elements, 0, n, 0, elements.length);
		return n;
	}

	@Override
	public boolean equals(Object obj) {
		return Iterables.elementsEqual(this, (Iterable<?>) obj);
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
