package net.cheney.snax.model;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Predicate.Filter;

public abstract class ParentNode extends Node {

	private static final class ElementTypePredicate extends Predicate<Node> {
		@Override
		protected boolean apply(Node t) {
			return t.type() == Type.ELEMENT;
		}
	}

	private final NodeList content;

	ParentNode(@Nonnull Node[] content) {
		this.content = NodeList.newInstance(content);
	}
	
	ParentNode(Iterable<? extends Node> content) {
		this.content = NodeList.newInstance(content);
	}

	private static final ElementTypePredicate ELEMENT_TYPE_PREDICATE = new ElementTypePredicate();
	
	public final Filter<? extends Node> children() {
		return childElementPredicate().filter(content);
	}
	
	abstract Predicate<Node> childElementPredicate();

	@SuppressWarnings("unchecked")
	public final Filter<Element> childElements() {
		return (Filter<Element>) children(elementTypePredicate());
	}

	protected final Predicate<Node> elementTypePredicate() {
		return ELEMENT_TYPE_PREDICATE;
	}

	@Override
	public boolean equals(Object that) {
		if (that instanceof ParentNode) {
			return this.content.equals(((ParentNode) that).content);
		}
		return false;
	} 

	@Override
	public int hashCode() {
		return this.content.hashCode();
	}
	
	/**
	 * Filter the list of child elements using the given predicate
	 * @param predicate 
	 * @return A Filtered Iterable of child elements that match the predicate
	 */
	final Filter<? extends Node> children(@Nonnull Predicate<Node> predicate) {
		return predicate.filter(content);
	}

}
