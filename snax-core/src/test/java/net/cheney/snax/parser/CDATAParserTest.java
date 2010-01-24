package net.cheney.snax.parser;

import net.cheney.snax.model.Element;
import net.cheney.snax.model.Text;

import org.junit.Assert;
import org.junit.Test;


public class CDATAParserTest {

	@Test public void testCDATAParse() {
		String text = "<element><![CDATA[this is the data]]></element>";
		Element element = new XMLBuilder().parse(text).rootElement();
		Text child = (Text) element.children().iterator().next();
		Assert.assertEquals(child.value(), "this is the data");
	}
	
	@Test public void testCDATAParseStyleTwo() {
		String text = "<element><![CDATA[this is the data]]></element>";
		Element element = new XMLBuilder().parse(text).rootElement();
		Assert.assertEquals(element.text(), "this is the data");
	}
	
	@Test public void testCDATAParseTwoTextNodes() {
		String text = "<element><![CDATA[first ]]><unrelated /><![CDATA[second]]></element>";
		Element element = new XMLBuilder().parse(text).rootElement();
		Assert.assertEquals(element.text(), "first second");
	}
	
	@Test public void testCDATAParseThreeNodes() {
		String text = "<element><![CDATA[first ]]>second <![CDATA[third]]></element>";
		Element element = new XMLBuilder().parse(text).rootElement();
		Assert.assertEquals(element.text(), "first second third");
	}

}
