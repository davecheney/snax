package net.cheney.snax;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import javax.annotation.Nonnull;

import net.cheney.snax.builder.DocumentBuilder;
import net.cheney.snax.builder.Element;
import net.cheney.snax.builder.FragmentBuilder;
import net.cheney.snax.model.Document;
import net.cheney.snax.parser.XMLParser;

public final class SNAX {

	private SNAX() { 
		// prevent construction
	}
	
	public static Document parse(@Nonnull CharBuffer buffer) {
		return parse((CharSequence)buffer);
	}

	public static Document parse(@Nonnull CharSequence seq) {
		XMLParser parser = new XMLParser();
		parser.parse(seq);
		return parser.document();
	}
	
	public Document parse(@Nonnull ByteBuffer buffer, @Nonnull Charset charset) {
		return parse((CharSequence)charset.decode(buffer));
	}

	public Document build(@Nonnull char[] xml) {
		return parse((CharSequence)CharBuffer.wrap(xml));
	}
	
	public static DocumentBuilder newDocument() {
		return new DocumentBuilder();
	}
	
	public static Element newFragment(String name) {
		return new FragmentBuilder().child(name);
	}
	
	
}
