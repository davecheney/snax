package net.cheney.snax.parser;

import javax.annotation.Nonnull;

public final class XMLParser {
	
	final State CHARACTERS = new State() {
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
	
	final State STAG_NAME_START = new State() {
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
	
	final State DECLARATION_START = new State() {
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
	
	final State DECLARATION = new State() {
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
	
	final State CDATA_START = new State() {
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
	
	final State CDATA = new State() {
		@Override
		State parse(char c) {
			if (c == ']') {
				return CDATA_END_1;
			} else {
				return this;
			}
		}
	};
	
	final State CDATA_END_1 = new State() {
		@Override
		State parse(char c) {
			if (c == ']') {
				return CDATA_END_2;
			} else {
				return CDATA;
			}
		}
	};
	
	final State CDATA_END_2 = new State() {
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
	
	final State ETAG_NAME_START = new State() {
		@Override
		State parse(char c) {
			if (isNameStartChar(c)) {
				return ETAG_NAME;
			} else {
				throw new IllegalParseStateException(c, ETAG_NAME_START);
			}
		}
	};
	
	final State ETAG_NAME = new State() {
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
	
	final State STAG_NAME = new State() {
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
	
	final State ATTRIBUTE_NAME_START = new State() {
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
	
	final State ELEMENT_END = new State() {
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
	
	final State ELEMENT_EMPTY_END = new State() {
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
	
	final State ATTRIBUTE_NAME = new State() {
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
	
	final State EQUALS_START = new State() {
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
	
	final State ATTRIBUTE_VALUE_START = new State() {
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
	
	final State ATTRIBUTE_VALUE_APOS = new State() {
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
	
	final State ATTRIBUTE_VALUE_QUOT = new State() {
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
	
	final State PROCESSING_INSTRUCTION_START = new State() {
		@Override
		State parse(char c) {
			if (isNameChar(c)) {
				return PROCESSING_INSTRUCTION;
			} else {
				throw new IllegalParseStateException(c, PROCESSING_INSTRUCTION_START);
			}
		}
	};
	
	final State PROCESSING_INSTRUCTION = new State() {
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
	
	final State PROCESSING_INSTRUCTION_CHARS = new State() {
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
	
	final State PROCESSING_INSTRUCTION_END = new State() {
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
	
	final State COMMENT_START = new State() {
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
	
	final State COMMENT = new State() {
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
	
	private int offset, limit = 0;

	private CharSequence sequence;
	
	public XMLParser(@Nonnull ContentHandler handler) {
		this.handler = handler;
	}

	void incrementOffsetAndResetLength() {
		offset = limit;
		offset++;
	}
	
	CharSequence subsequence() {
		return sequence.subSequence(offset, limit);
	}
	
	public void parse(@Nonnull CharSequence seq) {
		int max = seq.length();
		// Yank state into a stack local, reduces benchmark by 10%
		State currentState = this.state;
		// make seq available to the subsequence method without making offset and limit visible
		this.sequence = seq;
		for(offset = 0, limit = 0 ; limit < max ; ++limit ) {
			currentState = currentState.parse(seq.charAt(limit));
		}
		this.state = currentState;
	}
	
}
