package net.cheney.snax.parser;

import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;
import net.cheney.snax.parser.XMLBuilder;

import org.junit.Assert;
import org.junit.Test;


public class NamespaceTest {

	@Test public void testParseNamespace() {
		String xml = "<D:propfind xmlns:D=\"DAV:\"/>";
		Document doc = new XMLBuilder().parse(xml);
		Element propfind = new Element(QName.valueOf(Namespace.valueOf("D", "DAV:"), "propfind"));
		Assert.assertEquals(doc.rootElement(), propfind);
	}
	
	// http://www.w3.org/TR/REC-xml-names#dt-prefix
	@Test(expected=IllegalArgumentException.class)
	public void testParseInvalidNamespace() {
		String xml = "<D:propfind xmlns:D=\"DAV:\"><D:prop><bar:foo xmlns:bar=\"\"/></D:prop></D:propfind>";
		Document doc = new XMLBuilder().parse(xml);
	}
}
