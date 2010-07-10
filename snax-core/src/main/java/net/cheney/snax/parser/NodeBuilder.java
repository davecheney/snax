package net.cheney.snax.parser;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Comment;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.Node;
import net.cheney.snax.model.NodeList;
import net.cheney.snax.model.ProcessingInstruction;

abstract class NodeBuilder  {
	
	private final NodeList contents = new NodeList(8);
	
	public Iterable<Node> contents() {
		return contents;
	}

	public abstract void doAttributeName(CharSequence seq);

	public abstract void doAttributeValue(CharSequence seq);

	public abstract void doCharacters(CharSequence seq);

	public void doComment(@Nonnull CharSequence seq) {
		addContent(new Comment(seq.toString()));
	}

	public abstract NodeBuilder doElementEnd();

	public ElementBuilder doElementStart(@Nonnull CharSequence seq) {
		return new ElementBuilder(this, seq);
	}

	public void doProcessingInstruction(@Nonnull CharSequence seq) {
		addContent(new ProcessingInstruction(seq.toString(), ""));
	}
	
	protected void addContent(@Nonnull Node content) {
		this.contents.add(content);
	}
	
	protected abstract Namespace declaredNamespaceForPrefix(String prefix);
	
	static final class NamespaceMap {

		private String[] keys;
		private Namespace[] values;
		private int limit = 0, size;
		
		NamespaceMap(int initialSize) {
			keys = new String[initialSize];
			values = new Namespace[initialSize];
			size = initialSize;
		}

		public void clear() {
			limit = 0;
		}

		public boolean containsKey(String key) {
			for(int i = 0 ; i < limit ; ++i) {
				if(key.equals(keys[i])) {
					return true;
				}
			}
			return false;
		}

		public boolean containsValue(Namespace value) {
			for(int i = 0 ; i < limit ; ++i) {
				if(value.equals(values[i])) {
					return true;
				}
			}
			return false;
			
		}

		public Namespace get(String key) {
			for(int i = 0 ; i < limit ; ++i) {
				if(key.equals(keys[i])) {
					return values[i];
				}
			}
			return null;
		}

		public boolean isEmpty() {
			return limit == 0;
		}

		public Namespace put(String key, Namespace value) {
			ensureCapacity();
			keys[limit] = key;
			values[limit++] = value;
			return value;
		}

		private void ensureCapacity() {
			if(limit == size) {
				doubleCapacity(size * 2);
			}
		}

		private void doubleCapacity(int newSize) {
			String[] newKeys = new String[newSize];
			Namespace[] newValues = new Namespace[newSize];
			System.arraycopy(keys, 0, newKeys, 0, size);
			System.arraycopy(values, 0, newValues, 0, size);
			keys = newKeys;
			values = newValues;
			size = newSize;
		}

		public int size() {
			return limit;
		}
		
	}
}
