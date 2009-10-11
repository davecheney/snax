package net.cheney.snax.parser;

import javax.annotation.Nonnull;

public final class XMLParser {
	
	protected final State CHARACTERS = new State() {
		@Override
		State parse(char c) {
			if(c == '<') {
				handler.doCharacters(subsequence());
				incrementOffsetAndResetLength();
				return STAG_NAME_START;
			} else {
				return this;
			}
		}
	};
	
	protected final State STAG_NAME_START = new State() {
		@Override
		State parse(char c) {
			if (isNameStartChar(c)) {
				return STAG_NAME;
			} else if (c == '!') {
				incrementOffsetAndResetLength();
				return DECLARATION_START;
			} else if (c == '?') {
				incrementOffsetAndResetLength();
				return PROCESSING_INSTRUCTION_START;
			} else if (c == '/') {
				incrementOffsetAndResetLength();
				return ETAG_NAME_START;
			} else {
				throw new IllegalParseStateException(c, STAG_NAME_START);
			}
		}
	};
	
	protected final State DECLARATION_START = new State() {
		@Override
		State parse(char c) {
			if(c == '[') {
				incrementOffsetAndResetLength();
				return CDATA_START;
			} else if(isNameChar(c)) {
				return DECLARATION;
			} else {
				throw new IllegalParseStateException(c, DECLARATION_START);
			}
		}
	};
	
