package net.cheney.snax.util;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.AbstractIterator;

@Immutable
public abstract class Predicate<T> {

	protected abstract boolean apply(T t);

	public final Filter<T> filter(@Nonnull final Iterable<T> iterable) {
		return new Filter<T>(iterable, this);
	}

	@Immutable
	public static final class Filter<V> implements Iterable<V> {
		
		final Iterable<V> iterable;
		final Predicate<V> predicate;

		public Filter(Iterable<V> iterable, Predicate<V> predicate) {
			this.iterable = iterable;
			this.predicate = predicate;
		}

		@Override
		public Iterator<V> iterator() {
			return new Iterator<V>();
		}
		
		public V first() {
			return iterator().next();
		}

		public boolean any() {
			return iterator().hasNext();
		}
		
		public final class Iterator<K> implements java.util.Iterator<V> {

			private final java.util.Iterator<V> unfiltered = iterable.iterator();

			private V next = computeNext();

			private V computeNext() {
				while (unfiltered.hasNext()) {
					V element = unfiltered.next();
					if (predicate.apply(element)) {
						return element;
					}
				}
				return null;
			}

			public final boolean hasNext() {
				return next != null;  
			}

			public final V next() {
				try {
					return next;
				} finally {
					next = computeNext();
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		}
	}
}
