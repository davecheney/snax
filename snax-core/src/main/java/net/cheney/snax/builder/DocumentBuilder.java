package net.cheney.snax.builder;

import net.cheney.snax.model.Comment;

public class DocumentBuilder extends Builder implements PrologOrElement {

	@Override
	public Element end() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element text(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrologOrElement comment(String comment) {
		addContent(new Comment(comment));
		return this;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
