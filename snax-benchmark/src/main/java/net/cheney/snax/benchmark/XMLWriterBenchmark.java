package net.cheney.snax.benchmark;

import net.cheney.snax.writer.XMLWriter;

public class XMLWriterBenchmark extends AbstractBenchmark {

	@Override
	public String printDocument() {
		return XMLWriter.write(doc.rootElement());
	}
	
	public static void main(String[] args) {
		benchmark(new BenchmarkCallable(new XMLWriterBenchmark()));
	}

}
