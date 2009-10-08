package net.cheney.snax.parser;

import javax.annotation.Nonnull;

public final class XMLParser {

	enum Mode { CHARACTERS, STAG_NAME_START, DECLARATION_START, 
		PROCESSING_INSTRUCTION_START, STAG_NAME, COMMENT_START, 
		COMMENT, ATTRIBUTE_NAME_START, ATTRIBUTE_NAME, EQUALS_START, 
		ATTRIBUTE_VALUE_START, ATTRIBUTE_VALUE_APOS, ATTRIBUTE_VALUE_QUOT, 
		ELEMENT_EMPTY_END, ETAG_NAME_START, ETAG_NAME, 
		ELEMENT_END, PROCESSING_INSTRUCTION, PROCESSSING_INSTRUCTION_ATTRIBUTE_NAME_START, 
		PROCESSING_INSTRUCTION_END, PROCESSING_INSTRUCTION_ATTRIBUTE_NAME, 
		PROCESSING_INSTRUCTION_CHARS, DECLARATION, CDATA_START, CDATA, CDATA_END_1, CDATA_END_2 }

	public interface EventHandler {
		
		void doElementEnd();

		void doAttributeValue(CharSequence seq);

		void doAttributeName(CharSequence seq);

		void doElementStart(CharSequence seq);

		void doComment(CharSequence seq);

		void doCharacters(CharSequence seq);

		void doProcessingInstruction(CharSequence seq);

		void doProcessingInstructionEnd();

	}
	
	private final EventHandler handler;
	
	int offset, length = 0;
	
