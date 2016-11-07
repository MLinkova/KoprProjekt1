package sk.upjs.ics.kopr2016.cviko06.priklad4;

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
	public static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		System.out.println("Number of threads: " + NUMBER_OF_THREADS);
		
		File rootDir = new File(START_DIR);

		long start = System.nanoTime();
		File[] files = rootDir.listFiles();
		
		List<Future<DirSize>> futureList = new ArrayList<>();
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				DirAnalyzer analyzer = new DirAnalyzer(files[i]);
				Future<DirSize> future = EXECUTOR.submit(analyzer);
				futureList.add(future);
			}
		}
		
		for(Future<DirSize> future : futureList) {
			DirSize dirSize = future.get();
			System.out.println("Čas: "+ (System.nanoTime()-start)/1000000 +" ms   " + dirSize);
		}
		
		EXECUTOR.shutdown();
	}


}
