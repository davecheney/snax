package net.cheney.snax.parser;

import javax.annotation.Nonnull;

public final class XMLParser {
	
	protected State CHARACTERS = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			switch(c) { 
			case '<':
				handler.doCharacters(seq.subSequence(offset, offset + length));
				offset += length + 1;
				length = 0;
				return STAG_NAME_START;

			default:
				++length;
				return this;
			}
		}
	};
	
	protected State STAG_NAME_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isNameStartChar(c)) {
				++length;
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
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, STAG_NAME_START));
			}
		}
	};
	
	protected State DECLARATION_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if(c == '[') {
				incrementOffsetAndResetLength();
				return CDATA_START;
			} else if(isNameChar(c)) {
				++length;
				return DECLARATION;
			} else {
				throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, DECLARATION_START));
			}
		}
	};
	
	protected State DECLARATION = new State() {
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if(c == '>') {
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else if(isChar(c)) {
				++length;
				return this;
			} else {
				throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, CHARACTERS));
			}
		}
	};
	
	protected State CDATA_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '[') {
				incrementOffsetAndResetLength();
				return CDATA;
			} else {
				++length; // consume chars up to the 2nd [
				return this;
			}
		}
	};
	
	protected State CDATA = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == ']') {
				++length;
				return CDATA_END_1;
			} else {
				++length;
				return this;
			}
		}
	};
	
	protected State CDATA_END_1 = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == ']') {
				++length;
				return CDATA_END_2;
			} else {
				++length;
				return CDATA;
			}
		}
	};
	
	protected State CDATA_END_2 = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '>') {
				handler.doCharacters(seq.subSequence(offset, offset
						+ length - 2));
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else {
				++length;
				return CDATA;
			}
		}
	};
	
	protected State ETAG_NAME_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isNameStartChar(c)) {
				++length;
				return ETAG_NAME;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ETAG_NAME_START));
			}
		}
	};
	
	protected State ETAG_NAME = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isNameChar(c)) {
				// consume
				++length;
				return this;
			} else if (isWhitespace(c)) {
				incrementOffsetAndResetLength();
				return ELEMENT_END;
			} else if (c == '>') {
				handler.doElementEnd();
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ETAG_NAME));
			}
		}
	};
	
	protected State STAG_NAME = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isNameChar(c)) {
				// consume
				++length;
				return this;
			} else if (isWhitespace(c)) {
				handler.doElementStart(seq.subSequence(offset, offset
						+ length));
				incrementOffsetAndResetLength();
				return ATTRIBUTE_NAME_START;
			} else if (c == '>') {
				handler.doElementStart(seq.subSequence(offset, offset
						+ length));
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else if (c == '/') {
				handler.doElementStart(seq.subSequence(offset, offset
						+ length));
				incrementOffsetAndResetLength();
				return ELEMENT_EMPTY_END;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, STAG_NAME));
			}
		}
	};
	
	protected State ATTRIBUTE_NAME_START = new State() {
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isNameChar(c)) {
				++length;
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
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ATTRIBUTE_NAME_START));
			}
		}
	};
	
	protected State ELEMENT_END = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '>') {
				handler.doElementEnd();
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else if (isWhitespace(c)) {
				// skip
				incrementOffsetAndResetLength();
				return this;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ELEMENT_END));
			}
		}
	};
	
	protected State ELEMENT_EMPTY_END = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '>') {
				handler.doElementEnd();
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ELEMENT_EMPTY_END));
			}
		}
	};
	
	protected State ATTRIBUTE_NAME = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isNameChar(c)) {
				// consume!
				++length;
				return this;
			} else if (isWhitespace(c)) {
				handler.doAttributeName(seq.subSequence(offset, offset
						+ length));
				incrementOffsetAndResetLength();
				return EQUALS_START;
			} else if (c == '=') {
				handler.doAttributeName(seq.subSequence(offset, offset
						+ length));
				incrementOffsetAndResetLength();
				return ATTRIBUTE_VALUE_START;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ATTRIBUTE_NAME));
			}
		}
	};
	
	protected State EQUALS_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isWhitespace(c)) {
				// skip
				incrementOffsetAndResetLength();
				return this;
			} else if (c == '=') {
				incrementOffsetAndResetLength();
				return ATTRIBUTE_VALUE_START;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, EQUALS_START));
			}
		}
	};
	
	protected State ATTRIBUTE_VALUE_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
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
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ATTRIBUTE_VALUE_START));
			}
		}
	};
	
	protected State ATTRIBUTE_VALUE_APOS = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '\'') {
				handler.doAttributeValue(seq.subSequence(offset, offset
						+ length));
				incrementOffsetAndResetLength();
				return ATTRIBUTE_NAME_START;
			} else if (isChar(c)) {
				++length;
				return this;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ATTRIBUTE_VALUE_APOS));
			}
		}
	};
	
	protected State ATTRIBUTE_VALUE_QUOT = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '\"') {
				handler.doAttributeValue(seq.subSequence(offset, offset
						+ length));
				incrementOffsetAndResetLength();
				return ATTRIBUTE_NAME_START;
			} else if (isChar(c)) {
				++length;
				return this;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ATTRIBUTE_VALUE_QUOT));
			}
		}
	};
	
	protected State PROCESSING_INSTRUCTION_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isNameChar(c)) {
				++length;
				return PROCESSING_INSTRUCTION;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, PROCESSING_INSTRUCTION_START));
			}
		}
	};
	
	protected State PROCESSING_INSTRUCTION = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isNameChar(c)) {
				++length;
				return this;
			} else if (isWhitespace(c)) {
				++length;
				return PROCESSING_INSTRUCTION_CHARS;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, PROCESSING_INSTRUCTION));
			}
		}
	};
	
	protected State PROCESSING_INSTRUCTION_CHARS = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '?') {
				handler.doProcessingInstruction(seq.subSequence(offset, offset
						+ length));
				incrementOffsetAndResetLength();
				return PROCESSING_INSTRUCTION_END;
			} else if (isChar(c)) {
				++length;
				return this;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, PROCESSING_INSTRUCTION_CHARS));
			}
		}
	};
	
	protected State PROCESSING_INSTRUCTION_END = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '>') {
				incrementOffsetAndResetLength();
				return CHARACTERS;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, PROCESSING_INSTRUCTION_END));
			}
		}
	};
	
	protected State COMMENT_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			switch (c) {
			case '-':
				incrementOffsetAndResetLength();
				return COMMENT;

			default:
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, COMMENT_START));
			}
		}
	};
	
	protected State COMMENT = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			switch (c) {
			case '>':
				handler.doComment(seq.subSequence(offset, offset + length));
				incrementOffsetAndResetLength();
				return CHARACTERS;
			default:
				++length;
				return this;
			}
		}
	};

	protected State state = CHARACTERS;
	
	protected final ContentHandler handler;
	
	protected int offset, length = 0;
	
	public XMLParser(@Nonnull ContentHandler handler) {
		this.handler = handler;
	}

	protected void incrementOffsetAndResetLength() {
		offset += length + 1;
		length = 0;
	}
	
	public void parse(@Nonnull CharSequence seq) {
		int max = seq.length();
		for(offset = 0, length = 0 ; offset + length < max ; ) {
			state = state.parse(seq);
		}
	}
	
}
