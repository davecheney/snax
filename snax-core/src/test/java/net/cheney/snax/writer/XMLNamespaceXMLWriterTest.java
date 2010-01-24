package net.cheney.snax.writer;

import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;
import net.cheney.snax.writer.XMLWriter;

import org.junit.Assert;
import org.junit.Test;

public class XMLNamespaceXMLWriterTest {

	@Test public void simpleElementTest() {
		Element foo = new Element(QName.valueOf(Namespace.XML_NAMESPACE, "foo"));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<xml:foo/>", result);
	}
	
	@Test public void simpleElementWithAttributeTest() {
		Element foo = new Element(QName.valueOf(Namespace.XML_NAMESPACE, "foo"), 
				new Attribute(QName.valueOf(Namespace.NO_NAMESPACE, "bar"), "1"));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<xml:foo bar=\"1\"/>", result);
	}
	
	@Test public void elementWithOneChildTest() {
		Element foo = new Element(QName.valueOf(Namespace.XML_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar")));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<xml:foo><bar/></xml:foo>", result);
	}
	
	@Test public void elementWithTwoChildrenTest() {
		Element foo = new Element(QName.valueOf(Namespace.XML_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar")),
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "baz")));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<xml:foo><bar/><baz/></xml:foo>", result);
	}
	
	@Test public void elementWithTwoDecendants() {
		Element foo = new Element(QName.valueOf(Namespace.XML_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar"),
						new Element(QName.valueOf(Namespace.valueOf("dave"), "baz"))));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<xml:foo><bar><dave:baz/></bar></xml:foo>", result);
	}
}
