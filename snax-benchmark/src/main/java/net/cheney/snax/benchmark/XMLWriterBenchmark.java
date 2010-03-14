package net.cheney.snax.benchmark;

import static org.apache.commons.io.IOUtils.closeQuietly;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import net.cheney.benchmark.Benchmark;
import net.cheney.benchmark.BenchmarkResult;
import net.cheney.benchmark.Benchmarkable;
import net.cheney.snax.SNAX;
import net.cheney.snax.model.Document;
import net.cheney.snax.writer.XMLWriter;

import org.apache.commons.io.IOUtils;

public class XMLWriterBenchmark  {

	public static class XMLBenchmark extends Benchmarkable {
		
		Document doc;
		Appendable out = new Appendable() {
			
			@Override
			public Appendable append(CharSequence csq, int start, int end)
					throws IOException {
				return this;
			}
			
			@Override
			public Appendable append(char c) throws IOException {
				// TODO Auto-generated method stub
				return this;
			}
			
			@Override
			public Appendable append(CharSequence csq) throws IOException {
				// TODO Auto-generated method stub
				return this;
			}
		};
		
		private String name;
		
		public XMLBenchmark(String name) {
			this.name = name;
		}

		@Override
		public void setup() throws IOException {
			doc = parseDocument(readInputStream(getInputStream(name)));
		}
		
		private static InputStream getInputStream(String string) {
			return XMLParserBenchmark.class.getClassLoader().getResourceAsStream(string);
		}

		private static String readInputStream(InputStream stream) throws IOException {
			try {
				return IOUtils.toString(stream);
			} finally {
				closeQuietly(stream);
			}
		}
		
		@Override
		public void benchmark() {
			try {
				new XMLWriter(out).visit(doc);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private Document parseDocument(String string) {
			return SNAX.parse(string);
		}
		
	}
	
	public static void main(String[] args) {
		Benchmark.Builder benchmark = Benchmark.newBenchmark("XMLWriterBenchmark");
		for(String name : Arrays.asList(args)) {
			benchmark = benchmark.of(name, new XMLBenchmark(name));
		}
		BenchmarkResult results = benchmark.setRepetitions(500).setIterations(25).run();
		System.out.println(results.toString());
	}
	
}