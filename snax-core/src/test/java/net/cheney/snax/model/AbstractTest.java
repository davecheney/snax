package net.cheney.snax.model;

import java.util.Collection;
import java.util.List;

import net.cheney.snax.model.Predicate;

import com.google.common.collect.Lists;

public abstract class AbstractTest {

	protected Predicate<Integer> evens() {
		return new Predicate<Integer>() {
			
			@Override
			protected boolean apply(Integer t) {
				switch(t) {
					case 0: return true;
					case 1: return false;
					default: return (t % 2) == 0;
				}
			}
		};
	}
	
	protected Predicate<Integer> odds() {
		return new Predicate<Integer>() {
			
			@Override
			protected boolean apply(Integer t) {
				switch(t) {
					case 0: return false;
					case 1: return true;
					default: return (t % 2) == 1;
				}
			}
		};
	}
	
	protected Collection<Integer> sequence(int start, int end) {
		List<Integer> col = Lists.newArrayList();
		for(int i = start ; i < end + 1 ; i++) {
			col.add(i);
		}
		return col;
	}
}
