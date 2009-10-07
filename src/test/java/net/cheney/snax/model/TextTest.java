package net.cheney.snax.model;

import org.junit.Test;

import junit.framework.Assert;

public class TextTest extends NodeTest {

	@Test public void testLongToText() {
		Assert.assertEquals(new Text("1"), new Text(1));
	}
	
	@Override
	public void testDetatch() {
		Text t = new Text("foo");
		
		Assert.assertTrue(t == t.detach());
	}

	@Override
	public void testType() {
		Text t = new Text("foo");
		
		Assert.assertEquals(Node.Type.TEXT, t.type());
	}
}
