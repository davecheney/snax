package net.cheney.snax.model;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Namespace  {
	
	public static final String BLANK_URI = "";
	public static final String BLANK_PREFIX = "";

	public static final Namespace NO_NAMESPACE = Namespace.valueOf(BLANK_PREFIX, BLANK_URI);
	public static final Namespace XML_NAMESPACE = Namespace.valueOf("xml", "http://www.w3.org/XML/1998/namespace");
	public static final Namespace XMLNS_NAMESPACE = Namespace.valueOf("xmlns", "http://www.w3.org/2000/xmlns/");

	private final String uri;
	private final String prefix;

	private Namespace(@Nonnull String prefix, @Nonnull String uri) {
		this.prefix = prefix;
		this.uri = uri;
	}

	public static Namespace valueOf(@Nonnull String prefix, @Nonnull String uri) {
		return new Namespace(prefix, uri);
	}

	public String prefix() {
		return prefix;
	}
	
	public String uri() {
		return uri;
	}

	@Override
	public boolean equals(Object that) {
		if(that instanceof Namespace) {
			return this.uri.equals(((Namespace)that).uri);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return uri.hashCode();
	}
	
}
