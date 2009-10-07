package net.cheney.snax.parser;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Document;
import net.cheney.snax.model.Node;
import net.cheney.snax.parser.XMLParser.EventHandler;

public class ContentHandler implements EventHandler {

	private NodeBuilder builder;

	public ContentHandler() {
		this.builder = new DocumentBuilder();
	}
	
	public final Document document() {
		return new Document(builder.contents().toArray(new Node[0]));
	}

	@Override
	public final void doAttributeName(@Nonnull CharSequence seq) {
		builder.doAttributeName(seq);
	}

	@Override
	public final void doAttributeValue(@Nonnull CharSequence seq) {
		builder.doAttributeValue(seq);
	}

	@Override
	public final void doCharacters(@Nonnull CharSequence seq) {
		if(isBlank(seq)) {
			return;
		} else {
			builder.doCharacters(seq);
		}
	}

	@Override
	public final void doComment(@Nonnull CharSequence seq) {
		builder.doComment(seq);
	}

	@Override
	public final void doElementEnd() {
		builder = builder.doElementEnd();
	}

	@Override
	public final void doElementStart(@Nonnull CharSequence seq) {
		builder = builder.doElementStart(seq);
	}

	@Override
	public final void doProcessingInstruction(@Nonnull CharSequence seq) {
		builder.doProcessingInstruction(seq);
	}

	@Override
	public void doProcessingInstructionEnd() {
		// 
	}
	
    public static boolean isBlank(@Nonnull CharSequence str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

}
