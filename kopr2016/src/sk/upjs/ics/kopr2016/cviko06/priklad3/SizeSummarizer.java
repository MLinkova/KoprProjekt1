package sk.upjs.ics.kopr2016.cviko06.priklad3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
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
		CompletionService<DirSize> cs = new ExecutorCompletionService<>(executor);
		File rootDir = new File(START_DIR);

		long start = System.nanoTime();
		File[] files = rootDir.listFiles();
		
		int pocet = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				DirAnalyzer analyzer = new DirAnalyzer(files[i]);
				cs.submit(analyzer);
				pocet++;
			}
		}
		
		for(int i = 0; i < pocet; i++) {
			Future<DirSize> future = cs.take();
			DirSize dirSize = future.get();
			System.out.println("Čas: "+ (System.nanoTime()-start)/1000000 +" ms   " + dirSize);
		}
		executor.shutdown();
	}


}
