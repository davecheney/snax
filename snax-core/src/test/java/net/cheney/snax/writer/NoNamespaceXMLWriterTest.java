package net.cheney.snax.writer;

import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.ContainerNode;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;
import net.cheney.snax.model.Text;
import net.cheney.snax.writer.XMLWriter;

import org.junit.Assert;
import org.junit.Test;

public class NoNamespaceXMLWriterTest {

	@Test public void simpleElementTest() {
		ContainerNode foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<foo/>", result);
	}
	
	@Test public void simpleElementWithAttributeTest() {
		ContainerNode foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"), 
				new Attribute(QName.valueOf(Namespace.NO_NAMESPACE, "bar"), "1"));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<foo bar=\"1\"/>", result);
	}
	
	@Test public void elementWithOneChildTest() {
		ContainerNode foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar")));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<foo><bar/></foo>", result);
	}
	
	@Test public void elementWithTwoChildrenTest() {
		ContainerNode foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar")),
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "baz")));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<foo><bar/><baz/></foo>", result);
	}
	
	@Test public void elementWithTwoDecendants() {
		ContainerNode foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar"),
						new Element(QName.valueOf(Namespace.NO_NAMESPACE, "baz"))));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<foo><bar><baz/></bar></foo>", result);
	}
	
	@Test public void elementWithTwoDecendantsAndText() {
		ContainerNode foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar"),
						new Element(QName.valueOf(Namespace.NO_NAMESPACE, "baz"), new Text("cow"))));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<foo><bar><baz>cow</baz></bar></foo>", result);
	}
	
	@Test public void elementWithTwoDecendantsAndTextAlternate() {
		ContainerNode foo = new Element(QName.valueOf(Namespace.NO_NAMESPACE, "foo"), 
				new Element(QName.valueOf(Namespace.NO_NAMESPACE, "bar"),
						new Element(QName.valueOf(Namespace.NO_NAMESPACE, "baz")), new Text("cow")));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<foo><bar><baz/>cow</bar></foo>", result);
	}
}
