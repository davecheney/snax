package net.cheney.snax.model;

import junit.framework.Assert;

public class CommentTest extends ValueNodeTest {

	@Override
	public void testDetatch() {
		Comment c = new Comment("foo");
		
		Assert.assertTrue(c == c.detach());
	}

	@Override
	public void testType() {
		Comment c = new Comment("foo");
		Assert.assertEquals(Node.Type.COMMENT, c.type());
	}

}
