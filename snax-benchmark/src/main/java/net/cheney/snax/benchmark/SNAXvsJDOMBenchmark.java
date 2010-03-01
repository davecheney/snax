package net.cheney.snax.benchmark;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Arrays;

import net.cheney.benchmark.Benchmark;
import net.cheney.benchmark.BenchmarkResult;
import net.cheney.benchmark.Benchmarkable;
import net.cheney.snax.model.Document;
import net.cheney.snax.parser.XMLBuilder;

import org.apache.commons.io.IOUtils;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public final class SNAXvsJDOMBenchmark  {
	
	public abstract static class BaseBenchmark extends Benchmarkable {
		
		protected String doc;
		private String name;
		
		public BaseBenchmark(String name) {
			this.name = name;
		}
		
		@Override
		public void setup() throws IOException {
			doc = readInputStream(getInputStream(name));
		}
		
		private static InputStream getInputStream(String string) {
			return SNAXvsJDOMBenchmark.class.getClassLoader().getResourceAsStream(string);
		}

		private static String readInputStream(InputStream stream) throws IOException {
			try {
				return IOUtils.toString(stream);
			} finally {
				IOUtils.closeQuietly(stream);
			}
		}
		
		@Override
		public void teardown() {
			doc = null;
		}
		
		protected void assertThat(boolean bool) {
			if(!bool) {
				throw new AssertionError();
			}
		}
	}
	
	public final static class SNAXBenchmark extends BaseBenchmark {
		
		public SNAXBenchmark(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void benchmark() {
			Document d = parseDocument(doc);
			assertThat(!d.rootElement().qname().localpart().isEmpty());
		}
		
		private Document parseDocument(String string) {
			return new XMLBuilder().parse(string);
		}
		
	}
	
	public final static class JDOMBenchmark extends BaseBenchmark {
		
		public JDOMBenchmark(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void benchmark() {
			org.jdom.Document d = parseDocument(doc);
			assertThat(!d.getRootElement().getName().isEmpty());
		}
		
		private org.jdom.Document parseDocument(String string) {
			try {
				return new SAXBuilder().build(new StringReader(doc));
			} catch (JDOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	public static void main(String[] args) {
		Benchmark.Builder benchmark = Benchmark.newBenchmark("SNAXvsJDOMBenchmark");
		for(String name : Arrays.asList(args)) {
			benchmark = benchmark.of("SNAX ("+name+")", new SNAXBenchmark(name));
			benchmark = benchmark.of("JDOM ("+name+")", new JDOMBenchmark(name));
		}
		BenchmarkResult results = benchmark.setRepetitions(250).setIterations(50).run();
		System.out.println(results.toString());
	}

}
