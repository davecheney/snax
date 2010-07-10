package net.cheney.snax.builder;

import net.cheney.snax.model.Node;
import net.cheney.snax.model.NodeList;

public abstract class Builder implements Element {
	
	private final NodeList nodes = new NodeList(4);

	protected final void addContent(Node node) {
		nodes.add(node);
	}
	
	@Override
	public final StartTag start(String name) {
		return new ElementBuilder(this, name);
	}
	
	@Override
	public final Element child(String name) {
		return start(name);
	}
	
	@Override
	public final Element child(String name, String text) {
		 return child(name).text(text);
	}
	
	@Override
	public final Element child(String prefix, String name, String text) {
		// TODO this is a hack, it does not pass the namespace prefix into the child ...
		return child(name).text(text);
	}	
	protected final Iterable<? extends Node> contents() {
		return nodes;
	}
	
}
