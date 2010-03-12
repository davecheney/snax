package net.cheney.snax.util;

import junit.framework.Assert;


import org.junit.Test;


public class FirstTest extends AbstractTest {
	
	@Test public void testFirst() {
		Assert.assertEquals(Integer.valueOf(2), evens().filter(sequence(1, 10)).first());
		Assert.assertEquals(Integer.valueOf(1), odds().filter(sequence(1, 10)).first());
	}
}
