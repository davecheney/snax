package net.cheney.snax.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.cheney.snax.model.ContainerNode.NodeList;

import org.junit.Test;


public class NodeListTest {
	
	@Test public void testCreationFromIterable() {
		List<? extends Node> n = Arrays.asList(new Element("foo"), new Element("bar"), new Element("baz"));
		NodeList l = new NodeList(n);
		int count = 0;
		Iterator<Node> i = l.iterator();
		while(i.hasNext()) {
			++count;
			assertNotNull(i.next());
		}
		assertEquals(3, count);
	}

	@Test public void testIterator() {
		NodeList l = new NodeList(new Node[] { new Element("foo"), new Element("bar"), new Element("baz") });
		int count = 0;
		Iterator<Node> i = l.iterator();
		while(i.hasNext()) {
			++count;
			assertNotNull(i.next());
		}
		assertEquals(3, count);
	}
	
	@Test public void testEmptyIterator() {
		NodeList l = new NodeList(new Node[] {  });
		int count = 0;
		Iterator<Node> i = l.iterator();
		while(i.hasNext()) {
			++count;
			assertNotNull(i.next());
		}
		assertEquals(0, count);
	}
}
