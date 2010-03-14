package net.cheney.snax.parser;

import net.cheney.snax.SNAX;
import net.cheney.snax.model.ContainerNode;
import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Text;

import org.junit.Assert;
import org.junit.Test;


public class ElementParserTest {

	@Test public void singleElementParsingTest() {
		Document doc = SNAX.parse("<foo/>");
		Assert.assertEquals(doc.rootElement(), new Element("foo"));
	}
	
	@Test public void singleElementWithCharactersParsingTest() {
		Document doc = SNAX.parse("<foo>bar</foo>");
		ContainerNode foo = new Element("foo", new Text("bar"));
		Assert.assertEquals(doc.rootElement(), foo);
	}
	
}
