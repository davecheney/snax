package net.cheney.snax.model;

import javax.annotation.Nonnull;


public abstract class NodeBuilder  {
	
	private final NodeList contents = new NodeList(8);
	
	NodeList contents() {
		return contents;
	}

	public abstract void doAttributeName(CharSequence seq);

	public abstract void doAttributeValue(CharSequence seq);

	public abstract void doCharacters(CharSequence seq);

	public void doComment(@Nonnull CharSequence seq) {
		addContent(new Comment(seq.toString()));
	}

	public abstract NodeBuilder doElementEnd();

	public ElementBuilder doElementStart(@Nonnull CharSequence seq) {
		return new ElementBuilder(this, seq);
	}

	public void doProcessingInstruction(@Nonnull CharSequence seq) {
		addContent(new ProcessingInstruction(seq.toString(), ""));
	}
	
	protected void addContent(@Nonnull Node content) {
		this.contents.add(content);
	}
	
	protected abstract Namespace declaredNamespaceForPrefix(String prefix);
}
