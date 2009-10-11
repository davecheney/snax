package net.cheney.snax.parser;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Document;

public final class XMLParser {
	
	private State state = State.CHARACTERS;
	
	private int offset, limit = 0;

	private CharSequence sequence;
	
	private NodeBuilder builder;

	public Document document() {
		return new Document(builder.contents());
	}

	public void doAttributeName(@Nonnull CharSequence seq) {
		builder.doAttributeName(seq);
	}

	public void doAttributeValue(@Nonnull CharSequence seq) {
		builder.doAttributeValue(seq);
	}

	public void doCharacters(@Nonnull CharSequence seq) {
		if(isBlank(seq)) {
			return;
		} else {
			builder.doCharacters(seq);
		}
	}

	public void doComment(@Nonnull CharSequence seq) {
		builder.doComment(seq);
	}

	public void doElementEnd() {
		builder = builder.doElementEnd();
	}

	public void doElementStart(@Nonnull CharSequence seq) {
		builder = builder.doElementStart(seq);
	}

	public void doProcessingInstruction(@Nonnull CharSequence seq) {
		builder.doProcessingInstruction(seq);
	}

	public void doProcessingInstructionEnd() {
		// 
	}
	
    public static boolean isBlank(@Nonnull CharSequence str) {
        for (int i = 0, strLen = str.length(); i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
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
			currentState = currentState.parse(seq.charAt(limit), this);
		}
		this.state = currentState;
	}

	public void doCData(CharSequence cdata) {
		builder.doCharacters(cdata.subSequence(0, cdata.length() - 2));
	}
	
}
