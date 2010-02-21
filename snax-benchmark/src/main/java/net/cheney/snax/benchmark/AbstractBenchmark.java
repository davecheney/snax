package net.cheney.snax.benchmark;

import java.io.IOException;
import java.io.InputStream;

import net.cheney.benchmarker.core.BenchmarkCallable;
import net.cheney.snax.model.Document;
import net.cheney.snax.parser.XMLBuilder;

import org.apache.commons.io.IOUtils;

public abstract class AbstractBenchmark implements Runnable {
	
	public static final int ITERATIONS = 50, OUTER_LOOP_COUNT = 50, INNER_LOOP_COUNT = 200;
	
	protected static void benchmark(BenchmarkCallable c) {
		System.out.println(String.format("Warmup: %s", c.call().toString()));
		for(int iteractions = 0 ; iteractions < ITERATIONS ; ++iteractions) {
			System.out.println(String.format("Iteraction %d: %s", iteractions+1, c.call().toString()));
		}
	}

	public Document doc;
	
	@Override
	public void run() {
		setup();
		for(int outer = 0 ; outer < OUTER_LOOP_COUNT ; ++outer) {
			for(int inner = 0 ; inner < INNER_LOOP_COUNT ; ++inner) {
				String s = printDocument();
				assert !s.isEmpty();
			}
		}
	}

	public abstract String printDocument();

	private void setup() {
		try {
			InputStream stream = AbstractBenchmark.class.getClassLoader().getResourceAsStream("periodic.xml");
			doc = new XMLBuilder().parse(IOUtils.toString(stream));
			IOUtils.closeQuietly(stream);
		} catch (IOException e) {
			throw new Error(e);
		}
	}

}
