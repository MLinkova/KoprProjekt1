package sk.upjs.ics.kopr2016.cviko07.priklad3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SizeSummarizeMaster {

	public static void main(String[] args) throws Throwable {
		ExecutorService exekutor = Executors.newSingleThreadExecutor();
		SizeSummarizer uloha = new SizeSummarizer();
		long time = System.nanoTime();
		Future<Void> future = exekutor.submit(uloha);
		try {
			future.get(150, TimeUnit.MILLISECONDS);
			System.out.println("Prehladali sme vsetko");
		} catch (TimeoutException e) {
		// úloha ukončená po timeoute
			System.out.println("Prehladavanie skoncilo predcasne");
		} catch (ExecutionException e) {
			throw e.getCause();
		} finally {
			future.cancel(true);
		}
		exekutor.shutdown();
		exekutor.awaitTermination(365, TimeUnit.DAYS);
		System.out.println("Celkovy cas: " + (System.nanoTime() - time)/1000000.0 +" ms");
	}
}
