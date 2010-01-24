package net.cheney.snax.model;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.collect.Lists;

public class FilterTest extends AbstractTest {

	@Test public void testFilter() {
		Assert.assertEquals(Arrays.asList(2, 4, 6, 8, 10), Lists.newArrayList(evens().filter(Arrays.asList(1,2,3,4,5,6,7,8, 9, 10))));
		Assert.assertEquals(Arrays.asList(1, 3, 5, 7, 9), Lists.newArrayList(odds().filter(Arrays.asList(1,2,3,4,5,6,7,8, 9, 10))));
	}
}
