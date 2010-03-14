package net.cheney.snax.writer;

import net.cheney.snax.model.ContainerNode;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.QName;
import net.cheney.snax.writer.XMLWriter;

import org.junit.Assert;
import org.junit.Test;


public class NamespaceXMLWriterTest  {
	
	@Test public void singlePrefixNamespacedElementTest() {
		Namespace namespace = Namespace.valueOf("dave");
		ContainerNode foo = new Element(QName.valueOf(namespace, "foo"));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<dave:foo/>", result);
	}
	
	@Test public void singleURINamespacedElementTest() {
		Namespace namespace = Namespace.valueOf("", "http://cheney.net/~dave");
		ContainerNode foo = new Element(QName.valueOf(namespace, "foo"));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<foo xmlns=\"http://cheney.net/~dave\"/>", result);
	}
	
	@Test public void singleQualifiedNamespacedElementTest() {
		Namespace namespace = Namespace.valueOf("dave", "http://cheney.net/~dave");
		ContainerNode foo = new Element(QName.valueOf(namespace, "foo"));
		String  result = XMLWriter.write(foo);
		Assert.assertEquals("<dave:foo xmlns:dave=\"http://cheney.net/~dave\"/>", result);
	}
	
	@Test public void singleQualifiedNamespacedElementWithChildTest() {
		Namespace namespace = Namespace.valueOf("dave", "http://cheney.net/~dave");
		ContainerNode foo = new Element(QName.valueOf(namespace, "foo"),
				new Element(QName.valueOf(namespace, "bar")));
		String result = XMLWriter.write(foo);
		Assert.assertEquals("<dave:foo xmlns:dave=\"http://cheney.net/~dave\"><dave:bar/></dave:foo>", result);
	}
	
	@Test public void singleQualifiedNamespacedElementWithChildTestAlternative() {
		Namespace namespace = Namespace.valueOf("dave", "http://cheney.net/~dave");
		ContainerNode foo = new Element(QName.valueOf(namespace, "foo"),
				new Element(QName.valueOf(Namespace.valueOf("zip", "bap"), "bar")));
		String result = XMLWriter.write(foo);
		Assert.assertEquals("<dave:foo xmlns:dave=\"http://cheney.net/~dave\"><zip:bar xmlns:zip=\"bap\"/></dave:foo>", result);
	}
	
	@Test public void singleQualifiedNamespacedElementWithChildTestAlternative2() {
		Namespace namespace = Namespace.valueOf("dave", "http://cheney.net/~dave");
		ContainerNode foo = new Element(QName.valueOf(namespace, "foo"),
				new Element(QName.valueOf(Namespace.valueOf(Namespace.BLANK_PREFIX, "bap"), "bar")));
		String result = XMLWriter.write(foo);
		Assert.assertEquals("<dave:foo xmlns:dave=\"http://cheney.net/~dave\"><bar xmlns=\"bap\"/></dave:foo>", result);
	}
}
