package net.cheney.snax.model;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class QName {

	private final String localpart;
	private final Namespace namespace;

	public static QName valueOf(@Nonnull String localPart) {
		return new QName(Namespace.NO_NAMESPACE, localPart);
	}
	
	public static QName valueOf(@Nonnull Namespace namespace, @Nonnull String localPart) {
		return new QName(namespace, localPart);
	}
	
	private QName(@Nonnull Namespace namespace, @Nonnull String localPart) {
		assert namespace != null;
		assert localPart != null;
		this.namespace = namespace;
		this.localpart = localPart;
	}

	public String localpart() {
		return localpart;
	}
	
	@Override
	public boolean equals(Object that) {
        if (that instanceof QName) {

        return namespace.equals(((QName) that).namespace)
            && localpart.equals(((QName) that).localpart);
        } 
        return false;
	}
	
	@Override
	public int hashCode() {
		return localpart.hashCode() ^ namespace.hashCode();
	}
	
	@Override
	public String toString() {
		return namespace.uri() == null ? localpart : String.format("{%s}%s", namespace.uri(), localpart);
	}

	public Namespace namespace() {
		return this.namespace;
	}

	public String prefix() {
		return namespace().prefix();
	}

}
