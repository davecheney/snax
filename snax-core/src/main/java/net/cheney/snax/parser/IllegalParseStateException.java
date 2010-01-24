package net.cheney.snax.parser;

public class IllegalParseStateException extends IllegalArgumentException {

	private static final long serialVersionUID = -1464623700570596183L;

	public IllegalParseStateException(char c, State state) {
		super(String.format("'%s' is not allowed in %s", c, state.toString()));
	}
}
