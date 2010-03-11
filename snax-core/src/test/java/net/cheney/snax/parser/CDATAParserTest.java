package net.cheney.snax.parser;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Text;

import org.apache.commons.io.IOUtils;
import org.junit.Test;


public class CDATAParserTest {

	@Test public void testCDATAParse() {
		String text = "<element><![CDATA[this is the data]]></element>";
		Element element = new XMLBuilder().parse(text).rootElement();
		Text child = (Text) element.children().first();
		assertEquals(child.value(), "this is the data");
	}
	
	@Test public void testCDATAParseStyleTwo() {
		String text = "<element><![CDATA[this is the data]]></element>";
		Element element = new XMLBuilder().parse(text).rootElement();
		assertEquals(element.text(), "this is the data");
	}
	
	@Test public void testCDATAParseTwoTextNodes() {
		String text = "<element><![CDATA[first ]]><unrelated /><![CDATA[second]]></element>";
		Element element = new XMLBuilder().parse(text).rootElement();
		assertEquals(element.text(), "first second");
	}
	
	@Test public void testCDATAParseThreeNodes() {
		String text = "<element><![CDATA[first ]]>second <![CDATA[third]]></element>";
		Element element = new XMLBuilder().parse(text).rootElement();
		assertEquals(element.text(), "first second third");
	}
	
	@Test public void testCDATAParseXMLConf() throws IOException {
		String text = loadResource("xmlconf.xml");
		Document doc = new XMLBuilder().parse(text);
		assertNotNull(doc);
		Element root = doc.rootElement();
		assertNotNull(root);
		assertEquals(root.localpart(), "TESTSUITE");
		assertEquals(root.childElements().iterator().next().localpart(), "TESTCASES");
	}

	private String loadResource(String name) throws IOException {
		URL u = CDATAParserTest.class.getClassLoader().getResource(name);
		System.out.println(u);
		InputStream is = CDATAParserTest.class.getClassLoader().getResourceAsStream(name);
		try {
			return IOUtils.toString(is);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

}
