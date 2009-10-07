package net.cheney.snax.model;

import net.cheney.snax.model.QName;

import org.junit.Assert;
import org.junit.Test;


public class QNameTest {

	@Test public void equalityTest() {
		QName foo = QName.valueOf("a");
		QName bar = QName.valueOf("a");
		Assert.assertEquals(foo, bar);
	}
	
}
