package net.cheney.snax.model;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class FilterTest extends AbstractTest {

	@Test public void testFilter() {
		assertEquals(asList(2, 4, 6, 8, 10), newArrayList(evens().filter(asList(1,2,3,4,5,6,7,8, 9, 10))));
		assertEquals(asList(1, 3, 5, 7, 9), newArrayList(odds().filter(asList(1,2,3,4,5,6,7,8, 9, 10))));
	}
}
