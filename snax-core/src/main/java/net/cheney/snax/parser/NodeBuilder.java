package net.cheney.snax.parser;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Comment;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.Node;
import net.cheney.snax.model.ProcessingInstruction;

abstract class NodeBuilder  {
	
	private final NodeList contents = new NodeList(new Node[8]);
	
	public Iterable<Node> contents() {
		return contents;
	}

	public abstract void doAttributeName(CharSequence seq);

	public abstract void doAttributeValue(CharSequence seq);

	public abstract void doCharacters(CharSequence seq);

	public void doComment(@Nonnull CharSequence seq) {
		addContent(new Comment(seq.toString()));
	}

	public abstract NodeBuilder doElementEnd();

	public ElementBuilder doElementStart(@Nonnull CharSequence seq) {
		return new ElementBuilder(this, seq);
	}

	public void doProcessingInstruction(@Nonnull CharSequence seq) {
		addContent(new ProcessingInstruction(seq.toString(), ""));
	}
	
	protected void addContent(@Nonnull Node content) {
		this.contents.add(content);
	}
	
	protected abstract Namespace declaredNamespaceForPrefix(String prefix);
	
	private static final class NodeList implements java.lang.Iterable<Node> {

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
}
