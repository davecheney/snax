package net.cheney.snax.builder;

import net.cheney.snax.model.Namespace;

public interface StartTag extends Element {

	StartTag attr(String name, String value);

	StartTag defaultNamespace(Namespace namespace);

	@Override
	public StartTag start(String name);

}
