package net.cheney.snax.experimental.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import net.cheney.benchmark.Benchmark;
import net.cheney.benchmark.BenchmarkResult;
import net.cheney.benchmark.Benchmarkable;
import net.cheney.snax.SNAX;
import net.cheney.snax.model.Document;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class XMLParserBenchmark  {
	
	public final static class XMLStableBenchmark extends Benchmarkable {
		
		String doc;
		private String name;
		
		public XMLStableBenchmark(String name) {
			this.name = name;
		}

		@Override
		public void setup() throws IOException {
			doc = readInputStream(getInputStream(name));
		}
		
		private static InputStream getInputStream(String string) {
			return XMLParserBenchmark.class.getClassLoader().getResourceAsStream(string);
		}

		private static String readInputStream(InputStream stream) throws IOException {
			try {
				return IOUtils.toString(stream);
			} finally {
				IOUtils.closeQuietly(stream);
			}
		}
		
		@Override
		public void benchmark() {
			Document d = parseDocument(doc);
			assertThat(!d.rootElement().qname().localpart().isEmpty());
		}
		
		private Document parseDocument(String string) {
			return new XMLBuilder().parse(string);
		}
		
		private void assertThat(boolean bool) {
			if(!bool) {
				throw new AssertionError();
			}
		}
		
		@Override
		public void teardown() {
			doc = null;
		}
		
	}
	
	public final static class XMLExperimentalBenchmark extends Benchmarkable {
		
		String doc;
		private String name;
		
		public XMLExperimentalBenchmark(String name) {
			this.name = name;
		}

		@Override
		public void setup() throws IOException {
			doc = readInputStream(getInputStream(name));
		}
		
		private static InputStream getInputStream(String string) {
			return XMLParserBenchmark.class.getClassLoader().getResourceAsStream(string);
		}

		private static String readInputStream(InputStream stream) throws IOException {
			try {
				return IOUtils.toString(stream);
			} finally {
				IOUtils.closeQuietly(stream);
			}
		}
		
		@Override
		public void benchmark() {
			Document d = parseDocument(doc);
			assertThat(!d.rootElement().qname().localpart().isEmpty());
		}
		
		private Document parseDocument(String string) {
			return new SNAX.parse(string);
		}
		
		private void assertThat(boolean bool) {
			if(!bool) {
				throw new AssertionError();
			}
		}
		
		@Override
		public void teardown() {
			doc = null;
		}
		
	}
	
	public static void main(String[] args) {
		Benchmark.Builder benchmark = Benchmark.newBenchmark("Experimental Parser Benchmark");
		for(String name : Arrays.asList(args)) {
			benchmark = benchmark.of("Stable ["+name+"]", new XMLStableBenchmark(name)).and("Experimental ["+name+"]", new XMLExperimentalBenchmark(name));
		}
		BenchmarkResult results = benchmark.setRepetitions(500).setIterations(25).run();
		System.out.println(results.toString());
	}
	
	@Test public void testXMLParserBenchmark() {
		main(new String[] { "benchmark.xml", "oasis.xml", "periodic.xml", "xmltest.xml" } );
	}

}
