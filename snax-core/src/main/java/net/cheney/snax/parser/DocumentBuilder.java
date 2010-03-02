package net.cheney.snax.parser;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Namespace;

final class DocumentBuilder extends NodeBuilder {
	
	private final NamespaceMap declaredNamespaces = new NamespaceMap(1);
	
	DocumentBuilder() {
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
	public NodeBuilder doElementEnd() {
		throw new IllegalStateException();
	}

	@Override
	protected Namespace declaredNamespaceForPrefix(@Nonnull String prefix) {
		return declaredNamespaces.get(prefix);
	}

}
