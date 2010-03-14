package net.cheney.snax.writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.Comment;
import net.cheney.snax.model.Document;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.Node;
import net.cheney.snax.model.ProcessingInstruction;
import net.cheney.snax.model.Text;
import net.cheney.snax.util.Predicate.Filter;

public final class XMLWriter implements Node.Visitor {

	private final NamespaceStack namespaces = new NamespaceStack();
	private final CompactXMLPrinter printer;

	public XMLWriter(@Nonnull Appendable out) {
		this.printer = new CompactXMLPrinter(out);
	}

	private void printNamespace(@Nonnull Namespace namespace) throws IOException {
		if (namespace.equals(namespaces.getNamespace(namespace.prefix()))) {
			return;
		}
		printer.print(namespace);
		namespaces.push(namespace);
	}

	@Override
	public void visit(@Nonnull Attribute attribute) throws IOException {
		final Namespace namespace = attribute.namespace();
		if ((!namespace.equals(Namespace.NO_NAMESPACE))
				&& (!namespace.equals(Namespace.XML_NAMESPACE))) {
			printNamespace(namespace);
		}

		printer.print(attribute);
	}

	@Override
	public void visit(@Nonnull Comment comment) throws IOException {
		printer.print(comment);
	}

	@Override
	public void visit(@Nonnull Document document) throws IOException {
		visit(document.rootElement());
	}
	
	private void printElementNamespace(@Nonnull Namespace ns) throws IOException {
		if (ns.equals(Namespace.NO_NAMESPACE) || ns.equals(Namespace.XML_NAMESPACE) || (namespaces.getNamespace(Namespace.BLANK_PREFIX) != null)) {
			return;
		}
		printNamespace(ns);
	}

	@Override
	public void visit(@Nonnull Element element) throws IOException {
		final int previouslyDeclaredNamespaces = namespaces.size();

		printer.printStartElementOpenTag();
		printer.printQualifiedName(element);
		printElementNamespace(element.namespace());

		// optimization to avoid calling hasChildren() twice
		// avoids the construction of extra iterators
		Filter<? extends Node> children = element.children();
		if(children.any()) {
			visitAttributes(element.attributes());
			printer.printStartElementCloseTag();
			
			for(Node child : children) {
				child.accept(this);
			}
				
			printer.printEndElementTag(element);
		} else {
			visitAttributes(element.attributes());
			printer.printEmptyElementCloseTag();
		}

		while (namespaces.size() > previouslyDeclaredNamespaces) {
			namespaces.pop();
		}
	}

	private void visitAttributes(@Nonnull Iterable<Attribute> attributes) throws IOException {
		for(Attribute attribute : attributes) {
			visit(attribute);
		}
	}

	@Override
	public void visit(@Nonnull Text text) throws IOException {
		printer.print(text);
	}

	public static String write(@Nonnull Node node) {
		final StringBuilder sb = new StringBuilder(1024);
		XMLWriter visitor = new XMLWriter(sb);
		try {
			node.accept(visitor);
		} catch (IOException e) {
			// unpossible
		}
		return sb.toString();
	}

	@Override
	public void visit(@Nonnull ProcessingInstruction pi) throws IOException {
		printer.print(pi);
	}

	private static final class NamespaceStack {

		private final List<String> prefixes = new ArrayList<String>();
		private final List<Namespace> namespaces = new ArrayList<Namespace>();

		public void push(@Nonnull Namespace n) {
			prefixes.add(n.prefix());
			namespaces.add(n);
		}

		public String pop() {
			final int index = size() - 1;
			namespaces.remove(index);
			return prefixes.remove(index);
		}

		public int size() {
			return prefixes.size();
		}

		public Namespace getNamespace(@Nonnull String prefix) {
			final int index = prefixes.lastIndexOf(prefix);
			return index == -1 ? null : namespaces.get(index);
		}
	}
	
}
