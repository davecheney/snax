package net.cheney.snax.model;

import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.QName;

import org.junit.Assert;
import org.junit.Test;


public class AttributeTest {
	
	@Test public void testElementWithOneAttribute() {
		Attribute a = new Attribute("foo", "bar");
		Element e = new Element(QName.valueOf("e"), a);
		
		int attributeCount = 0;
		
		for(Attribute attr : e.attributes()) {
			Assert.assertEquals(a, attr);
			++attributeCount;
		}
		
		Assert.assertEquals(attributeCount, 1);
	}
	
	@Test public void testModesOfConstructionAreEqual() {
		Attribute a = new Attribute("foo", "bar");
		Attribute b = new Attribute(QName.valueOf("foo"), "bar");
		Attribute c = new Attribute(Namespace.NO_NAMESPACE, "foo", "bar");
		
		Assert.assertTrue(a.equals(b) && b.equals(c) && c.equals(a));
	}
	
	@Test public void testQName() {
		QName q = QName.valueOf("foo");
		Attribute a = new Attribute("foo", "bar");
		
		Assert.assertEquals(q, a.qname());
	}
	
	@Test public void testDetatch() {
		Attribute a = new Attribute("foo", "bar");
		
		Assert.assertTrue(a == a.detach());
	}

}
