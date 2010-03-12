package net.cheney.snax.parser;

import net.cheney.snax.SNAX;
import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;

import org.junit.Assert;
import org.junit.Test;


public class NamespaceTest {

	@Test public void testParseNamespace() {
		String xml = "<D:propfind xmlns:D=\"DAV:\"/>";
		Document doc = SNAX.parse(xml);
		Element propfind = new Element(QName.valueOf(Namespace.valueOf("D", "DAV:"), "propfind"));
		Assert.assertEquals(doc.rootElement(), propfind);
	}
	
	// http://www.w3.org/TR/REC-xml-names#dt-prefix
	@Test(expected=IllegalArgumentException.class)
	public void testParseInvalidNamespace() {
		String xml = "<D:propfind xmlns:D='DAV:'><D:prop><bar:foo xmlns:bar=''/></D:prop></D:propfind>";
		Document doc = SNAX.parse(xml);
		Element propfind = new Element(QName.valueOf(Namespace.valueOf("D", "DAV:"), "propfind"));
		Assert.assertEquals(doc.rootElement(), propfind);
	}
	
//	@Test 
//	public void testNamespaceBoundTwice() {
//		String xml = "<!-- http://www.w3.org is bound to n1 and n2 --><x xmlns:n1='http://www.w3.org' xmlns:n2='http://www.w3.org' ><bad a='1'     a='2' /><bad n1:a='1'  n2:a='2' /></x>";
//		Document doc = new XMLBuilder().parse(xml);
//	}
}
