package net.cheney.snax.experimental.parser;

public abstract class Reader {

	public abstract boolean hasMore();
	
	public abstract Sequence sequence();
}
