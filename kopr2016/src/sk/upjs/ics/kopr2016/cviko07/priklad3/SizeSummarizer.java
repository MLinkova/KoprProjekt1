package sk.upjs.ics.kopr2016.cviko07.priklad3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SizeSummarizer implements Callable<Void>{

	public static final String START_DIR = "C:/users/pc12";
	public static final int NUMBER_OF_THREADS = 
			Runtime.getRuntime().availableProcessors();

	@Override
	public Void call() throws Exception {
		System.out.println("Number of threads: " + NUMBER_OF_THREADS);
		ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
		File rootDir = new File(START_DIR);

		long start = System.nanoTime();
		File[] files = rootDir.listFiles();
		
		List<Future<DirSize>> futureList = new ArrayList<>();
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				DirAnalyzer analyzer = new DirAnalyzer(files[i]);
				Future<DirSize> future = executor.submit(analyzer);
				futureList.add(future);
			}
		}
		int i = 0;
		try {
			while( i < futureList.size()) {
				Future<DirSize> future = futureList.get(i);
				try{
					DirSize dirSize = future.get();
					System.out.println("Čas: "+ (System.nanoTime()-start)/1000000 +" ms   " + dirSize);
				} catch(ExecutionException e) {
					Throwable t = e.getCause();
					if (t instanceof DirectoryForbiddenException) {
						DirectoryForbiddenException dfe = 
								(DirectoryForbiddenException) t;
						DirSize dirSize = dfe.getDirSize();
						System.err.println("Čas: "+ (System.nanoTime()-start)/1000000 +" ms neuplna velkost z " + dirSize);
					}
				}
				i++;
			} 
		} catch (InterruptedException e1) {	//SizeSummarizerMaster mi poslal cancel
			System.out.println("Idem ukoncit ulohy");
			List<Runnable> nezacate = executor.shutdownNow();
			System.out.println("nezacatych uloh je " + nezacate.size());
			while( i < futureList.size()) {
				Future<DirSize> future = futureList.get(i);
				try{
					if (future.isDone()) {
						DirSize dirSize = future.get();
						System.out.println("Čas: "+ (System.nanoTime()-start)/1000000 +" ms   " + dirSize);
					} else {
						System.out.println("nezacata uloha");
					}
				} catch(ExecutionException e) {
					Throwable t = e.getCause();
					if (t instanceof DirectoryForbiddenException) {
						DirectoryForbiddenException dfe = 
								(DirectoryForbiddenException) t;
						DirSize dirSize = dfe.getDirSize();
						System.err.println("Čas: "+ (System.nanoTime()-start)/1000000 +" ms nepristupny adresar, neuplna velkost z " + dirSize);
					}
					if (t instanceof DirAnalyzerInteruptedException) {
						DirAnalyzerInteruptedException dfe = 
								(DirAnalyzerInteruptedException) t;
						DirSize dirSize = dfe.getDirSize();
						System.err.println("Čas: "+ (System.nanoTime()-start)/1000000 +" ms prerusene, neuplna velkost z " + dirSize);
					}
				} 
				i++;
			} 
		}
		executor.shutdown();
		return null;
	}
}
