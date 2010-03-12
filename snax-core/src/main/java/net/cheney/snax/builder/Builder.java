package net.cheney.snax.builder;

import net.cheney.snax.model.Node;

public abstract class Builder implements Element {
	
	private final NodeList nodes = new NodeList(4);

	protected void addContent(Node node) {
		nodes.add(node);
	}
	
	@Override
	public StartTag start(String name) {
		return new ElementBuilder(this, name);
	}
	
	@Override
	public Element child(String name, String text) {
		 return child(name).text(text);
	}
	
	protected Iterable<Node> contents() {
		return nodes;
	}
	
	private final class NodeList implements java.lang.Iterable<Node> {

		private Node[] elements;
		private int length = 0;

		NodeList(int size) {
			this.elements = new Node[size];
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
	
}
