package net.cheney.snax.model;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
abstract class ValueNode extends Node {

	private final String value;

	ValueNode(@Nonnull String value) {
		this.value = value;
	}
	
	public final String value() {
		return this.value;
	}
	
	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

}
