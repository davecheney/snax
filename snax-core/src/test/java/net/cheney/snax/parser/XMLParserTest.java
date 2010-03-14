package net.cheney.snax.parser;

import java.nio.CharBuffer;

import net.cheney.snax.SNAX;
import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.ContainerNode;
import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;

import org.junit.Assert;
import org.junit.Test;


public class XMLParserTest {

	@Test public void simpleElementTest() {
		CharBuffer xml = CharBuffer.wrap("<foo/>");
		Document doc = SNAX.parse(xml);
		Assert.assertEquals(doc.rootElement(), new Element("foo"));
	}
	
	@Test public void simpleElementWithAttributeTest() {
		CharBuffer xml = CharBuffer.wrap("<foo bar=\"1\"/>");
		Document doc = SNAX.parse(xml);
		ContainerNode foo = new Element("foo", new Attribute("bar", "1"));
		Assert.assertEquals(doc.rootElement(), foo);
	}
	
	@Test public void simpleElementStyle2AttributeTest() {
		CharBuffer xml = CharBuffer.wrap("<foo bar=\"1\"></foo>");
		Document doc = SNAX.parse(xml);
		ContainerNode foo = new Element("foo", new Attribute("bar", "1"));
		Assert.assertEquals(doc.rootElement(), foo);
	}
	
	@Test public void elementWithOneChildTest() {
		CharBuffer xml = CharBuffer.wrap("<foo><bar/></foo>");
		Document doc = SNAX.parse(xml);
		ContainerNode foo = new Element("foo", new Element("bar"));
		Assert.assertEquals(doc.rootElement(), foo);
	}
	
	@Test public void elementWithTwoChildrenTest() {
		CharBuffer xml = CharBuffer.wrap("<foo><bar/><baz/></foo>");
		Document doc = SNAX.parse(xml);
		ContainerNode foo = new Element("foo", new Element("bar"), new Element("baz"));
		Assert.assertEquals(doc.rootElement(), foo);
	}
	
	@Test public void elementWithTwoDecendants() {
		CharBuffer xml = CharBuffer.wrap("<foo><bar><baz/></bar></foo>");
		Document doc = SNAX.parse(xml);
		ContainerNode foo = new Element("foo", new Element("bar", new Element("baz")));
		Assert.assertEquals(doc.rootElement(), foo);
	}
}
