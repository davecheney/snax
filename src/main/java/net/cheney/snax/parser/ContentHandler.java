package net.cheney.snax.parser;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Document;

public final class ContentHandler {

	private NodeBuilder builder;

	public ContentHandler() {
		this.builder = new DocumentBuilder();
	}
	
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

}
