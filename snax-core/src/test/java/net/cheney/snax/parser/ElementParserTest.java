package net.cheney.snax.parser;

import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Text;
import net.cheney.snax.parser.XMLBuilder;

import org.junit.Assert;
import org.junit.Test;


public class ElementParserTest {

	@Test public void singleElementParsingTest() {
		Document doc = new XMLBuilder().parse("<foo/>");
		Assert.assertEquals(doc.rootElement(), new Element("foo"));
	}
	
	@Test public void singleElementWithCharactersParsingTest() {
		Document doc = new XMLBuilder().parse("<foo>bar</foo>");
		Element foo = new Element("foo", new Text("bar"));
		Assert.assertEquals(doc.rootElement(), foo);
	}
	
}
