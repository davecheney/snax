package net.cheney.snax.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChildElementTest {

	Element parent;
	
	@Before 
	public void setup() {
		parent = new Element("parent",
			new Attribute("attribute1", "1"),
			new Element("child"),
			new Element("child"),
			new Attribute("attribute2", "2"),
			new Comment("comment1"),
			new Element("child", new Attribute("attribute3", "3")),
			new Text("text"),
			new Element("child2")
		);
	}
	
	private Collection<? extends Node> iterableToCollection(Iterable<? extends Node> iterable) {
		ArrayList<Node> list = new ArrayList<Node>();
		for(Node n : iterable) {
			list.add(n);
		}
		return list;
	}
	
	@Test
	public void getAllChildren() {
		List<? extends Node> expected = Arrays.asList(new Element("child"), new Element("child"), new Comment("comment1"),
			new Element("child", new Attribute("attribute3", "3")),
			new Text("text"),
			new Element("child2"));
		Collection<? extends Node> actual = iterableToCollection(parent.children());
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getChildElements() {
		Collection<Element> expected = Arrays.asList(new Element("child"), new Element("child"), 
			new Element("child", new Attribute("attribute3", "3")),
			new Element("child2"));
		Collection<? extends Node> actual = iterableToCollection(parent.childElements());
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getSpecificChildren() {
		Collection<Element> expected = Arrays.asList(new Element("child"), new Element("child"), 
			new Element("child", new Attribute("attribute3", "3")));
		Collection<? extends Node> actual = iterableToCollection(parent.getChildren(QName.valueOf("child")));
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void getFirstChildElement() {
		Element expected = new Element("child");
		Element actual = parent.getFirstChild(QName.valueOf("child"));
		Assert.assertEquals(expected, actual);
	}
}
