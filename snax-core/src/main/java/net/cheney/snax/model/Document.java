package net.cheney.snax.model;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import net.cheney.snax.util.Predicate;

@Immutable
public final class Document extends Container {
	
	private static final class ChildElementPredicate extends Predicate<Node> {
		@Override
		protected boolean apply(@Nonnull Node node) {
			final Type t = node.type();
			// TODO, http://www.w3.org/TR/xpath-datamodel/#accessors 6.1.1 says should contain TEXT nodes
			return (t == Type.ELEMENT || t == Type.COMMENT || t == Type.PROCESSING_INSTRUCTION);
		}
	}

	private static final Predicate<Node> CHILD_ELEMENT_PREDICATE = new ChildElementPredicate();
	
	public Document(@Nonnull Node... content) {
		this(new NodeList(content));
	}
	
	public Document(@Nonnull Iterable<? extends Node> content) {
		this(new NodeList(content));
	}
	
	Document(@Nonnull NodeList content) {
		super(content);
	}

	@Override
	public Type type() {
		return Type.DOCUMENT;
	}

	@Override
	Document detach() {
		return new Document();
	}
	
	@Nonnull
	public Element rootElement() {
		return childElements().first();
	}
	
	@Override
	protected Predicate<Node> childElementPredicate() {
		return CHILD_ELEMENT_PREDICATE;
	}

	@Override
	public void accept(@Nonnull Visitor visitor) throws IOException {
		visitor.visit(this);
	}
	
	public static class Builder implements Node.Builder {
		
		private final NamespaceMap declaredNamespaces = new NamespaceMap(3);
		private final NodeList contents = new NodeList(2);
		
		public Builder() {
			declaredNamespaces.put(Namespace.NO_NAMESPACE.prefix(), Namespace.NO_NAMESPACE);
			declaredNamespaces.put(Namespace.XML_NAMESPACE.prefix(), Namespace.XML_NAMESPACE);
			declaredNamespaces.put(Namespace.XMLNS_NAMESPACE.prefix(), Namespace.XMLNS_NAMESPACE);
		}

		@Override
		public void doAttributeName(@Nonnull CharSequence seq) {
			throw new IllegalStateException(String.format("Unable to add attribute name [%s]", seq));
		}

		@Override
		public void doAttributeValue(@Nonnull CharSequence seq) {
			throw new IllegalStateException(String.format("Unable to add attribute value [%s]", seq));
		}

		@Override
		public void doCharacters(@Nonnull CharSequence seq) {
			throw new IllegalStateException(String.format("Unable to add characters value [%s]", seq));
		}

		@Override
		public Node.Builder doElementEnd() {
			throw new IllegalStateException();
		}

		public Namespace declaredNamespaceForPrefix(@Nonnull String prefix) {
			return declaredNamespaces.get(prefix);
		}
		
		public void doComment(@Nonnull CharSequence seq) {
			addContent(new Comment(seq.toString()));
		}

		public Element.Builder doElementStart(@Nonnull CharSequence seq) {
			return new Element.Builder(this, seq);
		}

		public void doProcessingInstruction(@Nonnull CharSequence seq) {
			addContent(new ProcessingInstruction(seq.toString(), ""));
		}

		public void addContent(@Nonnull Node content) {
			this.contents.add(content);
		}

		public Document build() {
			return new Document(contents);
		}
	}

}
