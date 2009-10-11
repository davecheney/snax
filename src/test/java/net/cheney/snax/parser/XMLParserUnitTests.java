package net.cheney.snax.parser;

import junit.framework.Assert;
import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;

import org.junit.Test;


public class XMLParserUnitTests {

	@Test public void testEmptyElement() {
		String xml = "<element foo='bar'/>";
		Element expected = new Element("element", new Attribute("foo", "bar"));
		Document doc = new XMLBuilder().parse(xml);
		Assert.assertEquals(expected, doc.rootElement());
	}
	
	@Test public void testWhitespaceOnElementEnd() {
		String xml = "<element foo='bar' ></element>";
		Element expected = new Element("element", new Attribute("foo", "bar"));
		Document doc = new XMLBuilder().parse(xml);
		Assert.assertEquals(expected, doc.rootElement());
	}
}
