package net.cheney.snax;

import net.cheney.snax.builder.DocumentBuilder;
import net.cheney.snax.builder.Element;
import net.cheney.snax.builder.FragmentBuilder;
import net.cheney.snax.model.Document;
import net.cheney.snax.parser.XMLBuilder;

public final class SNAX {

	private SNAX() { 
		// prevent construction
	}

	public static Document parse(CharSequence seq) {
		return new XMLBuilder().parse(seq);
	}

	public static DocumentBuilder newDocument() {
		return new DocumentBuilder();
	}
	
	public static Element newFragment(String name) {
		return new FragmentBuilder().child(name);
	}
	
	
}
