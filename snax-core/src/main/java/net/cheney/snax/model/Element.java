package net.cheney.snax.model;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import net.cheney.snax.util.Predicate;
import net.cheney.snax.util.Predicate.Filter;

@Immutable
public class Element extends Container implements Namespaced {

	private static final class QNamePredicate<T extends Namespaced> extends Predicate<T> {
		private final QName qname;

		private QNamePredicate(@Nonnull QName qname) {
			this.qname = qname;
		}

		@Override
		protected boolean apply(@Nonnull T namespaced) {
			return namespaced.qname().equals(qname);
		}
	}

	private static final class TextTypePredicate extends Predicate<Node> {
		@Override
		protected boolean apply(@Nonnull Node t) {
			return t.type() == Type.TEXT;
		}
	}

	private static final class ChildElementPredicate extends Predicate<Node> {
		@Override
		protected boolean apply(@Nonnull Node node) {
			final Type t = node.type();
			return (t == Type.ELEMENT || t == Type.TEXT || t == Type.COMMENT);
		}
	}

	private final QName qname;

	private static final TextTypePredicate TEXT_TYPE_PREDICATE = new TextTypePredicate();

	private static final ChildElementPredicate CHILD_ELEMENT_PREDICATE = new ChildElementPredicate();

	public Element(@Nonnull String localpart, @Nonnull Node... content) {
		this(QName.valueOf(localpart), content);
	}

	public Element(@Nonnull QName qname, @Nonnull Node... content) {
		this(qname, new NodeList(content));
	}

	public Element(@Nonnull QName qname, @Nonnull Iterable<? extends Node> content) {
		this(qname, new NodeList(content));
	}
	
	Element(@Nonnull QName qname, @Nonnull NodeList content) {
		super(content);
		this.qname = qname;
	}

	@Override
	public final Type type() {
		return Type.ELEMENT;
	}

	@Override
	public final boolean equals(Object that) {
		if (that instanceof Element) {
			return this.qname.equals(((Element) that).qname)
					&& super.equals(that);
		}
		return false;
	}

	@Override
	public final int hashCode() {
		return qname.hashCode() ^ super.hashCode();
	}

	@Override
	public final String localpart() {
		return this.qname.localpart();
	}

	@Override
	public final Namespace namespace() {
		return this.qname.namespace();
	}

	@Override
	public final String prefix() {
		return namespace().prefix();
	}

	@Override
	@Nonnull
	final Container detach() {
		return new Element(this.qname, attributes());
	}

	@SuppressWarnings("unchecked")
	private Iterable<Text> childTextNodes() {
		return (Iterable<Text>) children(withTextPredicate());
	}

	private TextTypePredicate withTextPredicate() {
		return TEXT_TYPE_PREDICATE;
	}

	/**
	 * Return the contents of all {@link Type.TEXT} nodes that are the direct
	 * descendants of this node
	 */
	public final String text() {
		final StringBuilder sb = new StringBuilder();
		for (Text text : childTextNodes()) {
			sb.append(text.value());
		}
		return sb.toString();
	}

	public final QName qname() {
		return qname;
	}

	public final String getAttribute(@Nonnull QName qname) {
		Attribute a = attributes().first();
		return a == null ? null : a.value(); 
	}

	public final Filter<Element> getChildren(@Nonnull QName qname) {
		return new QNamePredicate<Element>(qname).filter(childElements());
	}

	public final boolean hasChildren() {
		return children().any();
	}

	@Override
	protected final ChildElementPredicate childElementPredicate() {
		return CHILD_ELEMENT_PREDICATE;
	}

	@Override
	public final void accept(@Nonnull Visitor visitor) throws IOException {
		visitor.visit(this);
	}
}
