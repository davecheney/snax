package net.cheney.snax.benchmark;

import net.cheney.snax.benchmark.XMLParserBenchmark;

import org.junit.Test;

public class XMLParserBenchmarkTest extends XMLParserBenchmark {

	@Test public void testXMLParserBenchmark() {
		main(new String[] { "benchmark.xml", "oasis.xml", "periodic.xml", "xmltest.xml" } );
	}
}
