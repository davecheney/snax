package net.cheney.snax.model;

import org.junit.Assert;
import org.junit.Test;

public class NamespaceUnitTest {

	@Test 
	public void testNamespacesWithEquals() {
		Namespace a = Namespace.valueOf("a", "http://cheney.net/a");
		Namespace b = Namespace.valueOf("a", "http://cheney.net/a");
		
		Assert.assertTrue("Two namespaces with identical names are equal", a.equals(b));
	}
	
	@Test
	public void testNamespacesWithIdenticalNamesAreIdentical() {
		Namespace a = Namespace.valueOf("a", "http://cheney.net/a");
		Namespace b = Namespace.valueOf("b", "http://cheney.net/a");
		
		Assert.assertTrue("Two namespaces with identical names are equal", a.equals(b));

	}
	
	@Test
	public void testNamespacesWithoutPrefixIsEqualToAPrefixedNamespace() {
		Namespace a = Namespace.valueOf("a", "http://cheney.net/a");
		Namespace b = Namespace.valueOf(Namespace.BLANK_PREFIX, "http://cheney.net/a");
		
		Assert.assertTrue("Two namespaces with identical names are equal", a.equals(b));
	}
	
}
