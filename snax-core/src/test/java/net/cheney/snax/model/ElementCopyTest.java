package net.cheney.snax.model;

import org.junit.Assert;
import org.junit.Test;

public class ElementCopyTest {

	@Test public void testEmptyElementCopy() {
		Element a = new Element("foo");
		Container b = new Element(a.qname(), a.children());
		
		Assert.assertEquals(a, b);
	}
	
	@Test public void testElementWithOneChildCopy() {
		Element a = new Element("foo", new Element("bar"));
		Container b = new Element(a.qname(), a.children());
		
		Assert.assertEquals(a, b);
	}
	
	@Test public void testElementWithOneAttributeCopy() {
		Element a = new Element("foo", new Attribute("bar", "1"));
		Container b = new Element(a.qname(), a.children());
		
		Assert.assertFalse("Attributes are not copied", a.equals(b));
	}
}
