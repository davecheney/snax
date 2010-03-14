package net.cheney.snax.parser;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Document;

import static java.lang.Character.isWhitespace;

public final class XMLParser {
	
	private enum State {
		
		CHARACTERS {
			@Override
			State parse(char c, XMLParser parser) {
				if(c == '<') {
					parser.doCharacters();
					return STAG_NAME_START;
				} else {
					return this;
				}
			}
		},
		
		STAG_NAME_START {
			@Override
			State parse(char c, XMLParser parser) {
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
		},
		
		DECLARATION_START {
			@Override
			State parse(char c, XMLParser parser) {
				if(c == '[') {
					parser.incrementOffsetAndResetLength();
					return CDATA_START;
				} else if(isNameChar(c)) {
					return DECLARATION;
				} else {
					throw new IllegalParseStateException(c, DECLARATION_START);
				}
			}
		},
		
		DECLARATION  {
			@Override
			State parse(char c, XMLParser parser) {
				if(c == '>') {
					parser.incrementOffsetAndResetLength();
					return CHARACTERS;
				} else 	if (c == '[') {
					parser.incrementOffsetAndResetLength();
					return DECLARATION_CDATA;
				} else if(isChar(c)) {
					return this;
				} else {
					throw new IllegalParseStateException(c, CHARACTERS);
				}
			}
		},
		
		DECLARATION_CDATA {
			@Override
			State parse(char c, XMLParser parser) {
				if (c == ']') {
					return DECLARATION_END;
				} else {
					return this;
				}
			}
		},
		
		DECLARATION_END {
			@Override
			State parse(char c, XMLParser parser) {
				if (c == '>') {
//					parser.doCData();
					parser.incrementOffsetAndResetLength();
					return CHARACTERS;
				} else if (isWhitespace(c)) {
					parser.incrementOffsetAndResetLength();
					return this;
				} else {
					throw new IllegalParseStateException(c, DECLARATION_END);
				}
			}
		},
		
		CDATA_START {
			@Override
			State parse(char c, XMLParser parser) {
				if (c == '[') {
					parser.incrementOffsetAndResetLength();
					return CDATA;
				} else {
					// consume chars up to the 2nd [
					return this;
				}
			}
		},
		
		CDATA {
			@Override
			State parse(char c, XMLParser parser) {
				if (c == ']') {
					return CDATA_END_1;
				} else {
					return this;
				}
			}
		},
		
		CDATA_END_1 {
			@Override
			State parse(char c, XMLParser parser) {
				if (c == ']') {
					return CDATA_END_2;
				} else {
					return CDATA;
				}
			}
		},
		
		CDATA_END_2 {
			@Override
			State parse(char c, XMLParser parser) {
				if (c == '>') {
					parser.doCData();
					return CHARACTERS;
				} else {
					return CDATA;
				}
			}
		},
		
		ETAG_NAME_START {
			@Override
			State parse(char c, XMLParser parser) {
				if (isNameStartChar(c)) {
					return ETAG_NAME;
				} else {
					throw new IllegalParseStateException(c, ETAG_NAME_START);
				}
			}
		},
		
		ETAG_NAME {
			@Override
			State parse(char c, XMLParser parser) {
				if (isNameChar(c)) {
					// consume
					return this;
				} else if (isWhitespace(c)) {
					parser.incrementOffsetAndResetLength();
					return ELEMENT_END;
				} else if (c == '>') {
					parser.doElementEnd();
					return CHARACTERS;
				} else {
					throw new IllegalParseStateException(c, ETAG_NAME);
				}
			}
		},
		
		STAG_NAME {
			@Override
			State parse(char c, XMLParser parser) {
				if (isNameChar(c)) {
					// consume
					return this;
				} else if (isWhitespace(c)) {
					parser.doElementStart();
					return ATTRIBUTE_NAME_START;
				} else if (c == '>') {
					parser.doElementStart();
					return CHARACTERS;
				} else if (c == '/') {
					parser.doElementStart();
					return ELEMENT_EMPTY_END;
				} else {
					throw new IllegalParseStateException(c, STAG_NAME);
				}
			}
		},
		
		ATTRIBUTE_NAME_START {
			@Override
			State parse(char c, XMLParser parser) {
				if (isNameChar(c)) {
					return ATTRIBUTE_NAME;
				} else if (isWhitespace(c)) {
					// skip
					parser.incrementOffsetAndResetLength();
					return this;
				} else if (c == '/') {
					parser.incrementOffsetAndResetLength();
					return ELEMENT_EMPTY_END;
				} else if (c == '>') {
					// do not close the element, here, it is closed on the
					// corresponding </element>
					// this.handler.doElementEnd();
					parser.incrementOffsetAndResetLength();
					return CHARACTERS;
				} else {
					throw new IllegalParseStateException(c, ATTRIBUTE_NAME_START);
				}
			}
		},
		
		ELEMENT_END {
			@Override
			State parse(char c, XMLParser parser) {
				if (c == '>') {
					parser.doElementEnd();
					return CHARACTERS;
				} else if (isWhitespace(c)) {
					// skip
					parser.incrementOffsetAndResetLength();
					return this;
				} else {
					throw new IllegalParseStateException(c, ELEMENT_END);
				}
			}
		},
		
		ELEMENT_EMPTY_END {
			@Override
			State parse(char c, XMLParser parser) {
				if (c == '>') {
					parser.doElementEnd();
					return CHARACTERS;
				} else {
					throw new IllegalParseStateException(c, ELEMENT_EMPTY_END);
				}
			}
		},
		
		ATTRIBUTE_NAME {
			@Override
			State parse(char c, XMLParser parser) {
				if (isNameChar(c)) {
					// consume!
					return this;
				} else if (isWhitespace(c)) {
					parser.doAttributeName();
					return EQUALS_START;
				} else if (c == '=') {
					parser.doAttributeName();
					return ATTRIBUTE_VALUE_START;
				} else {
					throw new IllegalParseStateException(c, ATTRIBUTE_NAME);
				}
			}
		},
		
		EQUALS_START {
			@Override
			State parse(char c, XMLParser parser) {
				if (isWhitespace(c)) {
					// skip
					parser.incrementOffsetAndResetLength();
					return this;
				} else if (c == '=') {
					parser.incrementOffsetAndResetLength();
					return ATTRIBUTE_VALUE_START;
				} else {
					throw new IllegalParseStateException(c, EQUALS_START);
				}
			}
		},
		
		ATTRIBUTE_VALUE_START {
			@Override
			State parse(char c, XMLParser parser) {
				if (isWhitespace(c)) {
					// skip
					parser.incrementOffsetAndResetLength();
					return this;
				} else if (c == '\'') {
					parser.incrementOffsetAndResetLength();
					return ATTRIBUTE_VALUE_APOS;
				} else if (c == '"') {
					parser.incrementOffsetAndResetLength();
					return ATTRIBUTE_VALUE_QUOT;
				} else {
					throw new IllegalParseStateException(c, ATTRIBUTE_VALUE_START);
				}
			}
		},
		
		ATTRIBUTE_VALUE_APOS {
			@Override
			State parse(char c, XMLParser parser) {
				if (c == '\'') {
					parser.doAttributeValue();
					return ATTRIBUTE_NAME_START;
				} else if (isChar(c)) {
					return this;
				} else {
					throw new IllegalParseStateException(c, ATTRIBUTE_VALUE_APOS);
				}
			}
		},
		
		ATTRIBUTE_VALUE_QUOT {
			@Override
			State parse(char c, XMLParser parser) {
				if (c == '\"') {
					parser.doAttributeValue();
					return ATTRIBUTE_NAME_START;
				} else if (isChar(c)) {
					return this;
				} else {
					throw new IllegalParseStateException(c, ATTRIBUTE_VALUE_QUOT);
				}
			}
		},
		
		PROCESSING_INSTRUCTION_START {
			@Override
			State parse(char c, XMLParser parser) {
				if (isNameChar(c)) {
					return PROCESSING_INSTRUCTION;
				} else {
					throw new IllegalParseStateException(c, PROCESSING_INSTRUCTION_START);
				}
			}
		},
		
		PROCESSING_INSTRUCTION {
			@Override
			State parse(char c, XMLParser parser) {
				if (isNameChar(c)) {
					return this;
				} else if (isWhitespace(c)) {
					return PROCESSING_INSTRUCTION_CHARS;
				} else {
					throw new IllegalParseStateException(c, PROCESSING_INSTRUCTION);
				}
			}
		},
		
		PROCESSING_INSTRUCTION_CHARS {
			@Override
			State parse(char c, XMLParser parser) {
				if (c == '?') {
					parser.doProcessingInstruction();
					return PROCESSING_INSTRUCTION_END;
				} else if (isChar(c)) {
					return this;
				} else {
					throw new IllegalParseStateException(c, PROCESSING_INSTRUCTION_CHARS);
				}
			}
		},
		
		PROCESSING_INSTRUCTION_END {
			@Override
			State parse(char c, XMLParser parser) {
				if (c == '>') {
					parser.incrementOffsetAndResetLength();
					return CHARACTERS;
				} else {
					throw new IllegalParseStateException(c, PROCESSING_INSTRUCTION_END);
				}
			}
		},
		
		COMMENT_START {
			@Override
			State parse(char c, XMLParser parser) {
				if(c == '-') {
					parser.incrementOffsetAndResetLength();
					return COMMENT;
				} else {
					throw new IllegalParseStateException(c, COMMENT_START);
				}
			}
		},
		
		COMMENT {
			@Override
			State parse(char c, XMLParser parser) {
				if(c == '>') { 
					parser.doComment();
					return CHARACTERS;
				} else {
					return this;
				}
			}
		};
		
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
		
	}

	
	private State state = State.CHARACTERS;
	
