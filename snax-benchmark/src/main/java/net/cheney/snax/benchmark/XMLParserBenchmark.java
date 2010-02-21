package net.cheney.snax.benchmark;

import java.io.IOException;
import java.io.InputStream;

import net.cheney.benchmarker.core.BenchmarkCallable;
import net.cheney.snax.model.Document;
import net.cheney.snax.parser.XMLBuilder;

import org.apache.commons.io.IOUtils;

public class XMLParserBenchmark implements Runnable {
	
	public static final int ITERATIONS = 10, OUTER_LOOP_COUNT = 50, INNER_LOOP_COUNT = 200;
	
	protected static void benchmark(BenchmarkCallable c) {
		System.out.println(String.format("Warmup: %s", c.call().toString()));
		for(int iteractions = 0 ; iteractions < ITERATIONS ; ++iteractions) {
			System.out.println(String.format("Iteraction %d: %s", iteractions+1, c.call().toString()));
		}
	}

	public String doc;
	
	@Override
	public void run() {
		setup();
		for(int outer = 0 ; outer < OUTER_LOOP_COUNT ; ++outer) {
			for(int inner = 0 ; inner < INNER_LOOP_COUNT ; ++inner) {
				Document d = parseDocument(doc);
				assert d.rootElement() != null;
			}
		}
	}

	private Document parseDocument(String string) {
		return new XMLBuilder().parse(string);
	}


	private void setup() {
		try {
			InputStream stream = XMLParserBenchmark.class.getClassLoader().getResourceAsStream("periodic.xml");
			doc = IOUtils.toString(stream);
			IOUtils.closeQuietly(stream);
		} catch (IOException e) {
			throw new Error(e);
		}
	}
	
	public static void main(String[] args) {
		benchmark(new BenchmarkCallable(new XMLParserBenchmark()));
	}

}
