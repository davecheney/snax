package net.cheney.snax.parser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import net.cheney.snax.model.Namespace;

import org.junit.Test;

public class ArrayMapTest {

	@Test public void testInserts() {
		Namespace foo = Namespace.valueOf("foo"), bar = Namespace.valueOf("bar"), baz = Namespace.valueOf("baz");
		
			NamespaceMap a = new NamespaceMap(1);
		a.put("foo", foo);
		a.put("bar", bar);
		a.put("baz", baz);
		
		assertTrue(a.containsKey("foo"));
		assertTrue(a.containsKey("bar"));
		assertTrue(a.containsKey("baz"));
		
		assertTrue(a.containsValue(foo));
		assertTrue(a.containsValue(bar));
		assertTrue(a.containsValue(baz));
		
		assertEquals(foo, a.get("foo"));
		assertEquals(bar, a.get("bar"));
		assertEquals(baz, a.get("baz"));
	}
}
