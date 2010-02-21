package net.cheney.snax.parser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;

public class ArrayMapTest {

	@Test public void testInserts() {
		ArrayMap<String, Integer> a = new ArrayMap<String, Integer>(1);
		a.put("foo", 1);
		a.put("bar", 2);
		a.put("baz", 3);
		
		assertTrue(a.containsKey("foo"));
		assertTrue(a.containsKey("bar"));
		assertTrue(a.containsKey("baz"));
		
		assertTrue(a.containsValue(1));
		assertTrue(a.containsValue(2));
		assertTrue(a.containsValue(3));
		
		assertEquals(new Integer(1), a.get("foo"));
		assertEquals(new Integer(2), a.get("bar"));
		assertEquals(new Integer(3), a.get("baz"));
	}
}
