package net.cheney.snax.builder;

import net.cheney.snax.SNAX;

import org.junit.Test;

public class FragmentBuilderTest {

	@Test 
	public void singleUnprefixedElement() {
		SNAX.newFragment("foo").close();
	}
	
	@Test 
	public void singleUnprefixedElementWithSingleChild() {
		SNAX.newFragment("foo").child("bar").end().close();
	}
	
	@Test 
	public void singleUnprefixedElementWithSingleChildAndAttribute() {
		SNAX.newFragment("foo").child("bar").end().close();
	}
}
