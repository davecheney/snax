package net.cheney.snax.parser;

import java.util.Map;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;
import net.cheney.snax.model.Text;
import net.cheney.snax.util.ArrayMap;

class ElementBuilder extends NodeBuilder {
	
	private final NodeBuilder parent;
	
	private final Map<String, Namespace> declaredNamespaces = new ArrayMap<String, Namespace>(1);
	
	private final String elementName; // unqualified name, possibly containing namespace prefix

	private String attrName;

	ElementBuilder(@Nonnull NodeBuilder parent, @Nonnull CharSequence seq) {
		this.parent = parent;
		this.elementName = seq.toString();
	}
	
	@Override
	public void doAttributeName(@Nonnull CharSequence seq) {
		this.attrName = seq.toString();
	}

	@Override
	public void doAttributeValue(@Nonnull CharSequence seq) {
		if(attrName.startsWith("xmlns")) {
			if(attrName.startsWith("xmlns:")) {
				declareNamespace(attrName.substring(6), seq.toString());
			} else {
				declareNamespace(Namespace.BLANK_PREFIX, seq.toString());
			}
		} else {
			addContent(new Attribute(attrName, seq.toString()));
		}
	}

	private void declareNamespace(@Nonnull String prefix, @Nonnull String uri) {
		Namespace namespace = Namespace.valueOf(prefix, uri);
		declaredNamespaces.put(namespace.prefix(), namespace);
	}

	@Override
	public NodeBuilder doElementEnd() {
		Element element = buildElement();
		parent.addContent(element);
		return parent;
	}
	
	private Element buildElement() {
		String prefix = fetchPrefixFromName();
		Namespace namespace = declaredNamespaceForPrefix(prefix);
		String localpart = fetchLocalPartFromName();
		QName qname = QName.valueOf(namespace, localpart);
		return new Element(qname, contents());
	}

	private String fetchLocalPartFromName() {
		int index = elementName.indexOf(':');
		if(index > -1) {
			return elementName.substring(++index);
		} else {
			return elementName;
		}
	}

	private String fetchPrefixFromName() {
		int index = elementName.indexOf(':');
		if(index > -1) {
			return elementName.substring(0, index);
		} else {
			return Namespace.BLANK_PREFIX;
		}
	}

	@Override
	public void doCharacters(@Nonnull CharSequence seq) {
		addContent(new Text(seq.toString()));		
	}
	
	@Override
	protected Namespace declaredNamespaceForPrefix(@Nonnull String prefix) {
		return declaredNamespaces.containsKey(prefix) ? declaredNamespaces.get(prefix) : parent.declaredNamespaceForPrefix(prefix);
	}

}