	State CHARACTERS = new State() {
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
	
	State STAG_NAME_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isNameStartChar(c)) {
				++length;
				return STAG_NAME;
			} else if (c == '!') {
				offset += length + 1;
				length = 0;
				return DECLARATION_START;
			} else if (c == '?') {
				offset += length + 1;
				length = 0;
				return PROCESSING_INSTRUCTION_START;
			} else if (c == '/') {
				offset += length + 1;
				length = 0;
				return ETAG_NAME_START;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, STAG_NAME_START));
			}
		}
	};
	
	State DECLARATION_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if(c == '[') {
				offset += length + 1;
				length = 0;
				return CDATA_START;
			} else if(isNameChar(c)) {
				++length;
				return DECLARATION;
			} else {
				throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, DECLARATION_START));
			}
		}
	};
	
	State DECLARATION = new State() {
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if(c == '>') {
				offset += length + 1;
				length = 0;
				return CHARACTERS;
			} else if(isChar(c)) {
				++length;
				return this;
			} else {
				throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, CHARACTERS));
			}
		}
	};
	
	State CDATA_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '[') {
				offset += length + 1;
				length = 0;
				return CDATA;
			} else {
				++length; // consume chars up to the 2nd [
				return this;
			}
		}
	};
	
	State CDATA = new State() {
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
	
	State CDATA_END_1 = new State() {
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
	
	State CDATA_END_2 = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '>') {
				handler.doCharacters(seq.subSequence(offset, offset
						+ length - 2));
				offset += length + 1;
				length = 0;
				return CHARACTERS;
			} else {
				++length;
				return CDATA;
			}
		}
	};
	
	State ETAG_NAME_START = new State() {
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
	
	State ETAG_NAME = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isNameChar(c)) {
				// consume
				++length;
				return this;
			} else if (isWhitespace(c)) {
				offset += length + 1;
				length = 0;
				return ELEMENT_END;
			} else if (c == '>') {
				handler.doElementEnd();
				offset += length + 1;
				length = 0;
				return CHARACTERS;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ETAG_NAME));
			}
		}
	};
	
	State STAG_NAME = new State() {
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
				offset += length + 1;
				length = 0;
				return ATTRIBUTE_NAME_START;
			} else if (c == '>') {
				handler.doElementStart(seq.subSequence(offset, offset
						+ length));
				offset += length + 1;
				length = 0;
				return CHARACTERS;
			} else if (c == '/') {
				handler.doElementStart(seq.subSequence(offset, offset
						+ length));
				offset += length + 1;
				length = 0;
				return ELEMENT_EMPTY_END;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, STAG_NAME));
			}
		}
	};
	
	State ATTRIBUTE_NAME_START = new State() {
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isNameChar(c)) {
				++length;
				return ATTRIBUTE_NAME;
			} else if (isWhitespace(c)) {
				// skip
				offset += length + 1;
				length = 0;
				return this;
			} else if (c == '/') {
				offset += length + 1;
				length = 0;
				return ELEMENT_EMPTY_END;
			} else if (c == '>') {
				// do not close the element, here, it is closed on the
				// corresponding </element>
				// this.handler.doElementEnd();
				offset += length + 1;
				length = 0;
				return CHARACTERS;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ATTRIBUTE_NAME_START));
			}
		}
	};
	
	State ELEMENT_END = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '>') {
				handler.doElementEnd();
				offset += length + 1;
				length = 0;
				return CHARACTERS;
			} else if (isWhitespace(c)) {
				// skip
				offset += length + 1;
				length = 0;
				return this;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ELEMENT_END));
			}
		}
	};
	
	State ELEMENT_EMPTY_END = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '>') {
				handler.doElementEnd();
				offset += length + 1;
				length = 0;
				return CHARACTERS;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ELEMENT_EMPTY_END));
			}
		}
	};
	
	State ATTRIBUTE_NAME = new State() {
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
				offset += length + 1;
				length = 0;
				return EQUALS_START;
			} else if (c == '=') {
				handler.doAttributeName(seq.subSequence(offset, offset
						+ length));
				offset += length + 1;
				length = 0;
				return ATTRIBUTE_VALUE_START;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ATTRIBUTE_NAME));
			}
		}
	};
	
	State EQUALS_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isWhitespace(c)) {
				// skip
				offset += length + 1;
				length = 0;
				return this;
			} else if (c == '=') {
				offset += length + 1;
				length = 0;
				return ATTRIBUTE_VALUE_START;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, EQUALS_START));
			}
		}
	};
	
	State ATTRIBUTE_VALUE_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (isWhitespace(c)) {
				// skip
				offset += length + 1;
				length = 0;
				return this;
			} else if (c == '\'') {
				offset += length + 1;
				length = 0;
				return ATTRIBUTE_VALUE_APOS;
			} else if (c == '"') {
				offset += length + 1;
				length = 0;
				return ATTRIBUTE_VALUE_QUOT;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, ATTRIBUTE_VALUE_START));
			}
		}
	};
	
	State ATTRIBUTE_VALUE_APOS = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '\'') {
				handler.doAttributeValue(seq.subSequence(offset, offset
						+ length));
				offset += length + 1;
				length = 0;
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
	
	State ATTRIBUTE_VALUE_QUOT = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '\"') {
				handler.doAttributeValue(seq.subSequence(offset, offset
						+ length));
				offset += length + 1;
				length = 0;
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
	
	State PROCESSING_INSTRUCTION_START = new State() {
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
	
	State PROCESSING_INSTRUCTION = new State() {
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
	
	State PROCESSING_INSTRUCTION_CHARS = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '?') {
				handler.doProcessingInstruction(seq.subSequence(offset, offset
						+ length));
				offset += length + 1;
				length = 0;
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
	
	State PROCESSING_INSTRUCTION_END = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			if (c == '>') {
				offset += length + 1;
				length = 0;
				return CHARACTERS;
			} else {
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, PROCESSING_INSTRUCTION_END));
			}
		}
	};
	
	State COMMENT_START = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			switch (c) {
			case '-':
				offset += length + 1;
				length = 0;
				return COMMENT;

			default:
				throw new IllegalArgumentException(String.format(
						"'%s' is not allowed in %s", c, COMMENT_START));
			}
		}
	};
	
	State COMMENT = new State() {
		@Override
		public State parse(CharSequence seq) {
			char c = seq.charAt(offset + length);
			switch (c) {
			case '>':
				handler.doComment(seq.subSequence(offset, offset + length));
				offset += length + 1;
				length = 0;
				return CHARACTERS;
			default:
				++length;
				return this;
			}
		}
	};
	
	public XMLParser(@Nonnull EventHandler handler) {
		this.handler = handler;
	}

	State state = CHARACTERS;
	
	public void parse(@Nonnull CharSequence seq) {
		int max = seq.length();
		for(offset = 0, length = 0 ; offset + length < max ; ) {
			state = state.parse(seq);
		}
	}

	abstract class State {
		
		protected final boolean isNameStartChar(char c) {
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

		public abstract State parse(CharSequence seq);

		protected final boolean isNameChar(char c) {
			return (isNameStartChar(c) || c == '-' || c == '.'
					|| (c >= '0' && c <= '9') || c == '\u00B7'
					|| (c >= '\u0300' && c <= '\u036F') || (c >= '\u023F' && c <= '\u2040'));
		}

		protected final boolean isWhitespace(char c) {
			return (c == ' ' || c == '\n' || c == '\r' || c == '\t');
		}

		protected final boolean isChar(char c) {
			return (c == '\t' || c == '\r' || c == '\n'
					|| (c >= '\u0020' && c <= '\uD7FF') || (c >= '\uE000' && c <= '\uFFFD'));
		}
	}
	
}