	protected final State DECLARATION = new State() {
		@Override
		State parse(char c) {
			if(c == '>') {
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else if(isChar(c)) {
				return this;
			} else {
				throw new IllegalParseStateException(c, CHARACTERS);
			}
		}
	};
	
	protected final State CDATA_START = new State() {
		@Override
		State parse(char c) {
			if (c == '[') {
				incrementOffsetAndResetLength();
				return CDATA;
			} else {
				// consume chars up to the 2nd [
				return this;
			}
		}
	};
	
	protected final State CDATA = new State() {
		@Override
		State parse(char c) {
			if (c == ']') {
				return CDATA_END_1;
			} else {
				return this;
			}
		}
	};
	
	protected final State CDATA_END_1 = new State() {
		@Override
		State parse(char c) {
			if (c == ']') {
				return CDATA_END_2;
			} else {
				return CDATA;
			}
		}
	};
	
	protected final State CDATA_END_2 = new State() {
		@Override
		State parse(char c) {
			if (c == '>') {
				CharSequence s = subsequence();
				handler.doCharacters(s.subSequence(0, s.length() - 2));
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else {
				return CDATA;
			}
		}
	};
	
	protected final State ETAG_NAME_START = new State() {
		@Override
		State parse(char c) {
			if (isNameStartChar(c)) {
				return ETAG_NAME;
			} else {
				throw new IllegalParseStateException(c, ETAG_NAME_START);
			}
		}
	};
	
	protected final State ETAG_NAME = new State() {
		@Override
		State parse(char c) {
			if (isNameChar(c)) {
				// consume
				return this;
			} else if (isWhitespace(c)) {
				incrementOffsetAndResetLength();
				return ELEMENT_END;
			} else if (c == '>') {
				handler.doElementEnd();
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else {
				throw new IllegalParseStateException(c, ETAG_NAME);
			}
		}
	};
	
	protected final State STAG_NAME = new State() {
		@Override
		State parse(char c) {
			if (isNameChar(c)) {
				// consume
				return this;
			} else if (isWhitespace(c)) {
				handler.doElementStart(subsequence());
				incrementOffsetAndResetLength();
				return ATTRIBUTE_NAME_START;
			} else if (c == '>') {
				handler.doElementStart(subsequence());
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else if (c == '/') {
				handler.doElementStart(subsequence());
				incrementOffsetAndResetLength();
				return ELEMENT_EMPTY_END;
			} else {
				throw new IllegalParseStateException(c, STAG_NAME);
			}
		}
	};
	
	protected final State ATTRIBUTE_NAME_START = new State() {
		@Override
		State parse(char c) {
			if (isNameChar(c)) {
				return ATTRIBUTE_NAME;
			} else if (isWhitespace(c)) {
				// skip
				incrementOffsetAndResetLength();
				return this;
			} else if (c == '/') {
				incrementOffsetAndResetLength();
				return ELEMENT_EMPTY_END;
			} else if (c == '>') {
				// do not close the element, here, it is closed on the
				// corresponding </element>
				// this.handler.doElementEnd();
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else {
				throw new IllegalParseStateException(c, ATTRIBUTE_NAME_START);
			}
		}
	};
	
	protected final State ELEMENT_END = new State() {
		@Override
		State parse(char c) {
			if (c == '>') {
				handler.doElementEnd();
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else if (isWhitespace(c)) {
				// skip
				incrementOffsetAndResetLength();
				return this;
			} else {
				throw new IllegalParseStateException(c, ELEMENT_END);
			}
		}
	};
	
	protected final State ELEMENT_EMPTY_END = new State() {
		@Override
		State parse(char c) {
			if (c == '>') {
				handler.doElementEnd();
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else {
				throw new IllegalParseStateException(c, ELEMENT_EMPTY_END);
			}
		}
	};
	
	protected final State ATTRIBUTE_NAME = new State() {
		@Override
		State parse(char c) {
			if (isNameChar(c)) {
				// consume!
				return this;
			} else if (isWhitespace(c)) {
				handler.doAttributeName(subsequence());
				incrementOffsetAndResetLength();
				return EQUALS_START;
			} else if (c == '=') {
				handler.doAttributeName(subsequence());
				incrementOffsetAndResetLength();
				return ATTRIBUTE_VALUE_START;
			} else {
				throw new IllegalParseStateException(c, ATTRIBUTE_NAME);
			}
		}
	};
	
	protected final State EQUALS_START = new State() {
		@Override
		State parse(char c) {
			if (isWhitespace(c)) {
				// skip
				incrementOffsetAndResetLength();
				return this;
			} else if (c == '=') {
				incrementOffsetAndResetLength();
				return ATTRIBUTE_VALUE_START;
			} else {
				throw new IllegalParseStateException(c, EQUALS_START);
			}
		}
	};
	
	protected final State ATTRIBUTE_VALUE_START = new State() {
		@Override
		State parse(char c) {
			if (isWhitespace(c)) {
				// skip
				incrementOffsetAndResetLength();
				return this;
			} else if (c == '\'') {
				incrementOffsetAndResetLength();
				return ATTRIBUTE_VALUE_APOS;
			} else if (c == '"') {
				incrementOffsetAndResetLength();
				return ATTRIBUTE_VALUE_QUOT;
			} else {
				throw new IllegalParseStateException(c, ATTRIBUTE_VALUE_START);
			}
		}
	};
	
	protected final State ATTRIBUTE_VALUE_APOS = new State() {
		@Override
		State parse(char c) {
			if (c == '\'') {
				handler.doAttributeValue(subsequence());
				incrementOffsetAndResetLength();
				return ATTRIBUTE_NAME_START;
			} else if (isChar(c)) {
				return this;
			} else {
				throw new IllegalParseStateException(c, ATTRIBUTE_VALUE_APOS);
			}
		}
	};
	
	protected final State ATTRIBUTE_VALUE_QUOT = new State() {
		@Override
		State parse(char c) {
			if (c == '\"') {
				handler.doAttributeValue(subsequence());
				incrementOffsetAndResetLength();
				return ATTRIBUTE_NAME_START;
			} else if (isChar(c)) {
				return this;
			} else {
				throw new IllegalParseStateException(c, ATTRIBUTE_VALUE_QUOT);
			}
		}
	};
	
	protected final State PROCESSING_INSTRUCTION_START = new State() {
		@Override
		State parse(char c) {
			if (isNameChar(c)) {
				return PROCESSING_INSTRUCTION;
			} else {
				throw new IllegalParseStateException(c, PROCESSING_INSTRUCTION_START);
			}
		}
	};
	
	protected final State PROCESSING_INSTRUCTION = new State() {
		@Override
		State parse(char c) {
			if (isNameChar(c)) {
				return this;
			} else if (isWhitespace(c)) {
				return PROCESSING_INSTRUCTION_CHARS;
			} else {
				throw new IllegalParseStateException(c, PROCESSING_INSTRUCTION);
			}
		}
	};
	
	protected final State PROCESSING_INSTRUCTION_CHARS = new State() {
		@Override
		State parse(char c) {
			if (c == '?') {
				handler.doProcessingInstruction(subsequence());
				incrementOffsetAndResetLength();
				return PROCESSING_INSTRUCTION_END;
			} else if (isChar(c)) {
				return this;
			} else {
				throw new IllegalParseStateException(c, PROCESSING_INSTRUCTION_CHARS);
			}
		}
	};
	
	protected final State PROCESSING_INSTRUCTION_END = new State() {
		@Override
		State parse(char c) {
			if (c == '>') {
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else {
				throw new IllegalParseStateException(c, PROCESSING_INSTRUCTION_END);
			}
		}
	};
	
	protected final State COMMENT_START = new State() {
		@Override
		State parse(char c) {
			if(c == '-') {
				incrementOffsetAndResetLength();
				return COMMENT;
			} else {
				throw new IllegalParseStateException(c, COMMENT_START);
			}
		}
	};
	
	protected final State COMMENT = new State() {
		@Override
		State parse(char c) {
			if(c == '>') { 
				handler.doComment(subsequence());
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else {
				return this;
			}
		}
	};

	private State state = CHARACTERS;
	
	protected final ContentHandler handler;
	
	private int offset, length = 0;

	private CharSequence sequence;
	
	public XMLParser(@Nonnull ContentHandler handler) {
		this.handler = handler;
	}

	final void incrementOffsetAndResetLength() {
		offset += ++length;
		length = -1;
	}
	
	final CharSequence subsequence() {
		return sequence.subSequence(offset, offset + length);
	}
	
	public void parse(@Nonnull CharSequence seq) {
		int max = seq.length();
		// Yank state into a stack local, reduces benchmark by 10%
		State currentState = this.state;
		this.sequence = seq;
		for(offset = 0, length = 0 ; offset + length < max ; ++length ) {
			currentState = currentState.parse(seq.charAt(offset + length));
		}
		this.state = currentState;
	}
	
}
