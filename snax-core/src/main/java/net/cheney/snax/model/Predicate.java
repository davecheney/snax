package net.cheney.snax.model;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.AbstractIterator;

@Immutable
abstract class Predicate<T> {

	protected abstract boolean apply(T t);

	public final Filter<T> filter(@Nonnull final Iterable<T> iterable) {
		return new Filter<T>(iterable, this);
	}

	public final boolean any(@Nonnull Iterable<T> iterable) {
		return any(iterable.iterator());
	}

	private boolean any(@Nonnull Iterator<T> i) {
		while (i.hasNext()) {
			if (apply(i.next())) {
				return true;
			}
		}
		return false;
	}

	public final <V extends T> V first(@Nonnull Iterable<V> iterable) {
		return first(iterable.iterator());
	}

	private <V extends T> V first(@Nonnull Iterator<V> i) {
		while (i.hasNext()) {
			V element = i.next();
			if (apply(element)) {
				return element;
			}
		}
		return null;
	}
	
	public static final class Filter<V> implements Iterable<V> {
		
		final Iterable<V> iterable;
		final Predicate<V> predicate;

		public Filter(Iterable<V> iterable, Predicate<V> predicate) {
			this.iterable = iterable;
			this.predicate = predicate;
		}

		@Override
		public Iterator<V> iterator() {
			return new AbstractIterator<V>() {

				private final Iterator<V> unfiltered = iterable.iterator();
				
				@Override
				protected V computeNext() {
			        while (unfiltered.hasNext()) {
			            V element = unfiltered.next();
			            if (predicate.apply(element)) {
			              return element;
			            }
			          }
			          return endOfData();
				}
			};
		}
		
		public V first() {
			return iterator().next();
		}
	}
}
