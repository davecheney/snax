package net.cheney.snax.builder;

import net.cheney.snax.model.Namespace;

public interface StartTag extends Element {

	StartTag attr(String name, String value);
	
	StartTag attr(String name, Number num);

	StartTag defaultNamespace(Namespace namespace);

	@Override
	public StartTag start(String name);

	StartTag defaultNamespace(String uri, String value);

	StartTag namespace(String prefix, String uri, String value);

}
