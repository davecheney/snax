package net.cheney.snax.parser;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;
import net.cheney.snax.model.Text;

final class ElementBuilder extends NodeBuilder {
	
	private final NodeBuilder parent;
	
	private final NamespaceMap declaredNamespaces = new NamespaceMap(1);
	
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
				declareNamespace(attrName.substring(6), seq);
			} else {
				declareNamespace(Namespace.BLANK_PREFIX, seq);
			}
		} else {
			addContent(new Attribute(attrName, seq));
		}
	}

	private void declareNamespace(@Nonnull String prefix, @Nonnull CharSequence seq) {
//		Namespace namespace = Namespace.valueOf(prefix, uri);
		declaredNamespaces.put(prefix, Namespace.valueOf(prefix, seq.toString()));
	}

	@Override
	public NodeBuilder doElementEnd() {
		parent.addContent(buildElement());
		return parent;
	}
	
	private Element buildElement() {
//		Inling these next 4 lines reduces the number of bytecodes from 36 to 26
//		String prefix = fetchPrefixFromName();
//		Namespace namespace = declaredNamespaceForPrefix(prefix);
//		String localpart = fetchLocalPartFromName();
//		QName qname = QName.valueOf(namespace, localpart);
		return new Element(QName.valueOf(declaredNamespaceForPrefix(fetchPrefixFromName()), fetchLocalPartFromName()), contents());
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
		Namespace ns = declaredNamespaces.get(prefix);
		return ns == null ? parent.declaredNamespaceForPrefix(prefix) : ns;
	}
	
	

}
