package net.cheney.snax.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public final class ArrayMap<K, V> implements Map<K, V> {

	private Object[] keys, values;
	private int limit = 0, size;
	
	public ArrayMap(int initialSize) {
		keys = new Object[initialSize];
		values = new Object[initialSize];
		size = initialSize;
	}

	@Override
	public void clear() {
		limit = 0;
	}

	@Override
	public boolean containsKey(Object key) {
		for(int i = 0 ; i < limit ; ++i) {
			if(key.equals(keys[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		for(int i = 0 ; i < limit ; ++i) {
			if(value.equals(values[i])) {
				return true;
			}
		}
		return false;
		
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public V get(Object key) {
		for(int i = 0 ; i < limit ; ++i) {
			if(key.equals(keys[i])) {
				return (V) values[i];
			}
		}
		return null;
	}

	@Override
	public boolean isEmpty() {
		return limit == 0;
	}

	@Override
	public Set<K> keySet() {
		throw new UnsupportedOperationException();	
	}

	@Override
	public V put(K key, V value) {
		ensureCapacity();
		keys[limit] = key;
		values[limit++] = value;
		return value;
	}

	private void ensureCapacity() {
		if(limit == size) {
			doubleCapacity();
		}
	}

	private void doubleCapacity() {
		int newSize = size * 2;
		Object[] newKeys = new Object[newSize];
		Object[] newValues = new Object[newSize];
		System.arraycopy(keys, 0, newKeys, 0, size);
		System.arraycopy(values, 0, newValues, 0, size);
		keys = newKeys;
		values = newValues;
		size = newSize;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for(Entry<? extends K, ? extends V> entry : m.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return limit;
	}

	@Override
	public Collection<V> values() {
		throw new UnsupportedOperationException();
	}
}
