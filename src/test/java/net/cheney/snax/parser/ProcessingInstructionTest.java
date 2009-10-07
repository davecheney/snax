package net.cheney.snax.parser;

import java.nio.CharBuffer;

import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;
import net.cheney.snax.parser.XMLBuilder;

import org.junit.Assert;
import org.junit.Test;


public class ProcessingInstructionTest {
	
	Namespace DAV_NAMESPACE = Namespace.valueOf("", "DAV:");

	@Test public void singleEmptyElementWithXMLPrelude() {
		CharBuffer xml = CharBuffer.wrap("<?xml version=\"1.0\" encoding=\"utf-8\"?><propfind/>");
		Document doc = new XMLBuilder().parse(xml);
		Assert.assertEquals(doc.rootElement(), new Element("propfind"));
	}
	
	@Test public void singleEmptyElementWithNamespaceAndXMLPrelude() {
		CharBuffer xml = CharBuffer.wrap("<?xml version=\"1.0\" encoding=\"utf-8\"?><propfind xmlns=\"DAV:\"/>");
		Document doc = new XMLBuilder().parse(xml);
		Assert.assertEquals(doc.rootElement(), new Element(QName.valueOf(DAV_NAMESPACE, "propfind")));
	}
	
	@Test public void multipleElementsWithNamespaceAndXMLPrelude() {
		CharBuffer xml = CharBuffer.wrap("<?xml version=\"1.0\" encoding=\"utf-8\"?><propfind xmlns=\"DAV:\"><prop><resourcetype xmlns=\"DAV:\"/></prop></propfind>");
		Document doc = new XMLBuilder().parse(xml);
		Assert.assertEquals(doc.rootElement(), new Element(QName.valueOf(DAV_NAMESPACE, "propfind"), new Element(QName.valueOf(DAV_NAMESPACE, "prop"), new Element(QName.valueOf(DAV_NAMESPACE, "resourcetype")))));
	}
}
