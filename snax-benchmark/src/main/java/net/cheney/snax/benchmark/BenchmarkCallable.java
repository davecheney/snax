package net.cheney.snax.benchmark;

import java.util.concurrent.Callable;

import org.apache.commons.lang.time.StopWatch;

public class BenchmarkCallable implements Callable<StopWatch> {

	private final Runnable r;

	public BenchmarkCallable(Runnable r) {
		this.r = r;
	}
	
	@Override
	public StopWatch call() {
		StopWatch w = new StopWatch();
		w.start();
		r.run();
		w.stop();
		return w;
	}

}
