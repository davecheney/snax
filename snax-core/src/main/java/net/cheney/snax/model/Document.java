package net.cheney.snax.model;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import net.cheney.snax.util.Predicate;


@Immutable
public final class Document extends ParentNode {
	
	private static final class ChildElementPredicate extends Predicate<Node> {
		@Override
		protected boolean apply(@Nonnull Node node) {
			final Type t = node.type();
			return (t == Type.ELEMENT || t == Type.COMMENT || t == Type.PROCESSING_INSTRUCTION);
		}
	}

	private static final Predicate<Node> CHILD_ELEMENT_PREDICATE = new ChildElementPredicate();
	
	public Document(@Nonnull Node... content) {
		super(content);
	}
	
	public Document(@Nonnull Iterable<? extends Node> content) {
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

}
