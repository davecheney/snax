package net.cheney.snax.model;

import org.junit.Assert;
import org.junit.Test;

public class DocumentTest extends ParentNodeTest {

	@Override
	public void testDetatch() {
		ContainerNode e = new Element("e");
		Document d = new Document(e);
		Assert.assertEquals(new Document(), d.detach());
	}

	@Override
	public void testType() {
		ContainerNode e = new Element("e");
		Document d = new Document(e);
		Assert.assertEquals(Node.Type.DOCUMENT, d.type());
	}
	
	@Test
	public void testRootElement() {
		ContainerNode e = new Element("e");
		Document d = new Document(e);
		Assert.assertEquals(e, d.rootElement());
	}
	
}
