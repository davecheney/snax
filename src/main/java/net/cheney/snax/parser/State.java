package net.cheney.snax.parser;

abstract class State {
	
	static final boolean[] NAME_START_CHARS = new boolean[Character.MAX_VALUE], NAME_CHARS = new boolean[Character.MAX_VALUE], CHARS = new boolean[Character.MAX_VALUE];
	
	static {
		for(char c = 0 ; c < Character.MAX_VALUE ; ++c) {
			NAME_START_CHARS[c] = isNameStartChar0(c);
		}
		
		for(char c = 0 ; c < Character.MAX_VALUE ; ++c) {
			NAME_CHARS[c] = isNameChar0(c);
		}
		
		for(char c = 0 ; c < Character.MAX_VALUE ; ++c) {
			CHARS[c] = isChar0(c);
		}
	}
	
	protected final boolean isNameStartChar(char c) {
		return NAME_START_CHARS[c];
	}
	
	private static boolean isNameStartChar0(char c) {
		return (c == ':' || (c >= 'A' && c <= 'Z') || c == '_'
				|| (c >= 'a' && c <= 'z')
				|| (c >= '\u00C0' && c <= '\u00D6')
				|| (c >= '\u00D8' && c <= '\u00F6')
				|| (c >= '\u00F8' && c <= '\u02FF')
				|| (c >= '\u0370' && c <= '\u037D')
				|| (c >= '\u037F' && c <= '\u1FFF')
				|| (c >= '\u200C' && c <= '\u200D')
				|| (c >= '\u2070' && c <= '\u218F')
				|| (c >= '\u2C00' && c <= '\u2FEF')
				|| (c >= '\u3001' && c <= '\uD7FF')
				|| (c >= '\uF900' && c <= '\uFDCF') || (c >= '\uFDF0' && c <= '\uFFFD'));
	}


	protected final boolean isNameChar(char c) {
		return NAME_CHARS[c];
	}
	
	private static boolean isNameChar0(char c) {
		return (isNameStartChar0(c) || c == '-' || c == '.'
				|| (c >= '0' && c <= '9') || c == '\u00B7'
				|| (c >= '\u0300' && c <= '\u036F') || (c >= '\u023F' && c <= '\u2040'));
	}

	abstract State parse(char c, CharSequence seq);

	protected final boolean isWhitespace(char c) {
		return (c == ' ' || c == '\n' || c == '\r' || c == '\t');
	}
	
	protected final boolean isChar(char c) {
		return CHARS[c];
	}

	private static boolean isChar0(char c) {
		return (c == '\t' || c == '\r' || c == '\n'
				|| (c >= '\u0020' && c <= '\uD7FF') || (c >= '\uE000' && c <= '\uFFFD'));
	}
	
}
