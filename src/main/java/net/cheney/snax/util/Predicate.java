package net.cheney.snax.util;

import java.util.Iterator;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.AbstractIterator;

@Immutable
public abstract class Predicate<T> {

	protected abstract boolean apply(T t);

	public final Iterable<T> filter(@Nonnull final Iterable<T> iterable) {
		return new Iterable<T>() {

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
		};
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
}
