package net.cheney.snax.experimental.parser;

import net.cheney.snax.model.*;
import net.cheney.snax.parser.IllegalParseStateException;
import net.cheney.snax.parser.State;
import net.cheney.snax.parser.XMLParser;

public class Parser {
	
	enum State {
		CHARACTERS, STAG_NAME_START, DECLARATION_START, DECLARATION, CDATA_START, 
		CDATA, CDATA_END_1, CDATA_END_2, ETAG_NAME_START, ETAG_NAME, STAG_NAME, 
		ATTRIBUTE_NAME_START, ELEMENT_END, ELEMENT_EMPTY_END, ATTRIBUTE_NAME, 
		EQUALS_START, ATTRIBUTE_VALUE_START, ATTRIBUTE_VALUE_APOS, ATTRIBUTE_VALUE_QUOT, 
		PROCESSING_INSTRUCTION_START, PROCESSING_INSTRUCTION, PROCESSING_INSTRUCTION_CHARS, 
		PROCESSING_INSTRUCTION_END, COMMENT_START, COMMENT }

	private State state = State.CHARACTERS;
	
	private static final boolean[] NAME_START_CHARS = new boolean[Character.MAX_VALUE], NAME_CHARS = new boolean[Character.MAX_VALUE], CHARS = new boolean[Character.MAX_VALUE];
	
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
	
	protected boolean isNameStartChar(char c) {
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


	protected boolean isNameChar(char c) {
		return NAME_CHARS[c];
	}
	
	private static boolean isNameChar0(char c) {
		return (isNameStartChar0(c) || c == '-' || c == '.'
				|| (c >= '0' && c <= '9') || c == '\u00B7'
				|| (c >= '\u0300' && c <= '\u036F') || (c >= '\u023F' && c <= '\u2040'));
	}

	abstract State parse(char c, XMLParser parser);

	protected boolean isWhitespace(char c) {
		return (c == ' ' || c == '\n' || c == '\r' || c == '\t');
	}
	
	protected boolean isChar(char c) {
		return CHARS[c];
	}

	private static boolean isChar0(char c) {
		return (c == '\t' || c == '\r' || c == '\n'
				|| (c >= '\u0020' && c <= '\uD7FF') || (c >= '\uE000' && c <= '\uFFFD'));
	}
	
	public Element parseFragment(Reader reader) {
		Element element = null;
		while(element == null && reader.hasMore()) {
			element = parseFragment(reader.sequence());
		}
		return element;
	}
	
	private Element parseFragment(Sequence sequence) {
		State s = state;
		while(sequence.hasNext()) {
				char c = sequence.next();
				switch(state) {
				case CHARACTERS:
					if(c == '<') {
						doCharacters();
						s = State.STAG_NAME_START;
					} 
					continue;
					
				case STAG_NAME_START:
					if (isNameStartChar(c)) {
						return STAG_NAME;
					} else if (c == '!') {
						parser.incrementOffsetAndResetLength();
						return DECLARATION_START;
					} else if (c == '?') {
						parser.incrementOffsetAndResetLength();
						return PROCESSING_INSTRUCTION_START;
					} else if (c == '/') {
						parser.incrementOffsetAndResetLength();
						return ETAG_NAME_START;
					} else {
						throw new IllegalParseStateException(c, STAG_NAME_START);
					}
				
				}
			}
		}
		state = s;

	}

	private void doCharacters() {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isNameStartChar(char c) {
		// TODO Auto-generated method stub
		return false;
	}

	public Document parseDocument(Reader reader) {
		Document document = null;
		while(document == null && reader.hasMore()) {
			document = parseDocument(reader.sequence());
		}
		return document;
	}

	private Document parseDocument(Sequence sequence) {
		while(sequence.hasNext()) {
			char c = sequence.next();
		}
	}
}
