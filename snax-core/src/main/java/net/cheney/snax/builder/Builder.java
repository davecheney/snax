package net.cheney.snax.builder;

import net.cheney.snax.model.Node;
import net.cheney.snax.util.MutableNodeList;

public abstract class Builder implements Element {
	
	private final MutableNodeList nodes = new MutableNodeList(4);

	protected void addContent(Node node) {
		nodes.add(node);
	}
	
	@Override
	public StartTag start(String name) {
		return new ElementBuilder(this, name);
	}
	
	@Override
	public Element child(String name, String text) {
		 return child(name).text(text);
	}
	
	protected Iterable<Node> contents() {
		return nodes;
	}
	
}
