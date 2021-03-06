package net.cheney.snax.model;

import javax.annotation.Nonnull;

import net.cheney.snax.util.Predicate;
import net.cheney.snax.util.Predicate.Filter;

public abstract class Container extends Node implements Attributed {

	private static final class ElementTypePredicate extends Predicate<Node> {
		@Override
		protected boolean apply(Node t) {
			return t.type() == Type.ELEMENT;
		}
	}

	private static final ElementTypePredicate ELEMENT_TYPE_PREDICATE = new ElementTypePredicate();
	private static final AttributeTypePredicate ATTRIBUTE_TYPE_PREDICATE = new AttributeTypePredicate();
	
	public final Filter<? extends Node> children() {
		return childElementPredicate().filter(content());
	}
	
	protected abstract @Nonnull NodeList content();
	
	abstract void addContent(@Nonnull Node content);
	
	abstract Predicate<Node> childElementPredicate();

	@SuppressWarnings("unchecked")
	public final Filter<Element> childElements() {
		return (Filter<Element>) children(elementTypePredicate());
	}

	protected final Predicate<Node> elementTypePredicate() {
		return ELEMENT_TYPE_PREDICATE;
	}

	@Override
	public int hashCode() {
		return this.content().hashCode();
	}
	
	/**
	 * Filter the list of child elements using the given predicate
	 * @param predicate 
	 * @return A Filtered {@link Iterable} of child elements that match the predicate
	 */
	final @Nonnull Filter<? extends Node> children(@Nonnull Predicate<Node> predicate) {
		return predicate.filter(content());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final Filter<Attribute> attributes() {
		return (Filter<Attribute>) children(withAttributePredicate());
	}
	
	private AttributeTypePredicate withAttributePredicate() {
		return ATTRIBUTE_TYPE_PREDICATE;
	}

	protected static final class AttributeTypePredicate extends Predicate<Node> {
		@Override
		protected boolean apply(@Nonnull Node t) {
			return t.type() == Type.ATTRIBUTE;
		}
	}

	public interface Builder {
		
		void doAttributeName(CharSequence seq);
		
		void doAttributeValue(CharSequence seq);
		
		void doCharacters(CharSequence seq);
		
		void doComment(@Nonnull CharSequence seq);
		
		Builder doElementEnd();
		
		Element.Builder doElementStart(@Nonnull CharSequence seq);
		
		void doProcessingInstruction(@Nonnull CharSequence seq);
	
		Namespace declaredNamespaceForPrefix(String prefix);

		void addContent(Node buildElement);
	}

}
