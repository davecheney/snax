package net.cheney.snax.builder;

import static net.cheney.snax.model.Namespace.BLANK_PREFIX;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.Comment;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.Node;
import net.cheney.snax.model.QName;
import net.cheney.snax.model.Text;

public class ElementBuilder extends Builder implements StartTag, PrologOrElement {

	private Namespace defaultNamespace = Namespace.NO_NAMESPACE;
	private Builder parent;
	private String localPart;
	
	ElementBuilder(@Nonnull Builder parent, String localPart) {
		this.parent = parent;
		this.localPart = localPart;
	}
	
	@Override
	public StartTag defaultNamespace(Namespace namespace) {
		this.defaultNamespace = namespace;
		return this;
	}
	
	@Override
	public StartTag defaultNamespace(String uri, String value) {
		this.defaultNamespace = Namespace.valueOf(BLANK_PREFIX, uri);
		return this;
	}

	@Override
	public StartTag attr(String name, String value) {
		addContent(new Attribute(name, value));
		return this;
	}
	
	public StartTag attr(String name, Number num) {
		return attr(name, num.toString());
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

	private Node buildElement() {
		return new net.cheney.snax.model.Element(QName.valueOf(defaultNamespace, localPart), contents());
	}
	
	@Override
	public Element child(String name) {
		return new ElementBuilder(this, name);
	}
	
	@Override
	public Element child(String prefix, String name, String text) {
		// TODO this is a hack, it does not pass the namespace prefix into the child ...
		return child(name).text(text);
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

	@Override
	public StartTag namespace(String prefix, String uri, String value) {
		addContent(new Attribute(Namespace.XMLNS_NAMESPACE, prefix, uri));
		return this;
	}

}
