package net.cheney.snax.parser;

import java.util.Map;
import java.util.Map.Entry;

final class ArrayMap<K, V> {

	private Object[] keys, values;
	private int limit = 0, size;
	
	public ArrayMap(int initialSize) {
		keys = new Object[initialSize];
		values = new Object[initialSize];
		size = initialSize;
	}

	public void clear() {
		limit = 0;
	}

	public boolean containsKey(K key) {
		for(int i = 0 ; i < limit ; ++i) {
			if(key.equals(keys[i])) {
				return true;
			}
		}
		return false;
	}

	public boolean containsValue(V value) {
		for(int i = 0 ; i < limit ; ++i) {
			if(value.equals(values[i])) {
				return true;
			}
		}
		return false;
		
	}

	@SuppressWarnings("unchecked")
	public V get(K key) {
		for(int i = 0 ; i < limit ; ++i) {
			if(key.equals(keys[i])) {
				return (V) values[i];
			}
		}
		return null;
	}

	public boolean isEmpty() {
		return limit == 0;
	}

	public V put(K key, V value) {
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
		Object[] newKeys = new Object[newSize];
		Object[] newValues = new Object[newSize];
		System.arraycopy(keys, 0, newKeys, 0, size);
		System.arraycopy(values, 0, newValues, 0, size);
		keys = newKeys;
		values = newValues;
		size = newSize;
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		for(Entry<? extends K, ? extends V> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public int size() {
		return limit;
	}
	
}
