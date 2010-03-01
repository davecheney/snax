package net.cheney.snax.parser;

import net.cheney.snax.model.Node;

final class NodeList implements java.lang.Iterable<Node> {

	private Node[] elements;
	private int length = 0;

	NodeList(Node[] elements) {
		this.elements = elements;
	}

	@Override
	public Iterator iterator() {
		return new Iterator();
	}

	private void doubleCapacity() {
		Node[] target = new Node[elements.length * 2];
		System.arraycopy(elements, 0, target, 0, elements.length);
		elements = target;
	}

	public void add(Node content) {
		ensureCapacity();
		elements[length++] = content;
	}

	private void ensureCapacity() {
		if(length == elements.length) {
			doubleCapacity();
		}
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
