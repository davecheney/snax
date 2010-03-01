package net.cheney.snax.parser;

import net.cheney.snax.model.Node;

final class NodeList implements java.lang.Iterable<Node> {

	private Node[] elements;
	private int length;

	NodeList(Node[] elements, int length) {
		this.elements = elements;
		this.length = length;
	}

	@Override
	public java.util.Iterator<Node> iterator() {
		return new Iterator();
	}

	private static Node[] doubleCapacity(Node[] source) {
		Node[] target = new Node[source.length * 2];
		System.arraycopy(source, 0, target, 0, source.length);
		return target;
	}

	public void add(Node content) {
		if(length == elements.length) {
			elements = doubleCapacity(elements);
		}
		elements[length++] = content;
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
