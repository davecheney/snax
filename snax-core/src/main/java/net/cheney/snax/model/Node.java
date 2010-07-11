package net.cheney.snax.model;

import java.io.IOException;

import net.cheney.snax.writer.XMLWriter;

/**
 * @{link Node} is a class not an interface to limit the number of Node subclasses that can be 
 * instanciated
 * @author dave
 *
 */
public abstract class Node {

	public enum Type {
		ELEMENT {
			public boolean isElement() { return true; }
			public boolean isParentNode() { return true; } 
		},
		DOCUMENT {
			public boolean isParentNode() { return true; }
		},
		TEXT {
		},
		ATTRIBUTE {
		},
		PROCESSING_INSTRUCTION {
		},
		COMMENT {
		},
		NAMESPACE {
		};

		public boolean isParentNode() { return false; }

		public boolean isElement() { return false; }
	}
	
	public interface Visitor {

		void visit(Attribute attribute) throws IOException;

		void visit(Comment comment) throws IOException;

		void visit(Document document) throws IOException;

		void visit(Element element) throws IOException;

		void visit(Text text) throws IOException;

		void visit(ProcessingInstruction pi) throws IOException;

	}
	
	Node() {
		// package private prevents other node types being created
	}
	
	public abstract Type type();
	
	/**
	 * Return a copy of this node without child contents. 
	 * 
	 * @return For {@link ValueNode}s this will be the node itself, for {@link Container}s this will be a clone of the Element including its {@link Attribute}s 
	 */
	abstract Node detach();
	
	public abstract void accept(Visitor visitor) throws IOException;
	
	@Override
	public final String toString() {
		return XMLWriter.write(detach());
	}
	
}