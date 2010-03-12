package net.cheney.snax.builder;

public interface Element extends Common {

	Element child(String name);
	
	Element child(String name, String text);
	
	Element child(String prefix, String name, String text);

	Element end();

	Element text(String text);
	
	void close();
}
