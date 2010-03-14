package net.cheney.snax.parser;

import static org.junit.Assert.assertEquals;
import net.cheney.snax.SNAX;
import net.cheney.snax.model.ContainerNode;
import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;

import org.junit.Test;

public class DAVTest {

	@Test public void testDavPropfind() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><propfind xmlns=\"DAV:\"><prop><getcontentlength xmlns=\"DAV:\"/><getlastmodified xmlns=\"DAV:\"/><executable xmlns=\"http://apache.org/dav/props/\"/><resourcetype xmlns=\"DAV:\"/><checked-in xmlns=\"DAV:\"/><checked-out xmlns=\"DAV:\"/></prop></propfind>";
		Document doc = SNAX.parse(xml);
		Namespace DAV = Namespace.valueOf("", "DAV:");
		ContainerNode propfind = new Element(QName.valueOf(DAV, "propfind"), 
				new Element(QName.valueOf(DAV, "prop"),
						new Element(QName.valueOf(DAV, "getcontentlength")),
						new Element(QName.valueOf(DAV, "getlastmodified")),
						new Element(QName.valueOf(Namespace.valueOf("", "http://apache.org/dav/props/"), "executable")),
						new Element(QName.valueOf(DAV, "resourcetype")),
						new Element(QName.valueOf(DAV, "checked-in")),
						new Element(QName.valueOf(DAV, "checked-out"))
				)
		);
		assertEquals(propfind, doc.rootElement());
	}
}
