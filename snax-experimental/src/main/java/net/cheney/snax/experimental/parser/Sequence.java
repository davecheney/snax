package net.cheney.snax.experimental.parser;

public abstract class Sequence {

	/**
	 * @return the next character in this sequence
	 * 
	 * @throws IllegalStateException if next() is called when hasNext() is false
	 */
	public abstract char next();
	
	/**
	 * 
	 * @return true if there are more characters in this sequence, false if there are no more characters in this sequence
	 */
	public abstract boolean hasNext();
	
	/**
	 * Mark this position in the sequence
	 */
	public abstract void mark();
}
