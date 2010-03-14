package net.cheney.snax.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import net.cheney.snax.SNAX;
import net.cheney.snax.model.Document;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class XMLTest {

	private final String body;
	private final int CHUNK_SIZE = 1024;

	public XMLTest(String name) throws IOException {
		InputStream stream = XMLTest.class.getClassLoader().getResourceAsStream(name);
		body = IOUtils.toString(stream);
		stream.close();
	}
	
	@Test public void testXMLParserWithWholeBody() {
		Document doc = SNAX.parse(body);
		Assert.assertNotNull(doc);
	}
	
	@Test public void testXMLParserWithWholeChunk() {
		XMLParser parser = new XMLParser();
		String remainder = body;
		while(!remainder.isEmpty()) {
			String chunk = StringUtils.substring(remainder, 0, CHUNK_SIZE);
			parser.parse(chunk);
			remainder = StringUtils.substring(remainder, CHUNK_SIZE);
		}
		Assert.assertNotNull(parser.document());
	}
	
	@Test public void testXMLParserWithRandomChunk() {
		XMLParser parser = new XMLParser();
		Random r = new Random(200910110000L);
		String remainder = body;
		while(!remainder.isEmpty()) {
			int chunkSize = r.nextInt();
			String chunk = StringUtils.substring(remainder, 0, chunkSize);
			parser.parse(chunk);
			remainder = StringUtils.substring(remainder, chunkSize);
		}
		Assert.assertNotNull(parser.document());
	}

	@Parameters  
    public static List<Object[]> names() {
		return Arrays.asList(new Object[][]{
			{"emptyElement1.xml"},
			{"basic.xml"},
			{"time.xml"},
			{"cdata.xml"},
			{"namespace.xml"},
			{"xmltest.xml"},
			{"oasis.xml"},
			{"ibm_oasis_not-wf.xml"},
			{"errata4e.xml"},
			{"xmlconf.xml"}
		});
	}
}
