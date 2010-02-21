package net.cheney.snax.benchmark;

import org.junit.Test;

public class XMLWriterBenchmarkTest extends XMLWriterBenchmark {

	@Test public void testXMLWriterBenchmark() {
		main(new String[] { "benchmark.xml", "oasis.xml", "periodic.xml", "xmltest.xml" } );
	}
}
