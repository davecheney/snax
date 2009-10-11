package net.cheney.snax.parser;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Document;

public class XMLBuilder {

	private final XMLParser parser;

	public XMLBuilder() {
		this.parser = new XMLParser();
	}
	
	public Document build(@Nonnull CharSequence cs) {
		return build(cs.toString().toCharArray());
	}
	
	public Document build(@Nonnull ByteBuffer buffer, @Nonnull Charset charset) {
		return build(charset.decode(buffer));
	}

	public Document build(@Nonnull char[] xml) {
		parser.parse(CharBuffer.wrap(xml));
		return parser.document();
	}
	
	public Document build(@Nonnull CharBuffer buffer) {
		parser.parse(buffer);
		return parser.document();
	}

	public Document parse(@Nonnull CharSequence seq) {
		parser.parse(seq);
		return parser.document();
	}

}
