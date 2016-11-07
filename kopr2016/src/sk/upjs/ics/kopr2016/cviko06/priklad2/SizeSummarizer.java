package sk.upjs.ics.kopr2016.cviko06.priklad2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SizeSummarizer {

	public static final String START_DIR = "C:/users/pc12/documents";
	public static final int NUMBER_OF_THREADS = 
			Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		System.out.println("Number of threads: " + NUMBER_OF_THREADS);
		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		File rootDir = new File(START_DIR);

		long start = System.nanoTime();
		File[] files = rootDir.listFiles();
		
		List<DirAnalyzer> ulohy = new ArrayList<>();
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				DirAnalyzer analyzer = new DirAnalyzer(files[i]);
				ulohy.add(analyzer);
			}
		}
		List<Future<DirSize>> futureList = executor.invokeAll(ulohy);		
		
		for(Future<DirSize> future : futureList) {
			DirSize dirSize = future.get();
			System.out.println("Čas: "+ (System.nanoTime()-start)/1000000 +" ms   " + dirSize);
		}
		executor.shutdown();
	}


}
