package net.cheney.snax.model;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Text extends ValueNode {

	public Text(long l) {
		this(Long.toString(l));
	}

	public Text(@Nonnull String string) {
		super(string);
	}

	@Override
	public Type type() {
		return Node.Type.TEXT;
	}

	@Override
	public boolean equals(Object that) {
		if(that instanceof Text) {
			return this.value().equals((((Text)that).value()));
		} 
		return false;
	}
	
	@Override
	public int hashCode() {
		return value().hashCode();
	}
	
	@Override
	protected Text detach() {
		return this;
	}
	
	@Override
	public void accept(@Nonnull Visitor visitor) throws IOException {
		visitor.visit(this);
	}
}
