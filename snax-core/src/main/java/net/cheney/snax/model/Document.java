package net.cheney.snax.model;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import net.cheney.snax.util.Predicate;

@Immutable
public class Document extends Container {
	
	private static final class ChildElementPredicate extends Predicate<Node> {
		@Override
		protected boolean apply(@Nonnull Node node) {
			final Type t = node.type();
			// TODO, http://www.w3.org/TR/xpath-datamodel/#accessors 6.1.1 says should contain TEXT nodes
			return (t == Type.ELEMENT || t == Type.COMMENT || t == Type.PROCESSING_INSTRUCTION);
		}
	}

	private static final Predicate<Node> CHILD_ELEMENT_PREDICATE = new ChildElementPredicate();
	private final NodeList content;
	
	public Document(@Nonnull Node... content) {
		this(new NodeList(content));
	}
	
	public Document(@Nonnull Iterable<? extends Node> content) {
		this(new NodeList(content));
	}
	
	Document(@Nonnull NodeList content) {
		this.content = content;
	}
	
	@Override
	protected NodeList content() {
		return content;
	}
	
	@Override
	public void addContent(Node content) {
		this.content.add(content);
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
	
	@Override
	public boolean equals(Object that) {
		if (that instanceof Document) {
			return this.content().equals(((Document) that).content());
		}
		return false;
	} 
	
	public static Builder builder() {
		return new Document.Builder();
	}
	
	public static class Builder extends Document implements Container.Builder {
		
		private static final NamespaceMap declaredNamespaces = new NamespaceMap(3);
		
		static {
			declaredNamespaces.put(Namespace.NO_NAMESPACE.prefix(), Namespace.NO_NAMESPACE);
			declaredNamespaces.put(Namespace.XML_NAMESPACE.prefix(), Namespace.XML_NAMESPACE);
			declaredNamespaces.put(Namespace.XMLNS_NAMESPACE.prefix(), Namespace.XMLNS_NAMESPACE);
		}
		
		Builder() {
			super(new NodeList(1));
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
		public Container.Builder doElementEnd() {
			throw new IllegalStateException();
		}

		public Namespace declaredNamespaceForPrefix(@Nonnull String prefix) {
			return declaredNamespaces.get(prefix);
		}
		
		public void doComment(@Nonnull CharSequence seq) {
			addContent(new Comment(seq.toString()));
		}

		public Element.Builder doElementStart(@Nonnull CharSequence seq) {
			return Element.builder(this, seq);
		}

		public void doProcessingInstruction(@Nonnull CharSequence seq) {
			addContent(new ProcessingInstruction(seq.toString(), ""));
		}

		public Document build() {
			return this;
		}
	}

}
