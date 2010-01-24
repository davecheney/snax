package net.cheney.snax.writer;

import java.io.IOException;

import javax.annotation.Nonnull;

import net.cheney.snax.model.Attribute;
import net.cheney.snax.model.Comment;
import net.cheney.snax.model.Element;
import net.cheney.snax.model.Namespace;
import net.cheney.snax.model.Namespaced;
import net.cheney.snax.model.ProcessingInstruction;
import net.cheney.snax.model.Text;

public abstract class XMLPrinter {

	private final Appendable out;

	public XMLPrinter(@Nonnull Appendable out) {
		this.out = out;
	}

	private void printNamespacePrefix(@Nonnull Namespace ns) throws IOException {
		final String prefix = ns.prefix();
		if (!prefix.isEmpty()) {
			out.append(':').append(prefix);
		}
	}

	public final void printEscapedAttributeEntities(@Nonnull String value) throws IOException {
		for (int i = 0, n = value.length(); i < n; ++i) {
			final char ch = value.charAt(i);
			switch (ch) {
			case '<':
				out.append("&lt;");
				break;
			case '>':
				out.append("&gt;");
				break;
			case '"':
				out.append("&quot;");
				break;
			case '&':
				out.append("&amp;");
				break;
			case '\r':
				out.append("&#xD;");
				break;
			case '\t':
				out.append("&#x9;");
				break;
			case '\n':
				out.append("&#xA;");
				break;

			default:
				out.append(ch);
			}
		}
	}

	final void printQualifiedName(@Nonnull Namespaced e) throws IOException {
		final String prefix = e.prefix();
		if (!prefix.isEmpty()) {
			out.append(prefix);
			out.append(':');
		}
		out.append(e.localpart());
	}


	public final void print(@Nonnull ProcessingInstruction pi) throws IOException {
		printStartElementOpenTag();
		out.append('?').append(pi.target()).append(' ').append(pi.data())
				.append('?');
		printEndElementCloseTag();
	}

	public final void print(@Nonnull Text text) throws IOException {
		printEscapedText(text.value());
	}

	private void printEscapedText(@Nonnull String str) throws IOException {
		for (int i = 0, n = str.length(); i < n; ++i) {
			final char c = str.charAt(i);
			switch (c) {
			case '<':
				out.append("&lt;");
				break;
			case '>':
				out.append("&gt;");
				break;
			case '&':
				out.append("&amp;");
				break;
			default:
				out.append(c);
			}
		}
	}

	public final void print(@Nonnull Namespace namespace) throws IOException {
		    out.append(" xmlns");
		    printNamespacePrefix(namespace);
		    out.append("=\"");
		    printEscapedAttributeEntities(namespace.uri());
		    out.append('"');
	}

	public final void print(@Nonnull Comment comment) throws IOException {
		printStartElementOpenTag();
	    out.append("!-- ");
		printEscapedText(comment.value());
	    out.append(" --");
	    printEndElementCloseTag();
	}
	

	protected final void printEscapedElementEntities(@Nonnull String str)
			throws IOException {
		for (int i = 0, n = str.length(); i < n; ++i) {
			final char c = str.charAt(i);
			switch (c) {
			case '<':
				out.append("&lt;");
				break;
			case '>':
				out.append("&gt;");
				break;
			case '&':
				out.append("&amp;");
				break;
			case '\r':
				out.append("&#xD;");
				break;
			case '\n':
				out.append("&#xA;");
				break;
			default:
				out.append(c);
			}
		}
	}

	public void print(@Nonnull Attribute attribute) throws IOException {
	    out.append(' ');
	    printQualifiedName(attribute);
	    out.append("=\"");
	    printEscapedAttributeEntities(attribute.value());
	    out.append('"');
	}
	
	protected void printStartElementOpenTag() throws IOException {
		out.append('<');
	}

	protected void printEndElementCloseTag() throws IOException {
		out.append('>');
	}
	
	protected void printEndElementOpenTag() throws IOException {
		out.append("</");
	}

	protected void printEmptyElementCloseTag() throws IOException {
		out.append("/>");
	}

	protected void printStartElementCloseTag() throws IOException {
		out.append('>');
	}

	public void printEndElementTag(@Nonnull Element element) throws IOException {
		printEndElementOpenTag();
		printQualifiedName(element);
		printEndElementCloseTag();
	}

}
