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
	
	public XMLParser(@Nonnull EventHandler handler) {
		this.handler = handler;
	}

	public void parse(@Nonnull CharSequence seq) {
		Mode mode = Mode.CHARACTERS;
		for(int offset = 0, length = 0, max = seq.length() ; offset + length < max ; ) {
			char c = seq.charAt(offset + length);
			switch(mode) {
			case CHARACTERS:
				switch(c) { 
				case '<':
					this.handler.doCharacters(seq.subSequence(offset, offset + length));
					offset += length + 1;
					length = 0;
					mode = Mode.STAG_NAME_START;
					break;
					
				default:
					++length;
				}
				break;
				
			case STAG_NAME_START:
				if (isNameStartChar(c)) {
					mode = Mode.STAG_NAME;
					++length;
				} else if(c == '!') {
					offset += length + 1;
					length = 0;
					mode = Mode.DECLARATION_START;
				} else if (c == '?') {
					offset += length + 1;
					length = 0;
					mode = Mode.PROCESSING_INSTRUCTION_START;
				} else if (c == '/') {
					offset += length + 1;
					length = 0;
					mode = Mode.ETAG_NAME_START;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case DECLARATION_START:
				if(c == '[') {
					offset += length + 1;
					length = 0;
					mode = Mode.CDATA_START;
				} else if(isNameChar(c)) {
					mode = Mode.DECLARATION;
					++length;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;		
				
			case DECLARATION:
				if(c == '>') {
					offset += length + 1;
					length = 0;
					mode = Mode.CHARACTERS;
				} else if(isChar(c)) {
					++length;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case CDATA_START:
				if(c == '[') {
					offset += length + 1;
					length = 0;
					mode = Mode.CDATA; 
				} else {
					++length; // consume chars up to the 2nd [
				}
				break;
				
			case CDATA:
				if(c == ']') {
					++length;
					mode = Mode.CDATA_END_1;
				} else {
					++length; 
				}
				break;
				
			case CDATA_END_1:
				if(c == ']') {
					++length;
					mode = Mode.CDATA_END_2;
				} else {
					++length;
					mode = Mode.CDATA;
				}
				break;
				
			case CDATA_END_2:
				if(c == '>') {
					this.handler.doCharacters(seq.subSequence(offset, offset + length - 2));
					offset += length + 1;
					length = 0;
					mode = Mode.CHARACTERS;
					break;
				} else {
					++length;
					mode = Mode.CDATA;
				}
				break;
				
			case ETAG_NAME_START:
				if (isNameStartChar(c)) {
					mode = Mode.ETAG_NAME;
					++length;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case ETAG_NAME:
				if(isNameChar(c)) {
					// consume
					++length;
				} else if (isWhitespace(c)) {
					offset += length + 1;
					length = 0;
					mode = Mode.ELEMENT_END;
				} else if(c == '>') {
					this.handler.doElementEnd();
					offset += length + 1;
					length = 0;
					mode = Mode.CHARACTERS;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case STAG_NAME:
				if(isNameChar(c)) {
					// consume
					++length;
				} else if (isWhitespace(c)) {
					this.handler.doElementStart(seq.subSequence(offset, offset + length));
					offset += length + 1;
					length = 0;
					mode = Mode.ATTRIBUTE_NAME_START;
				} else if(c == '>') {
					this.handler.doElementStart(seq.subSequence(offset, offset + length));
					offset += length + 1;
					length = 0;
					mode = Mode.CHARACTERS;
				} else if(c == '/') {
					this.handler.doElementStart(seq.subSequence(offset, offset + length));
					offset += length + 1;
					length = 0;
					mode = Mode.ELEMENT_EMPTY_END;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case ATTRIBUTE_NAME_START:
				if(isNameChar(c)) {
					++length;
					mode = Mode.ATTRIBUTE_NAME;
				} else if(isWhitespace(c)) {
					// skip 
					offset += length + 1;
					length = 0;
				} else if(c == '/') {
					offset += length + 1;
					length = 0;
					mode = Mode.ELEMENT_EMPTY_END; 
				} else if(c == '>') {
					// do not close the element, here, it is closed on the corresponding </element>
//					this.handler.doElementEnd();
					offset += length + 1;
					length = 0;
					mode = Mode.CHARACTERS;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case ELEMENT_END:
				if(c == '>') {
					this.handler.doElementEnd();
					offset += length + 1;
					length = 0;
					mode = Mode.CHARACTERS;
				} else if(isWhitespace(c)) {
					// skip 
					offset += length + 1;
					length = 0;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case ELEMENT_EMPTY_END:
				if(c == '>') {
					this.handler.doElementEnd();
					offset += length + 1;
					length = 0;
					mode = Mode.CHARACTERS;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case ATTRIBUTE_NAME:
				if(isNameChar(c)) {
					// consume!
					++length;
				} else if (isWhitespace(c)) {
					this.handler.doAttributeName(seq.subSequence(offset, offset + length));
					offset += length + 1;
					length = 0;
					mode = Mode.EQUALS_START;
				} else if (c == '=') {
					this.handler.doAttributeName(seq.subSequence(offset, offset + length));
					offset += length + 1;
					length = 0;
					mode = Mode.ATTRIBUTE_VALUE_START;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case EQUALS_START:
				if(isWhitespace(c)) {
					// skip 
					offset += length + 1;
					length = 0;
				} else if (c == '=') {
					offset += length + 1;
					length = 0;
					mode = Mode.ATTRIBUTE_VALUE_START;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;

			case ATTRIBUTE_VALUE_START:
				if(isWhitespace(c)) {
					// skip 
					offset += length + 1;
					length = 0;
				} else if (c == '\'') {
					offset += length + 1;
					length = 0;
					mode = Mode.ATTRIBUTE_VALUE_APOS;
				} else if (c == '"') {
					offset += length + 1;
					length = 0;
					mode = Mode.ATTRIBUTE_VALUE_QUOT;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case ATTRIBUTE_VALUE_APOS:
				if (c == '\'') {
					this.handler.doAttributeValue(seq.subSequence(offset, offset + length));
					offset += length + 1;
					length = 0;
					mode = Mode.ATTRIBUTE_NAME_START;
				} else if (isChar(c)) {
					++length;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case ATTRIBUTE_VALUE_QUOT:
				if(c == '\"') {
					this.handler.doAttributeValue(seq.subSequence(offset, offset + length));
					offset += length + 1;
					length = 0;
					mode = Mode.ATTRIBUTE_NAME_START;
				} else if (isChar(c)) {
					++length;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case PROCESSING_INSTRUCTION_START:
				if(isNameChar(c)) {
					mode = Mode.PROCESSING_INSTRUCTION;
					++length;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;		
				
			case PROCESSING_INSTRUCTION:
				if(isNameChar(c)) {
					++length;
				} else if (isWhitespace(c)) {
					++length;
					mode = Mode.PROCESSING_INSTRUCTION_CHARS;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case PROCESSING_INSTRUCTION_CHARS:
				if(c == '?') {
					this.handler.doProcessingInstruction(seq.subSequence(offset, offset + length));
					offset += length + 1;
					length = 0;
					mode = Mode.PROCESSING_INSTRUCTION_END;
				} else if (isChar(c)) {
					++length;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case PROCESSING_INSTRUCTION_END:
				if(c == '>') {
					offset += length + 1;
					length = 0;
					mode = Mode.CHARACTERS;
				} else {
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case COMMENT_START:
				switch(c) {
				case '-':
					offset += length + 1;
					length = 0;
					mode = Mode.COMMENT;
					break;
					
				default:
					throw new IllegalArgumentException(String.format("'%s' is not allowed in %s", c, mode));
				}
				break;
				
			case COMMENT:
				switch(c) {
				case '>':
					this.handler.doComment(seq.subSequence(offset, offset + length));
					offset += length + 1;
					length = 0;
					mode = Mode.CHARACTERS;
					break;
				default:
					// nothing
					++length;
				}
				break;

			default:
				throw new IllegalStateException(String.format("Unhandled state: %s", mode));
				
			}
			
		}
	}

	private boolean isNameStartChar(char c) {
		return (c == ':' || (c >= 'A' && c <= 'Z') || c == '_'
				|| (c >= 'a' && c <= 'z') || (c >= '\u00C0' && c <= '\u00D6')
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
	
	private boolean isNameChar(char c) {
		return (isNameStartChar(c) || c == '-' || c == '.'
				|| (c >= '0' && c <= '9') || c == '\u00B7'
				|| (c >= '\u0300' && c <= '\u036F') || (c >= '\u023F' && c <= '\u2040'));
	}

	private boolean isWhitespace(char c) {
		return (c == ' ' || c == '\n' || c == '\r' || c == '\t');
	}
	
	private boolean isChar(char c) {
		return ( c == '\t' || c == '\r' || c == '\n' || (c >= '\u0020' && c <= '\uD7FF') || ( c >= '\uE000' && c <= '\uFFFD') );
	}
}
