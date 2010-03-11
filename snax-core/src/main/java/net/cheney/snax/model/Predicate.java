package net.cheney.snax.model;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.AbstractIterator;

@Immutable
abstract class Predicate<T> {

	protected abstract boolean apply(T t);

	public final Filter<T> filter(@Nonnull final Iterable<T> iterable) {
		return new Filter<T>(iterable);
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
	
	public final class Filter<V> implements Iterable<T> {
		
		final Iterable<T> iterable;

		public Filter(Iterable<T> iterable) {
			this.iterable = iterable;
		}

		@Override
		public Iterator<T> iterator() {
			return new AbstractIterator<T>() {

				private final Iterator<T> unfiltered = iterable.iterator();
				
				@Override
				protected T computeNext() {
			        while (unfiltered.hasNext()) {
			            T element = unfiltered.next();
			            if (apply(element)) {
			              return element;
			            }
			          }
			          return endOfData();
				}
			};
		}
		
		public T first() {
			return iterator().next();
		}
	}
}
