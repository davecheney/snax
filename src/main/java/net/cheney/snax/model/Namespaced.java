package net.cheney.snax.model;

public interface Namespaced {

	String localpart();
	
	String prefix();
	
	Namespace namespace();
	
	QName qname();
	
}