	private int offset, limit = 0;

	private CharSequence sequence;
	
	private NodeBuilder builder;

	public Document document() {
		return new Document(builder.contents());
	}

	public void doAttributeName() {
		builder.doAttributeName(subsequence());
		incrementOffsetAndResetLength();
	}

	public void doAttributeValue() {
		builder.doAttributeValue(subsequence());
		incrementOffsetAndResetLength();
	}

	public void doCharacters() {
		CharSequence seq = subsequence();
		incrementOffsetAndResetLength();
		if(isBlank(seq)) {
			return;
		} else {
			builder.doCharacters(seq);
		}
	}

	public void doComment() {
		builder.doComment(subsequence());
		incrementOffsetAndResetLength();
	}

	public void doElementEnd() {
		builder = builder.doElementEnd();
		incrementOffsetAndResetLength();
	}

	public void doElementStart() {
		builder = builder.doElementStart(subsequence());
		incrementOffsetAndResetLength();
	}

	public void doProcessingInstruction() {
		builder.doProcessingInstruction(subsequence());
		incrementOffsetAndResetLength();
	}

	public void doProcessingInstructionEnd() {
		// 
	}
	
    public static boolean isBlank(@Nonnull CharSequence str) {
        for (int i = 0, strLen = str.length(); i < strLen; i++) {
            if (!isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
	
	public XMLParser() {
		this.builder = new DocumentBuilder();
	}

	void incrementOffsetAndResetLength() {
		offset = limit;
		offset++;
	}
	
	private CharSequence subsequence() {
		return sequence.subSequence(offset, limit);
	}
	
	public void parse(CharSequence seq) {
		int max = seq.length();
		// Yank state into a stack local, reduces benchmark by 10%
		State currentState = this.state;
		// make seq available to the subsequence method without making offset and limit visible
		this.sequence = seq;
		for(offset = 0, limit = 0 ; limit < max ; ++limit ) {
			currentState = currentState.parse(seq.charAt(limit), this);
		}
		this.state = currentState;
	}
	
	public void doCData() {
		CharSequence cdata = subsequence();
		incrementOffsetAndResetLength();
		builder.doCharacters(cdata.subSequence(0, cdata.length() - 2));
	}
	
	public static class IllegalParseStateException extends IllegalArgumentException {

		private static final long serialVersionUID = -1464623700570596183L;

		public IllegalParseStateException(char c, State state) {
			super(String.format("'%s' is not allowed in %s", c, state.toString()));
		}
	}
	
}
