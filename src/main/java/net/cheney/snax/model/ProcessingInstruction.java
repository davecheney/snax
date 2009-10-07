package net.cheney.snax.model;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class ProcessingInstruction extends Node {

	public static final Node XML_DECLARATION = new ProcessingInstruction("xml", "version=\"1.0\" encoding=\"utf-8\""); // hack
	
	private final String target;
	private final String data;

	public ProcessingInstruction(@Nonnull String target, @Nonnull String data) {
		this.target = target;
		this.data = data;
	}
	
	@Override
	public Type type() {
		return Type.PROCESSING_INSTRUCTION;
	}
	
	public String target() {
		return target;
	}
	
	public String data() {
		return data;
	}

	@Override
	protected ProcessingInstruction detach() {
		return this;
	}

	@Override
	public void accept(@Nonnull Visitor visitor) throws IOException {
		visitor.visit(this);
	}

}
