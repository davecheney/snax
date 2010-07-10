package net.cheney.snax.model;

import org.junit.Assert;
import org.junit.Test;

public class DocumentTest extends ParentNodeTest {

	@Override
	public void testDetatch() {
		Container e = new Element("e");
		Document d = new Document(e);
		Assert.assertEquals(new Document(), d.detach());
	}

	@Override
	public void testType() {
		Container e = new Element("e");
		Document d = new Document(e);
		Assert.assertEquals(Node.Type.DOCUMENT, d.type());
	}
	
	@Test
	public void testRootElement() {
		Container e = new Element("e");
		Document d = new Document(e);
		Assert.assertEquals(e, d.rootElement());
	}
	
}
