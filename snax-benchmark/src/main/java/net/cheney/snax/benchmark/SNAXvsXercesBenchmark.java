package net.cheney.snax.benchmark;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.cheney.benchmark.Benchmark;
import net.cheney.benchmark.BenchmarkResult;
import net.cheney.benchmark.Benchmarkable;
import net.cheney.snax.SNAX;
import net.cheney.snax.model.Document;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

public class SNAXvsXercesBenchmark  {
	
	public abstract static class BaseBenchmark extends Benchmarkable {
		
		protected String name;
		
		public BaseBenchmark(String name) {
			this.name = name;
		}

		
		protected static InputStream getInputStream(String string) {
			return SNAXvsXercesBenchmark.class.getClassLoader().getResourceAsStream(string);
		}

	}
	
	public static class SNAXBenchmark extends BaseBenchmark {

		protected String doc;
		
		public SNAXBenchmark(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void benchmark() {
			Document d = parseDocument(doc);
			assert d.rootElement() != null;
		}
		
		private Document parseDocument(String string) {
			return SNAX.parse(string);
		}
		
		@Override
		public void setup() throws IOException {
			doc = readInputStream(getInputStream(name));
		}
		
		private static String readInputStream(InputStream stream) throws IOException {
			try {
				return IOUtils.toString(stream);
			} finally {
				IOUtils.closeQuietly(stream);
			}
		}
		
	}
	
	public static class XercesBenchmark extends BaseBenchmark {
		
		public XercesBenchmark(String name) {
			super(name);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void benchmark() {
			org.w3c.dom.Document d = parseDocument();
			assert d.getDocumentElement() != null;
		}
		
		private org.w3c.dom.Document parseDocument() {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			InputStream stream = getInputStream(name);
			//Using factory get an instance of document builder
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				return db.parse(stream);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(stream);
			}
			return null;
		}
		
	}
	
	public static void main(String[] args) {
		Benchmark.Builder benchmark = Benchmark.newBenchmark("SNAXvsXercesBenchmark");
		for(String name : Arrays.asList(args)) {
			benchmark = benchmark.of("SNAX ("+name+")", new SNAXBenchmark(name));
			benchmark = benchmark.of("Xerces ("+name+")", new XercesBenchmark(name));
		}
		BenchmarkResult results = benchmark.setRepetitions(250).setIterations(50).run();
		System.out.println(results.toString());
	}

}
