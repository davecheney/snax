package net.cheney.snax.model;

import junit.framework.Assert;

import org.junit.Test;


public class FirstTest extends AbstractTest {
	
	@Test public void testFirst() {
		Assert.assertEquals(Integer.valueOf(2), evens().first(sequence(1, 10)));
		Assert.assertEquals(Integer.valueOf(1), odds().first(sequence(1, 10)));
	}
}
