package net.cheney.snax.model;

import java.io.IOException;

import javax.annotation.Nonnull;

import net.cheney.snax.writer.XMLWriter;

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
	
	public interface Builder {
		
		void doAttributeName(CharSequence seq);
		
		void doAttributeValue(CharSequence seq);
		
		void doCharacters(CharSequence seq);
		
		void doComment(@Nonnull CharSequence seq);
		
		Node.Builder doElementEnd();
		
		Element.Builder doElementStart(@Nonnull CharSequence seq);
		
		void doProcessingInstruction(@Nonnull CharSequence seq);

		void addContent(Node buildElement);

		Namespace declaredNamespaceForPrefix(String prefix);
	}
	
//	public static abstract class Builder {
//			
//			private final NodeList contents = new NodeList(8);
//			
//			NodeList contents() {
//				return contents;
//			}
//
//			public abstract void doAttributeName(CharSequence seq);
//
//			public abstract void doAttributeValue(CharSequence seq);
//
//			public abstract void doCharacters(CharSequence seq);
//
//			public void doComment(@Nonnull CharSequence seq) {
//				addContent(new Comment(seq.toString()));
//			}
//
//			public abstract Node.Builder doElementEnd();
//
//			public Element.Builder doElementStart(@Nonnull CharSequence seq) {
//				return new Element.Builder(this, seq);
//			}
//
//			public void doProcessingInstruction(@Nonnull CharSequence seq) {
//				addContent(new ProcessingInstruction(seq.toString(), ""));
//			}
//			
//			protected void addContent(@Nonnull Node content) {
//				this.contents.add(content);
//			}
//			
//			protected abstract Namespace declaredNamespaceForPrefix(String prefix);
//		}
}
