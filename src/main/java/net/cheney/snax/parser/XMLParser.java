package net.cheney.snax.parser;

import javax.annotation.Nonnull;

public final class XMLParser {
	
	private State state = State.CHARACTERS;
	
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
			currentState = currentState.parse(seq.charAt(limit), this);
		}
		this.state = currentState;
	}
	
}
