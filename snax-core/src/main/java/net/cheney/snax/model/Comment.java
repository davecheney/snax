package net.cheney.snax.model;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Comment extends ValueNode {

	public Comment(@Nonnull String value) {
		super(value);
	}

	@Override
	public Type type() {
		return Type.COMMENT;
	}
	
	@Override
	Comment detach() {
		return this;
	}
	
	@Override
	public boolean equals(Object that) {
		if(that instanceof Comment) {
			return this.value().equals((((Comment)that).value()));
		} 
		return false;
	}

	@Override
	public void accept(@Nonnull Visitor visitor) throws IOException {
		visitor.visit(this);
	}
	
	@Override
	public int hashCode() {
		return value().hashCode();
	}
	
}
