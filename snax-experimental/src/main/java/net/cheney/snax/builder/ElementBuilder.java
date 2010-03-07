package net.cheney.snax.builder;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.Comment;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;
import net.cheney.snax.model.Text;

public class ElementBuilder extends Builder implements StartTag, PrologOrElement {

	private Namespace defaultNamespace = Namespace.NO_NAMESPACE;
	private Builder parent;
	private final QName name;
	
	ElementBuilder(@Nonnull Builder parent, String localPart) {
		this.parent = parent;
		this.name = QName.valueOf(defaultNamespace, localPart);
	}
	
	@Override
	public StartTag defaultNamespace(Namespace namespace) {
		this.defaultNamespace = namespace;
		return this;
	}

	@Override
	public StartTag attr(String name, String value) {
		addContent(new Attribute(name, value));
		return this;
	}

	@Override
	public Element end() {
		parent.addContent(buildElement());
		return parent;
	}
	
	@Override
	public Element text(String text) {
		addContent(new Text(text));
		return this;
	}

	private net.cheney.snax.model.Element buildElement() {
		return new net.cheney.snax.model.Element(name, contents());
	}
	
	@Override
	public Element child(String name) {
		return new ElementBuilder(this, name);
	}

	@Override
	public PrologOrElement comment(String comment) {
		addContent(new Comment(comment));
		return this;
	}

	@Override
	public void close() {
		end().close();
	}

}
