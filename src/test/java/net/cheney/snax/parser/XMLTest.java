package net.cheney.snax.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import net.cheney.snax.model.Document;
import net.cheney.snax.parser.XMLBuilder;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class XMLTest {

	private final InputStream stream;

	public XMLTest(String name) {
		stream = XMLTest.class.getClassLoader().getResourceAsStream(name);
	}
	
	@Test public void testXMLParser() throws IOException {
		Document doc = new XMLBuilder().parse(IOUtils.toString(stream));
		Assert.assertNotNull(doc);
	}

	@Parameters  
    public static List<Object[]> names() {
		return Arrays.asList(new Object[][]{
			{"emptyElement1.xml"},
			{"basic.xml"},
			{"time.xml"},
			{"cdata.xml"},
			{"namespace.xml"}
		});
	}
}
