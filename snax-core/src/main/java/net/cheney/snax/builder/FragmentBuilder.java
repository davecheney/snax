package net.cheney.snax.builder;

public class FragmentBuilder extends Builder implements PrologOrElement {

	@Override
	public Element child(String localPart) {
		return new ElementBuilder(this, localPart);
	}

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
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Element child(String name, String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element child(String prefix, String name, String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrologOrElement comment(String comment) {
		// TODO Auto-generated method stub
		return null;
	}
}
