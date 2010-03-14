package net.cheney.snax.writer;

import static org.junit.Assert.assertEquals;
import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;
import net.cheney.snax.model.Text;
import net.cheney.snax.writer.XMLWriter;

import org.junit.Test;

public class NoNamespaceXMLWriterTest {

	@Test public void simpleElementTest() {
		Element foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"));
		String  result = XMLWriter.write(foo);
		assertEquals("<foo/>", result);
	}
	
	@Test public void simpleElementWithAttributeTest() {
		Element foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"), 
				new Attribute(QName.valueOf(Namespace.NO_NAMESPACE, "bar"), "1"));
		String  result = XMLWriter.write(foo);
		assertEquals("<foo bar=\"1\"/>", result);
	}
	
	@Test public void elementWithOneChildTest() {
		Element foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar")));
		String  result = XMLWriter.write(foo);
		assertEquals("<foo><bar/></foo>", result);
	}
	
	@Test public void elementWithTwoChildrenTest() {
		Element foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar")),
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "baz")));
		String  result = XMLWriter.write(foo);
		assertEquals("<foo><bar/><baz/></foo>", result);
	}
	
	@Test public void elementWithTwoDecendants() {
		Element foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar"),
						new Element(QName.valueOf(Namespace.NO_NAMESPACE, "baz"))));
		String  result = XMLWriter.write(foo);
		assertEquals("<foo><bar><baz/></bar></foo>", result);
	}
	
	@Test public void elementWithTwoDecendantsAndText() {
		Element foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar"),
						new Element(QName.valueOf(Namespace.NO_NAMESPACE, "baz"), new Text("cow"))));
		String  result = XMLWriter.write(foo);
		assertEquals("<foo><bar><baz>cow</baz></bar></foo>", result);
	}
	
	@Test public void elementWithTwoDecendantsAndTextAlternate() {
		Element foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar"),
						new Element(QName.valueOf(Namespace.NO_NAMESPACE, "baz")), new Text("cow")));
		String  result = XMLWriter.write(foo);
		assertEquals("<foo><bar><baz/>cow</bar></foo>", result);
	}
}
