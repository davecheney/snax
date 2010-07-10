package net.cheney.snax.model;

import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;

import org.junit.Assert;
import org.junit.Test;


public class ElementTest extends ParentNodeTest {
	
	@Test public void elementTest() {
		Namespace n = Namespace.valueOf("a", "http://cheney.net/a");
		Container e2 = new Element(QName.valueOf(n, "xml"));
		
		QName q = QName.valueOf(n, "xml");
		Container e3 = new Element(q);
		
		Assert.assertEquals(e2, e3);
	}
	
	@Test public void elementWithOneChildTest() {
		Container child = new Element(QName.valueOf("child"));
		Container parent = new Element(QName.valueOf("parent"), child);
		
		Assert.assertEquals(child, parent.children().first());
	}
	
	@Test
	public void toStringTest() {
		Container foo = new Element(QName.valueOf("foo"));
		Assert.assertEquals("<foo/>", foo.toString());
		
		Container bar = new Element(QName.valueOf("bar"), new Attribute(QName.valueOf("baz"), "blah"));
		Assert.assertEquals("<bar baz=\"blah\"/>", bar.toString());
		
		Container a = new Element("a", new Element("b"));
		Assert.assertEquals("<a/>", a.toString());
	}
	
	@Test public void stringContentsTest() {
		Element foo = new Element("body", new Text("This is the body contents"));
		Assert.assertEquals(foo.text(), "This is the body contents");
	}

	@Override
	public void testDetatch() {
		Attribute a = new Attribute("foo", "bar");
		Container e = new Element("e");
		Container f = new Element("f", a);
		Container g = new Element("g", a, e);
		
		Assert.assertEquals(e, e.detach());
		Assert.assertEquals(f, f.detach());
		Assert.assertEquals(new Element("g", a), g.detach());
	}

	@Override
	public void testType() {
		Container element = new Element("foo");
		Assert.assertEquals(element.type(), Node.Type.ELEMENT);
		
	}
	
}
