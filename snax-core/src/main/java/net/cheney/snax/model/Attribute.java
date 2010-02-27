package net.cheney.snax.model;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class Attribute extends ValueNode implements Namespaced {
	
	public static final QName XML_LANG = QName.valueOf(Namespace.XML_NAMESPACE, "lang");

	private final QName qname;

	public Attribute(@Nonnull String localPart, @Nonnull CharSequence value) {
		this(QName.valueOf(localPart), value);
	}
	
	public Attribute(@Nonnull Namespace namespace, @Nonnull String localPart, @Nonnull String value) {
		this(QName.valueOf(namespace, localPart), value);
	}
	
	public Attribute(@Nonnull QName qname, @Nonnull CharSequence value) {
		super(value);
		this.qname = qname;
	}
	
	@Override
	public Type type() {
		return Type.ATTRIBUTE;
	}

	@Override
	@Nonnull
	public String localpart() {
		return this.qname.localpart();
	}

	@Override
	@Nonnull
	public Namespace namespace() {
		return this.qname.namespace();
	}
	
	@Nonnull
	public QName qname() {
		return this.qname;
	}

	@Override
	@Nonnull
	public String prefix() {
		return namespace().prefix();
	}

	@Override
	Attribute detach() {
		return this;
	}
	
	@Override
	public boolean equals(Object that) {
		if(that instanceof Attribute) {
			return (this.qname.equals(((Attribute)that).qname) && (this.value().equals(((Attribute)that).value())));
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return qname.hashCode() ^ super.hashCode();
	}
	
	@Override
	public void accept(@Nonnull Visitor visitor) throws IOException {	
		visitor.visit(this);
	}
}
