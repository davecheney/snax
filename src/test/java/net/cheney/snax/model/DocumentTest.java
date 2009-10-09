package net.cheney.snax.model;

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class DocumentTest extends ParentNodeTest {

	@Override
	public void testDetatch() {
		Element e = new Element("e");
		Document d = new Document(e);
		Assert.assertEquals(new Document(), d.detach());
	}

	@Override
	public void testType() {
		Element e = new Element("e");
		Document d = new Document(e);
		Assert.assertEquals(Node.Type.DOCUMENT, d.type());
	}
	
	@Test
	public void testRootElement() {
		Element e = new Element("e");
		Document d = new Document(e);
		Assert.assertEquals(e, d.rootElement());
	}
	
}
