package net.cheney.snax.validation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import net.cheney.snax.model.Document;
import net.cheney.snax.parser.XMLBuilder;

import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class XMLValidationTest {

	 @Parameters
	 public static Collection xmlconf() throws IOException {
		 Document xmlconf = loadXML("xmlconf.xml");
		 return Arrays.asList(new Object[][] {
	   {"22101", true },
	   {"221x1", false },
	   {"22101-5150", true },
	   {"221015150", false }});
	 }

	private static Document loadXML(String source) throws IOException {
		return new XMLBuilder().build(load(source));
	}

	private static char[] load(String source) throws IOException {
		InputStream is = streamFromSource(source);
		try {
			return IOUtils.toCharArray(is);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	private static InputStream streamFromSource(String source) {
		String s = XMLValidationTest.class.getPackage().getName().replace('.', '/').replace('-', '_') + "/" + source;
		System.out.println(String.format("Loading tests from [%s]", s));
		return XMLValidationTest.class.getClassLoader().getResourceAsStream(s);
	}
	
}
